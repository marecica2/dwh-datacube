import React from 'react';
import { Grid } from '@material-ui/core';
import FileUpload from '../FileUpload';

function FileUploadStep(props) {
  return (
    <Grid container spacing={1}>
      <Grid item xs={12}>
        <FileUpload {...props} />
      </Grid>
    </Grid>
  );
}

export default FileUploadStep;