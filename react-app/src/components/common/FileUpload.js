import React, { Component } from 'react';
import { withStyles } from '@material-ui/styles';
import {Typography, Button, CircularProgress, Grid} from '@material-ui/core';
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
  uploadEntry: {
    display: 'flex',
  },
  uploadStatus: {
    flexGrow: 1,
    fontWeight: 500,
    textAlign: 'right',
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

  handleChange = (files) => {
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
        <div key={file.name} className={classes.uploadEntry}>
          <Typography variant="subtitle1">{file.name}</Typography>
          <span className={classes.uploadStatus}>{ progressIndicator }</span>
        </div>
    )
    }) : null;
  };

  onUpload = async () => {
    const { transaction, setFiles } = this.props;
    const uploadedFiles = await Promise.all(this.props.files.map(async (file) => {
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
    console.log('all uploads finished', uploadedFiles);
    setFiles(uploadedFiles.flat());
  };

  render() {
    return (
      <div>
        <Grid container spacing={2}>
          <Grid item xs={this.props.files.length === 0 ? 12 : 8}>
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
          <Grid item xs={4}>
            { this.renderFileProgress() }
          </Grid>
        </Grid>
        <p>
          <Button variant="contained" onClick={this.onUpload}>
          Upload
          </Button>
        </p>
      </div>
    );
  }
}

export default withStyles(styles)(FileUpload);