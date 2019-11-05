import {
  Box,
  Typography,
} from '@material-ui/core';
import React from 'react';
import Loader from '../Loader';
import ImportJob from './ImportJob';

export default function ImportProgressStep(props) {
  return (
    <Box display="flex" justifyContent="center">
      <Box p={5} style={{ textAlign: 'center' }}>
        <Typography variant="h3">
        Import in progress
        </Typography>
        <ImportJob {...props} />
        <Loader variant="wrapped" />
      </Box>
    </Box>
  );
}