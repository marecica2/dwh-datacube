import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {
  Typography,
  Grid,
  Paper,
  Table,
  TableCell,
  TableRow,
  TableBody,
} from '@material-ui/core';

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
  },
  paper: {
    padding: theme.spacing(2),
    textAlign: 'center',
    color: theme.palette.text.secondary,
  },
}));

const importStats = [
  {
    label: 'Import running',
    key: 'ImportRunning',
    value: 'false',
  },
  {
    label: 'Rows imported',
    key: 'rows',
    value: 20000,
  },
];

function Supplier() {
  const classes = useStyles();
  return (
    <div>
      <Typography variant="h6" paragraph>
          Datacube Dashboard
      </Typography>
      <Grid container spacing={3}>
        <Grid item xs={6}>
          <Paper className={classes.paper}>
            <Typography variant="h7" paragraph>
              Data stats
            </Typography>
            <Table className={classes.table}>
              <TableBody>
                {importStats.map(row => (
                  <TableRow key={row.key}>
                    <TableCell scope="row">{row.label}</TableCell>
                    <TableCell align="right">{row.value}</TableCell>
                  </TableRow>
                  ))}
              </TableBody>
            </Table>
          </Paper>
        </Grid>
        <Grid item xs={6}>
          <Paper className={classes.paper}>Background jobs</Paper>
        </Grid>
        <Grid item xs={12}>
          <Paper className={classes.paper}>Event log</Paper>
        </Grid>
      </Grid>
    </div>
  );
}

export default Supplier;