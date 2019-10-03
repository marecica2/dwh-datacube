import {
  CircularProgress,
  Grid,
  makeStyles,
  Typography,
} from '@material-ui/core';
import React from 'react';

const useStyles = makeStyles(() => ({
}));

export const stepName = 'Import in progress';

export default function FinishStep() {
  const classes = useStyles();
  return (
    <Grid container>
      <Typography variant="h4">
          Import in progress
      </Typography>
      <CircularProgress className={classes.progress} />
    </Grid>
  );
}