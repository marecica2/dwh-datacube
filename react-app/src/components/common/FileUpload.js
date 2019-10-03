import React, { Component } from 'react';
import { withStyles } from '@material-ui/styles';
import {Typography, CircularProgress, Grid, TableBody, Table, TableRow, TableCell} from '@material-ui/core';
import CheckIcon from '@material-ui/icons/Check';
import lightGreen from '@material-ui/core/colors/lightGreen';
import { DropzoneArea } from 'material-ui-dropzone'
import ImportApi from './import/ImportApi';

const types = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'text/csv'];

const styles = () => ({
  successIcon: {
    color: lightGreen[500],
    fontWeight: 500,
  },
  uploadStatus: {
    fontWeight: 500,
  },
});

/* eslint-disable no-param-reassign */
class FileUpload extends Component {
  constructor(props) {
    super(props);
    this.state = {
      filesProgresses: {},
    }
  }

  async componentDidUpdate(prevProps) {
    const { transaction, setUploadedFiles } = this.props;
    const files = this.props.files.slice(prevProps.files.length);
    if(files.length > 0) {
      const uploadedFiles = await Promise.all(files.map(async (file) => {
        const data = new FormData();
        data.append('file', file);
        return ImportApi.uploadFiles(transaction, data, (ProgressEvent) => {
          // eslint-disable-next-line react/no-access-state-in-setstate
          const filesProgresses = { ...this.state.filesProgresses };
          filesProgresses[file.name] = (ProgressEvent.loaded / ProgressEvent.total*100);
          this.setState({
            filesProgresses,
          });
        });
      }));
      setUploadedFiles(uploadedFiles.flat());
    }
  }

  handleChange = async (files) => {
    this.props.setFiles(files);
  };

  renderFileProgress = () => {
    const { classes } = this.props;
    return this.props.files ? this.props.files.map((file) => {
      const progress = Math.round(this.state.filesProgresses[file.name]) || 0;
      const progressIndicator = progress === 100 ?
        <CheckIcon className={classes.successIcon} /> :
        <CircularProgress variant="static" value={progress} size='1.5rem' />;
      return (
        <TableRow key={file.name}>
          <TableCell>
            <Typography variant="body1">{file.name}</Typography>
          </TableCell>
          <TableCell>
            <span className={classes.uploadStatus}>{ progressIndicator }</span>
          </TableCell>
        </TableRow>
    )
    }) : null;
  };

  render() {
    return (
      <Grid container spacing={2}>
        <Grid item xs={this.props.files.length === 0 ? 12 : 6}>
          <DropzoneArea
            filesLimit={10}
            dropzoneClass="dropzone"
            dropzoneText="Click or drop files here"
            acceptedFiles={types}
            showPreviewsInDropzone={false}
            showFileNamesInPreview={true}
            showFileNames={true}
            onChange={this.handleChange}
            showAlerts={false}
            maxFileSize={100000000}
          />
        </Grid>
        <Grid item xs={6}>
          <Table>
            <TableBody>
              { this.renderFileProgress() }
            </TableBody>
          </Table>
        </Grid>
      </Grid>
    );
  }
}

export default withStyles(styles)(FileUpload);