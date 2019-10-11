import React from 'react';
import { LinearProgress } from '@material-ui/core';

export default function ImportJob(props) {
  const { importStatus } = props;
  return (
    <div>
      {importStatus && importStatus.running && (
        <div>
          <p>{`${importStatus.file + 1}. of ${importStatus.files} files`}</p>
          <LinearProgress variant="determinate" value={(importStatus.file + 1 / importStatus.files) * 100}/>
          <p>
            <span>{`Reading ${importStatus.fileName} `}</span>
            {importStatus.rowsCount}
            <span> rows of </span>
            {importStatus.totalRowsCount}
          </p>
          <LinearProgress
            variant="determinate" color="secondary"
            value={(importStatus.rowsCount / importStatus.totalRowsCount) * 100}
          />
        </div>
      )}
    </div>
  )
}