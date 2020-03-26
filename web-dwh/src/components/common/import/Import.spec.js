/* eslint-disable import/first */
import React from 'react';
import { render, act, fireEvent } from '@testing-library/react';
import { sources } from 'eventsourcemock';

// jest.mock('material-ui-dropzone');
// import { DropzoneArea } from 'material-ui-dropzone'

jest.mock('../../../shared/api/import.api');
jest.mock('../../../shared/api/appState.api');
import importApiMock from '../../../shared/api/import.api';
import appStateApiMock from '../../../shared/api/appState.api';

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
    'Organization': 'BU1',
  },
  'destinationColumns': {
    'id': { 'type': 'BigDecimal', 'label': 'Id' },
    'transactionId': { 'type': 'String', 'label': 'Transaction Id' },
    'supplierName': { 'type': 'String', 'label': 'Supplier Name' },
    'businessUnit': { 'type': 'String', 'label': 'Business Unit' },
  },
  'mapping': {
    'Supplier Name': 'supplierName',
    'Organization': 'businessUnit',
  },
};

const mappingPreset = [{
  'id': 1, 'name': 'sample.csv', 'mapping': {
    'S. No.': 'transactionId',
    'Business Unit': 'businessUnit',
    'Supplier Name': 'supplierName',
  },
}];

const preview = [
  {
    'entity': {
      'id': 1,
      'transactionId': 1234,
      'supplierName': 'UPS',
      'cost': null,
    },
    'valid': false,
    'errors': {
      'cost': [
        'must not be null',
      ],
    },
  },
];


describe('Import workflow', () => {

  const uploadFiles = [createFile(uploadFileName, 1111, 'application/csv')];
  const data = createDtWithFiles(uploadFiles);
  let ui;

  beforeEach(async () => {
    importApiMock.initImport.mockResolvedValueOnce(transactionId);
    importApiMock.uploadFiles.mockImplementationOnce(() => [uploadFileName]);
    importApiMock.getMapping.mockImplementationOnce(() => mapping);
    importApiMock.getMappingPresets.mockImplementationOnce(() => mappingPreset);
    importApiMock.getPreview.mockImplementationOnce(() => preview);
    appStateApiMock.getAppStateUrl.mockImplementationOnce(() => '/api/importer/status');

    await act(async () => {
      ui = render(<AppStateProvider><Import/></AppStateProvider>);
    });
    sources['/api/importer/status'].emitOpen();
  });

  test('Display correct step on startup', async () => {
    const { container } = ui;
    expect(container.querySelector('#fileUploadStep'));
    expect(container.querySelector('#mappingStep')).toBeNull();
    expect(container.querySelector('#previewStep')).toBeNull();
  });


  test('Fileupload step', async () => {
    const { container, getByText } = ui;
    const dropzone = container.querySelector('#dropzone');
    await act(async () => {
      fireDrop(dropzone, data);
    });
    expect(getByText('file1.csv'));
  });

  test('Mapping step', async () => {
    const { container, getByText } = ui;
    const dropzone = container.querySelector('#dropzone');
    await act(async () => {
      fireDrop(dropzone, data);
    });
    await act(async () => {
      fireEvent.click(getByText('Next'));
    });
    expect(container.querySelector('#mappingStep')).not.toBeNull();
    expect(getByText('Column name'));
    expect(getByText('Preview'));
    expect(getByText('Mapped column'));
    expect(getByText('Organization'));
    expect(getByText('BU1'));
    expect(getByText('Business Unit'));

    const mockMappingPreset = jest.fn();
    importApiMock.createMappingPreset.mockImplementationOnce(mockMappingPreset);

    await act(async () => {
      fireEvent.click(getByText('Business Unit'));
    });

    expect(getByText('Required fields'));
    expect(getByText('Optional fields'));
    expect(getByText('Transaction Id'));

    await act(async () => {
      fireEvent.click(getByText('Transaction Id'));
    });

    await act(async () => {
      fireEvent.click(getByText('Save this preset'));
    });

    expect(mockMappingPreset).toHaveBeenCalledWith(
      {
        mapping: {
          'Organization': 'transactionId',
          'Supplier Name': 'supplierName',
        },
        name: 'file1.csv',
      },
    );
  });

  test('Preview step', async () => {
    const { container, getByText } = ui;
    const dropzone = container.querySelector('#dropzone');
    await act(async () => {
      fireDrop(dropzone, data);
    });
    await act(async () => {
      fireEvent.click(getByText('Next'));
    });
    await act(async () => {
      fireEvent.click(getByText('Next'));
    });

    expect(container.querySelector('#previewStep'));
    expect(getByText('1234'));
    expect(getByText('UPS'));
    expect(getByText('must not be null'));
  });

  test('Config step', async () => {
    const { container, getByText, getByLabelText } = ui;
    const dropzone = container.querySelector('#dropzone');
    await act(async () => {
      fireDrop(dropzone, data);
    });
    await act(async () => {
      fireEvent.click(getByText('Next'));
    });
    await act(async () => {
      fireEvent.click(getByText('Next'));
    });
    await act(async () => {
      fireEvent.click(getByText('Next'));
    });

    expect(container.querySelector('#configStep'));
    const mockImport = jest.fn();
    importApiMock.doImport.mockImplementationOnce(mockImport);
    await act(async () => {
      fireEvent.click(getByLabelText('Skip row'));
    });

    await act(async () => {
      fireEvent.click(getByText('Finish'));
    });

    expect(mockImport).toHaveBeenCalledWith(
      '12345',
      { 'Organization': 'businessUnit', 'Supplier Name': 'supplierName' },
      { 'skipStrategy': 'row' },
    );

    expect(container.querySelector('#fileUploadStep'));
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
