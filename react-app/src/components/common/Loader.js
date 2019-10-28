import './Loader.css';
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
    root: {
      textAlign: 'center',
      padding: theme.spacing(15),
    },
    color: {
      borderColor: `${theme.palette.primary.light} transparent transparent transparent`,
      border: `5px solid ${theme.palette.primary.light}`,
    },
  }))
;

export default function Loader() {
  const classes = useStyles();
  return (
    <div className={classes.root}>
      <div className="lds-ring">
        <div className={classes.color} />
        <div className={classes.color} />
        <div className={classes.color} />
        <div className={classes.color} />
      </div>
    </div>
  );
}