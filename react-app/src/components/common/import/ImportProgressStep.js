import {
  Typography,
} from '@material-ui/core';
import React, { useEffect } from 'react';
import ImportApi from '../../../shared/api/import.api';
import Loader from '../Loader';
import ImportJob from './ImportJob';

export const stepName = 'Import in progress';

export default function ImportProgressStep({ transaction, config, mapping, importStatus }) {

  useEffect(() => {
    async function api() {
      if (importStatus == null || !importStatus.running) {
        await ImportApi.doImport(transaction, mapping, config);
      }
    }

    api();
  }, [transaction, mapping, importStatus, config]);

  return (
    <div style={{ textAlign: 'middle' }}>
      <Typography variant="h5">
        Import in progress
      </Typography>
      <ImportJob importStatus={importStatus} />
      <Loader/>
    </div>
  );
}