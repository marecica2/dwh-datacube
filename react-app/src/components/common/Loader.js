import './Loader.css';
import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Box } from '@material-ui/core';
import spinnerGif from './spinner.gif';

const useStyles = makeStyles(() => ({
    root: {},
    color: {
      filter: 'sepia(100%) saturate(500%) brightness(50%) hue-rotate(186deg)',
    },
  }))
;



export default function Loader({ size = 70, variant = 'simple', height = 200 }) {
  const classes = useStyles();
  const spinner = (
    <span className={classes.root}>
      <img alt="loading" className={classes.color} src={spinnerGif} height={size} width={size} />
    </span>
  );
  const wrapper = (
    <div>
      <Box display="flex" justifyContent="center" alignItems="center" style={{ minHeight: `${height}px` }} >
        {spinner}
      </Box>
    </div>
  );
  if(variant === 'simple')
    return spinner;
  return wrapper
}