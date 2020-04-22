import React, { useContext, useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {
  Button,
  Typography,
  Grid,
  Paper,
  Table,
  TableCell,
  TableRow,
  TableBody,
} from '@material-ui/core';
import handleStreamingDownload from '../../shared/util/download';
import msToTime from '../../shared/util/time';
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

function Home() {
  const classes = useStyles();
  const { state } = useContext(AppContext);
  const [stats, setStats] = useState();

  useEffect(() => {
    const api = async () => {
      const data = await importApi.getStats();
      setStats(data);
    };
    api();
  }, [setStats, state.importStatus.running]);

  const click = async () => {
    const response = await importApi.getErrors();
    handleStreamingDownload(response);
  };

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
      <Typography variant="h3" paragraph>
        Dashboard
      </Typography>
      <Grid container spacing={3}>
        <Grid item xs={6}>
          <Paper className={classes.paper}>
            <Typography variant="h6" paragraph>
              Data statistics
            </Typography>
            <Table className={classes.table}>
              <TableBody>
                <TableRow key="status">
                  <TableCell scope="row">Status</TableCell>
                  <TableCell align="right">
                    {stats != null && stats.status}
                  </TableCell>
                </TableRow>
                <TableRow key="duration">
                  <TableCell scope="row">Duration</TableCell>
                  <TableCell align="right">
                    {stats != null && msToTime(stats.duration)}
                  </TableCell>
                </TableRow>
                <TableRow key="totalRows">
                  <TableCell scope="row">Rows imported</TableCell>
                  <TableCell align="right">{stats && stats.totalRows != null && stats.totalRows - stats.skippedRows}</TableCell>
                </TableRow>
                <TableRow key="skippedRows">
                  <TableCell scope="row">Rows skipped</TableCell>
                  <TableCell align="right">
                    {stats != null && stats.skippedRows}
                  </TableCell>
                </TableRow>
                <TableRow key="download">
                  <TableCell scope="row">Errors download</TableCell>
                  <TableCell align="right">
                    {stats && stats.status === 'COMPLETED' && <Button onClick={click} color="primary">errors.zip</Button>}
                  </TableCell>
                </TableRow>
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
