import {
  Typography,
} from '@material-ui/core';
import React from 'react';
import Loader from '../Loader';
import ImportJob from './ImportJob';

export const stepName = 'Import in progress';

export default function ImportProgressStep(props) {
  return (
    <div style={{ textAlign: 'middle' }}>
      <Typography variant="h5">
        Import in progress
      </Typography>
      <ImportJob {...props} />
      <Loader/>
    </div>
  );
}