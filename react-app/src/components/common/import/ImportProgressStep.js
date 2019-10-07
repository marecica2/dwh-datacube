import {
  Typography,
    LinearProgress,
} from '@material-ui/core';
import React, { useEffect } from 'react';
import ImportApi from '../../../shared/api/import.api';
import Loader from '../Loader';

export const stepName = 'Import in progress';

export default function ImportProgressStep({ transaction, config, mapping, importStatus }) {

    useEffect(() => {
        async function api() {
            if(importStatus == null || !importStatus.running) {
                await ImportApi.doImport(transaction, mapping, config);
            }
        }
        api();
    }, [transaction, mapping, importStatus, config]);

  return (
    <div style={{ textAlign: 'center'}}>
      <Typography variant="h5">
          Import in progress
      </Typography>
      {importStatus.currentFile != null && (
        <div>
          <p>{`${importStatus.currentFile + 1}. of ${importStatus.files} files`}</p>
          <LinearProgress variant="determinate" value={(importStatus.currentFile + 1 / importStatus.files) * 100}/>
          <p>{`Reading ${importStatus.currentFileName}, ${importStatus.currentRows} rows of ${importStatus.totalRows} rows`}</p>
          <LinearProgress
            variant="determinate" color="secondary"
            value={(importStatus.currentRows / importStatus.totalRows) * 100}
          />
        </div>
        )}
      <Loader />
    </div>
  );
}