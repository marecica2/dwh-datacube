import React from 'react';
import { LinearProgress } from '@material-ui/core';

export default function ImportJob(props) {
  const { importStatus } = props;
  return (
    <div>
      {importStatus && importStatus.running && (
        <div>
          <p>{`${importStatus.currentFile + 1}. of ${importStatus.files} files`}</p>
          <LinearProgress variant="determinate" value={(importStatus.currentFile + 1 / importStatus.files) * 100}/>
          <p>
            <span>{`Reading ${importStatus.currentFileName} `}</span>
            {importStatus.currentRows}
            <span> rows of </span>
            {importStatus.totalRows}
          </p>
          <LinearProgress
            variant="determinate" color="secondary"
            value={(importStatus.currentRows / importStatus.totalRows) * 100}
          />
        </div>
      )}
    </div>
  )
}