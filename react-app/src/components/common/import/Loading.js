import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import CircularProgress from '@material-ui/core/CircularProgress';

const useStyles = makeStyles(theme => ({
    root: {
      textAlign: 'center',
      padding: theme.spacing(15),
    },
}));

export default function CircularIndeterminate() {
    const classes = useStyles();
    return (
      <div className={classes.root}>
        <CircularProgress />
      </div>
    );
}