import React from 'react';
import {Typography} from '@material-ui/core';
import Import from '../common/import/Import';

function ImportPage() {
  return (
    <div>
      <Typography variant="h3" gutterBottom>
        Import
      </Typography>
      <Import />
    </div>
  )
}

export default ImportPage;