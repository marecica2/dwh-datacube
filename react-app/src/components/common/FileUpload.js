import React, { useEffect, useRef } from 'react';
import { CircularProgress, IconButton } from '@material-ui/core';
import UploadIcon from '@material-ui/icons/CloudUpload';

function FileUpload({
                      style, uploadApi, before = () => {
  }, after = () => {
  },
                    }) {
  const [progress, setProgress] = React.useState();
  const [file, setFile] = React.useState();
  const inputRef = useRef();

  useEffect(() => {
    async function fetchData() {
      if (file && file.uploadStarted == null) {
        await before();
        const data = new FormData();
        data.append('file', file);
        setFile(() => {
          return {
            ...file,
            uploadStarted: true,
          }
        });
        try {
          await uploadApi({
            data,
            onUploadProgress: (ProgressEvent) => {
              const percentProgress = Math.round((ProgressEvent.loaded / ProgressEvent.total * 100));
              setProgress(percentProgress)
            },
          });
          await after();
        } finally {
          setFile(null);
          setProgress(null);
          inputRef.current.value = null;
        }
      }

    }

    fetchData();
  }, [inputRef, file, setProgress, uploadApi, before, after]);

  const addFile = () => {
    setFile(inputRef.current.files[0]);
  };

  return (
    <span style={style}>
      {progress ?
        <CircularProgress variant="static" value={progress} size='1.5rem'/> :
        (
          <IconButton color="primary" onClick={() => inputRef.current.click()}>
            <UploadIcon/>
          </IconButton>
        )
      }
      <input
        type="file"
        ref={inputRef}
        onChange={() => addFile()}
        style={{ display: 'none' }}
        multiple={false}
        accept=".csv, .xlsx, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
      />
    </span>
  );
}

export default FileUpload;