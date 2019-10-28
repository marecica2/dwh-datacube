import React from 'react';
import { Typography } from '@material-ui/core';
import crudApi from '../../shared/api/crud.api'
import Grid from '../common/Grid';

function SettingsPage() {
  const api = crudApi( { url: '/zip-code-locations', relation: 'zip-code-locations', tenantRequest: false });

  return (
    <div>
      <Typography variant="h1">
        Settings
      </Typography>

      <br/>
      <Typography variant="h6" gutterBottom>
        Zip code locations
      </Typography>
      <Grid
        columnsConfig={[
          { name: 'zipCode', title: 'Zip Code' },
          { name: 'latitude', title: 'Latitude' },
          { name: 'longitude', title: 'Longitude' },
        ]}
        crudApi={api}
        editable={true}
        uploader={true}
      />

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