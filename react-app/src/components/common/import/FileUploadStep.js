import React from 'react';
import FileUpload from './FileUpload';

export const stepName = 'File Upload';

function FileUploadStep(props) {
  return (
    <FileUpload {...props} />
  );
}

export default FileUploadStep;