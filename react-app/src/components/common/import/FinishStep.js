import {
  CircularProgress,
  Grid,
  makeStyles,
  Typography,
} from '@material-ui/core';
import React, { useEffect } from 'react';
import ImportApi from './ImportApi';

const useStyles = makeStyles(() => ({
}));

export const stepName = 'Import in progress';

export default function FinishStep({ transaction, config, mapping  }) {
  const classes = useStyles();

    useEffect(() => {
        function api() {
            ImportApi.doImport(transaction, mapping, config);
        }
        api();
    }, []);

  return (
    <div style={{ textAlign: 'center'}}>
      <Typography variant="h4">
          Import in progress
      </Typography>
      <CircularProgress className={classes.progress} />
    </div>
  );
}