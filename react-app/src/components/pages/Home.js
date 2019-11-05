import React, { useContext, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {
  Typography,
  Grid,
  Paper,
  Table,
  TableCell,
  TableRow,
  TableBody,
} from '@material-ui/core';
import { AppContext } from '../context/AppContext';
import ImportJob from '../common/import/ImportJob';
import importApi from '../../shared/api/import.api';


const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
  },
  paper: {
    padding: theme.spacing(2),
    color: theme.palette.text.secondary,
  },
}));

const importStats = [
  {
    label: 'Rows imported',
    key: 'rows',
    value: 20000,
  },
  {
    label: 'Dashboard',
    key: 'dashboard',
    value: 'calculated',
  },
  {
    label: 'Levers',
    key: 'levers',
    value: 'calculated',
  },
  {
    label: 'Last import',
    key: 'updated',
    value: '2019/10/02 10:43',
  },
  {
    label: 'Errors file',
    key: 'errors',
    value: 'Download errors.zip',
  },
  {
    label: 'Imported by',
    key: 'importedBy',
    value: 'John Doe',
  },
];

function Home() {
  const classes = useStyles();
  const { state } = useContext(AppContext);

  useEffect(() => {
    const api = async () => {
      const resp = await importApi.getStats();
      console.log(resp);
    };
    api();
  }, []);

  const renderJobs = () => {
    if (state.importStatus && state.importStatus.running) {
      return (
        <div>
          <Typography variant="body1">
            Import is running
          </Typography>
          <ImportJob/>
        </div>
      )
    }
    return (
      <div style={{ margin: '38px', textAlign: 'center' }}>
        <Typography variant="body1">
          No active background jobs
        </Typography>
      </div>
    )
  };

  return (
    <div>
      <Typography variant="h6" paragraph>
        Datacube Dashboard
      </Typography>
      <Grid container spacing={3}>
        <Grid item xs={6}>
          <Paper className={classes.paper}>
            <Typography variant="h6" paragraph>
              Data statistics
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
            <Typography variant="h6" paragraph>
              Admin events
            </Typography>
          </Paper>
          <br/>
          <Paper className={classes.paper}>
            <Typography variant="h6" paragraph>
              Background jobs
            </Typography>
            {renderJobs()}
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