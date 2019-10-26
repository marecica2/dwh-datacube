import React from 'react';
import {Typography} from '@material-ui/core';
import Import from '../common/import/Import';

function ImportPage() {
  return (
    <div>
      <Typography variant="h1" paragraph>
        Import
      </Typography>
      <Import />
    </div>
  )
}

export default ImportPage;