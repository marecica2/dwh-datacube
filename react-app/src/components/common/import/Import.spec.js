/* eslint-disable import/first */
import React from 'react';
import { render, act, fireEvent } from '@testing-library/react';

// jest.mock('material-ui-dropzone');
// import { DropzoneArea } from 'material-ui-dropzone'

jest.mock('../../../shared/api/import.api');
import { sources } from 'eventsourcemock';
import importApiMock from '../../../shared/api/import.api';

import Import from './Import';
import { AppStateProvider } from '../../context/AppContext';

// const messageEvent = new MessageEvent('foo', {
//   data: '1',
// });

const transactionId = '12345';

const uploadFileName = 'file1.csv';

const mapping = {
  'sourceColumns': {
    'S. No.': '1',
    'Supplier Name': 'UPS',
    'Business Unit': 'BU1',
  },
  'destinationColumns': {
    'id': { 'type': 'BigDecimal', 'label': 'Id' },
    'transactionId': { 'type': 'String', 'label': 'Transaction Id' },
    'supplierName': { 'type': 'String', 'label': 'Supplier Name' },
    'businessUnit': { 'type': 'String', 'label': 'Business Unit' },
  },
  'mapping': {
    'Supplier Name': 'supplierName',
    'Business Unit': 'businessUnit',
  },
};

const mappingPreset = [{
  'id': 1, 'name': 'sample.csv', 'mapping': {
    'S. No.': 'transactionId',
    'Business Unit': 'businessUnit',
    'Supplier Name': 'supplierName',
  },
}];


describe('Import workflow', () => {

  const uploadFiles = [createFile(uploadFileName, 1111, 'application/csv')];
  let ui;

  beforeAll(async () => {
    importApiMock.initImport.mockResolvedValueOnce(transactionId);
    importApiMock.uploadFiles.mockImplementationOnce(() => [uploadFileName]);
    importApiMock.getMapping.mockImplementationOnce(() => mapping);
    importApiMock.getMappingPresets.mockImplementationOnce(() => mappingPreset);

    await act(async () => {
      ui = render(<AppStateProvider><Import/></AppStateProvider>);
    });
    sources['/api/00000-00000-00001/1/status'].emitOpen();
  });

  test('File upload step', async () => {
    const data = createDtWithFiles(uploadFiles);
    const { container, debug, getByText, rerender } = ui;
    const dropzone = container.querySelector('#dropzone');

    await act(async () => {
      fireDrop(dropzone, data);
    });

    expect(getByText('file1.csv'));

    await act(async () => {
      fireEvent.click(getByText('Next'));
    });

    rerender(<AppStateProvider><Import/></AppStateProvider>);
    debug();
  });

});

function fireDrop(node, data) {
  dispatchEvt(node, 'drop', data)
}

function dispatchEvt(node, type, data) {
  const event = new Event(type, { bubbles: true });
  if (data) {
    Object.assign(event, data)
  }
  fireEvent(node, event)
}

function createDtWithFiles(files = []) {
  return {
    dataTransfer: {
      files,
      items: files.map(file => ({
        kind: 'file',
        size: file.size,
        type: file.type,
        getAsFile: () => file,
      })),
      types: ['Files'],
    },
  }
}

function createFile(name, size, type) {
  const file = new File([], name, { type });
  Object.defineProperty(file, 'size', {
    get() {
      return size
    },
  });
  return file
}