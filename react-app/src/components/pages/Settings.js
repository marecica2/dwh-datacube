import React from 'react';
import {Typography} from '@material-ui/core';
import Import from '../common/import/Import';

function SettingsPage() {
  return (
    <div>
      <Typography variant="h6">
        Settings
      </Typography>

      <br/>
      <Typography variant="subtitle2" gutterBottom>
        Zip code locations
      </Typography>
      <Import variant="simple"/>

      {/* <br/> */}
      {/* <Typography variant="subtitle2" gutterBottom > */}
      {/*  Taxonomy */}
      {/* </Typography> */}
      {/* <Import variant="simple"/> */}

      {/* <br/> */}
      {/* <Typography variant="subtitle2" gutterBottom> */}
      {/*  Service types */}
      {/* </Typography> */}
      {/* <Import variant="simple"/> */}
    </div>
  )
}

export default SettingsPage;