import React from 'react';
import FileUpload from './FileUpload';

export const stepName = 'File Upload';

function FileUploadStep(props) {
  return (
    <div id="fileUploadStep">
      <FileUpload {...props} />
    </div>
  );
}

export default FileUploadStep;