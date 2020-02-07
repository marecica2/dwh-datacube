import React from 'react';
import { render, fireEvent, waitForElement } from '@testing-library/react';
// import '@testing-library/jest-dom/extend-expect';
import axiosMock from 'axios';
import FileUpload from './FileUpload';

jest.mock('axios')

const useState = () => {
  const state = {};
  return handler => handler(state);
};

describe('FileUpload', () => {
  const files = [{ name: 'file1.xlsx'}];
  const defaultProps = {
    api: {
      // uploadFiles: jest.fn().mockResolvedValue(files[0].name),
      uploadFiles : (transactionId, files, onUploadProgress) => {
        onUploadProgress({ loaded: 50, total: 100});
        onUploadProgress({ loaded: 100, total: 100});
        return files;
      },
    },
    transaction: '1234',
    files: [],
    setFiles: useState(),
    setUploadInProgress: jest.fn(),
    setMappingConfig: jest.fn(),
    setPreview: jest.fn(),
  };

  test('render FileUpload with files', async () => {
    const props = {
      ...defaultProps,
      files,
    };

    const { getByText, rerender } = render(<FileUpload {...props} />);

    axiosMock.post.mockResolvedValueOnce({
      data: ['file1.xlsx'],
    });

    props.files = [{ name: 'file1.xlsx'}, { name: 'file2.xlsx'}];
    rerender(<FileUpload {...props } />);
    expect(getByText('file1.xlsx'));
    expect(getByText('file2.xlsx'));
  });

});