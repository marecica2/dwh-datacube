import React from 'react';
import { Typography } from '@material-ui/core';
import crudApi from '../../shared/api/crud.api'
import Grid from '../common/Grid';

function SettingsPage() {
  const zipApi = crudApi( { url: '/zip-code-locations', relation: 'zip-code-locations', tenantRequest: false });
  const taxonomyApi = crudApi( { url: '/taxonomy', relation: 'taxonomy', tenantRequest: false });
  const rateCardApi = crudApi( { url: '/rate-cards', relation: 'rate-cards', tenantRequest: false });
  const serviceTypeApi = crudApi( { url: '/service-types', relation: 'service-types', tenantRequest: false });

  return (
    <div>
      <Typography variant="h1">
        Settings
      </Typography>

      <br/>
      <Typography variant="h4" gutterBottom>
        Zip code locations
      </Typography>
      <Grid
        columnsConfig={[
          { name: 'zipCode', title: 'Zip Code' },
          { name: 'latitude', title: 'Latitude' },
          { name: 'longitude', title: 'Longitude' },
        ]}
        crudApi={zipApi}
        editable={true}
        uploader={true}
        downloader={true}
      />

      <br />
      <Typography variant="h4" gutterBottom>
        Taxonomy
      </Typography>
      <Grid
        columnsConfig={[
          { name: 'id', title: 'Id' },
          { name: 'name', title: 'Name' },
          { name: 'expedite', title: 'Expedite' },
          { name: 'serviceLevelDownType1', title: 'Service Level Down Type 1' },
          { name: 'serviceLevelDownType2', title: 'Service Level Down Type 2' },
          { name: 'discountGroup', title: 'Service Type Group' },
        ]}
        crudApi={taxonomyApi}
        editable={false}
        uploader={true}
        downloader={false}
      />

      <br />
      <Typography variant="h4" gutterBottom>
        Rates
      </Typography>
      <Grid
        columnsConfig={[
          { name: 'supplierName', title: 'Supplier Name' },
          { name: 'supplierServiceType', title: 'Service Type' },
          { name: 'supplierZone', title: 'Zone' },
          { name: 'weight', title: 'Weight' },
          { name: 'weightType', title: 'Weight Type' },
          { name: 'price', title: 'Price' },
          { name: 'pricePerUnit', title: 'Price Per Unit' },
        ]}
        crudApi={rateCardApi}
        editable={false}
        uploader={true}
        downloader={false}
      />

      <br />
      <Typography variant="h4" gutterBottom>
        Service types
      </Typography>
      <Grid
        columnsConfig={[
          { name: 'supplierName', title: 'Supplier Name' },
          { name: 'supplierServiceType', title: 'Service Type' },
          { name: 'standardServiceTypeLetter', title: 'Parcel Type' },
          { name: 'standardServiceTypeParcel', title: 'Letter Type' },
          { name: 'standardServiceTypeLetterKey', title: 'Letter Key' },
          { name: 'standardServiceTypeParcelKey', title: 'Parcel Key' },
        ]}
        crudApi={serviceTypeApi}
        editable={false}
        uploader={true}
        downloader={false}
      />
    </div>
  )
}

export default SettingsPage;