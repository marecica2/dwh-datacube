import React, { useContext } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {
  Typography,
  Grid,
  Paper,
  Table,
  TableCell,
  TableRow,
  TableBody, LinearProgress,
} from '@material-ui/core';
import AppContext from '../context/AppContext';

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

function Home() {
  const classes = useStyles();
  const appContext = useContext(AppContext);
  const { importStatus } = appContext.jobs;

  return (
    <div>
      <Typography variant="h6" paragraph>
        Datacube Dashboard
      </Typography>
      <Grid container spacing={3}>
        <Grid item xs={6}>
          <Paper className={classes.paper}>
            <Typography variant="h6" paragraph>
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
          <Paper className={classes.paper}>
            <Typography variant="h6">
              Backgroud jobs
            </Typography>
            {importStatus && importStatus.running && (
              <div>
                <p>{`${importStatus.currentFile + 1}. of ${importStatus.files} files`}</p>
                <LinearProgress
                  variant="determinate"
                  value={(importStatus.currentFile + 1 / importStatus.files) * 100}
                />
                <p>{`Reading ${importStatus.currentFileName}, ${importStatus.currentRows} rows of ${importStatus.totalRows} rows`}</p>
                <LinearProgress
                  variant="determinate" color="secondary"
                  value={(importStatus.currentRows / importStatus.totalRows) * 100}
                />
              </div>
            )}
          </Paper>
        </Grid>
        <Grid item xs={12}>
          <Paper className={classes.paper}>Event log</Paper>
        </Grid>
      </Grid>
    </div>
  );
}

export default Home;