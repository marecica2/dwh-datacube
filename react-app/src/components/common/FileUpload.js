import React, { useEffect } from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {Typography, CircularProgress, Grid, TableBody, Table, TableRow, TableCell} from '@material-ui/core';
import CheckIcon from '@material-ui/icons/Check';
import lightGreen from '@material-ui/core/colors/lightGreen';
import { DropzoneArea } from 'material-ui-dropzone'
import ImportApi from './import/ImportApi';

const types = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'text/csv'];

const useStyles = makeStyles(() => ({
  successIcon: {
    color: lightGreen[500],
    fontWeight: 500,
  },
  uploadStatus: {
    fontWeight: 500,
  },
}));

function FileUpload({ transaction, files, setFiles, setUploadInProgress }) {
  const classes = useStyles();
  const [progresses, setProgresses] = React.useState({});

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

        await ImportApi.uploadFiles(transaction, data, (ProgressEvent) => {
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
  }, [transaction, files, setFiles, setUploadInProgress, progresses]);

  const handleChange = (newFiles) => {
    const addedFiles = newFiles
        .filter(file => !Object.keys(files).includes(file.name));
    setFiles({
      ...files,
      ...addedFiles.reduce((acc, file) => ({ ...acc, [file.name]: file}), {}),
    });
  };

  const renderFileProgress = () => {
    return Object.values(files).map((file) => {
      const progress = progresses[file.name] || 0;

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
    })
  };

  return (
    <Grid container spacing={2}>
      <Grid item xs={files.length === 0 ? 12 : 6}>
        <DropzoneArea
          filesLimit={10}
          dropzoneClass="dropzone"
          dropzoneText="Click or drop files here"
          acceptedFiles={types}
          showPreviewsInDropzone={false}
          showFileNamesInPreview={true}
          showFileNames={true}
          onChange={handleChange}
          showAlerts={false}
          maxFileSize={100000000}
        />
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