import React from 'react';
import FileUpload from './FileUpload';
import { Box } from '@material-ui/core';
import Loader from '../Loader';

export const stepName = 'File Upload';

function FileUploadStep(props) {
  return (
    <FileUpload {...props} />
  );
}

export default FileUploadStep;