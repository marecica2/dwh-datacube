import React, { useEffect, useCallback } from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Typography, CircularProgress, Grid, TableBody, Table, TableRow, TableCell} from '@material-ui/core';
import CheckIcon from '@material-ui/icons/Check';
import lightGreen from '@material-ui/core/colors/lightGreen';
import Dropzone from 'react-dropzone'

// const types = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'text/csv'];

const useStyles = makeStyles(() => ({
  successIcon: {
    color: lightGreen[500],
    fontWeight: 500,
  },
  uploadStatus: {
    fontWeight: 500,
  },
  uploader: {
    textAlign: 'center',
    fontSize: '1.3em',
    background: '#f0f0f0',
    lineHeight: '300px',
    cursor: 'pointer',
  },
}));

function FileUpload({ api, transaction, files, setFiles, setUploadInProgress, setMappingConfig, setPreview }) {
  const classes = useStyles();
  const [progresses, setProgresses] = React.useState({});

  const onDrop = useCallback((newFiles) => {
    const addedFiles = newFiles
      .filter(file => !Object.keys(files).includes(file.name));
    setFiles({
      ...files,
      ...addedFiles.reduce((acc, file) => ({ ...acc, [file.name]: file}), {}),
    });
  }, [files, setFiles]);

  useEffect(() => {
    async function fetchData() {
      const filesToUpload = Object.values(files).filter(file => file.uploadStarted == null);
      if(filesToUpload.length === 0)
        return;

      await Promise.all(filesToUpload.map(async (file) => {
        const data = new FormData();
        data.append('file', file);

        setFiles((prevState) => {
          // eslint-disable-next-line no-param-reassign
          file.uploadStarted = true;
          return {
            ...prevState,
            [file.name]: file,
          }
        });
        setUploadInProgress(true);
        setMappingConfig(null);
        setPreview(null);

        await api.uploadFiles(transaction, data, (ProgressEvent) => {
          setProgresses( (prev) => {
            return {
              ...prev,
              [file.name]: Math.round((ProgressEvent.loaded / ProgressEvent.total * 100)),
            }
          });
        });

        setFiles((prevState) => {
          // eslint-disable-next-line no-param-reassign
          file.uploadFinished = true;
          return {
            ...prevState,
            [file.name]: file,
          }
        });
      }));
      setUploadInProgress(false);
    }
      fetchData();
  }, [api, transaction, files, setFiles, setUploadInProgress, setMappingConfig, setPreview, progresses]);

  const renderFileProgress = () => {
    return Object.values(files).map((file) => {
      const progress = progresses[file.name] || 0;

      const progressIndicator = (progress === 100 || file.uploadFinished) ?
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
    })
  };

  return (
    <Grid container spacing={2}>
      <Grid item xs={Object.keys(files).length === 0 ? 12 : 6}>
        <Dropzone onDrop={onDrop}>
          {({getRootProps, getInputProps}) => (
            <section>
              <div {...getRootProps()} id="dropzone" className={classes.uploader}>
                <input {...getInputProps()} />
                <p>Drag drop some files here, or click to select files</p>
              </div>
            </section>
            )}
        </Dropzone>
      </Grid>
      <Grid item xs={6}>
        <Table>
          <TableBody>
            { renderFileProgress() }
          </TableBody>
        </Table>
      </Grid>
    </Grid>
  );
}

export default FileUpload;