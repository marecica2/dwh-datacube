import React from 'react';
import {Grid, Typography} from '@material-ui/core';
import { contextWrapper } from './ImportContext';
import FileUpload from '../FileUpload';

function FileUploadStep({ actions }) {
  return (
    <Grid container spacing={1}>
      <Grid item xs={12}>
        <Typography variant="body1" paragraph>
            Upload files ( .xls, .xlsx, .csv )
        </Typography>
        <FileUpload actions={actions} />
      </Grid>
    </Grid>
  );
}

export default contextWrapper(FileUploadStep);