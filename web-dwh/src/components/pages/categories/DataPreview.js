import React from 'react';
import Grid from '../../common/Grid';
import crudApi from '../../../shared/api/crud.api';

const factsApi = crudApi( { url: '/olap/facts', relation: 'facts', tenantRequest: true });

function DataPreview() {
  return (
    <div>
      <Grid
        columnsConfig={[
          { name: 'transactionId', title: 'Transaction Id', width: 60 },
          { name: 'businessUnit', title: 'Business Unit', width: 60 },
          { name: 'supplierName', title: 'Supplier Name', width: 60 },
          { name: 'originCity', title: 'Origin City' },
          { name: 'originState', title: 'Origin State' },
          { name: 'originCountry', title: 'Origin Country' },
          { name: 'originZip', title: 'Origin Zip' },
          { name: 'destinationCity', title: 'Destination City' },
          { name: 'destinationState', title: 'Destination State' },
          { name: 'destinationCountry', title: 'Destination Country' },
          { name: 'destinationZip', title: 'Destination Zip' },
          { name: 'serviceType', title: 'Supplier Service Type', width: 100 },
          { name: 'standardServiceType', title: 'Standard Service Type' },
          { name: 'zone', title: 'Zone' },
          { name: 'cost', title: 'Cost (USD)' },
          { name: 'billableWeight', title: 'Billable Weight (p.)' },
          { name: 'length', title: 'Length (in)' },
          { name: 'height', title: 'Height (in)' },
          { name: 'width', title: 'Width (in)' },
          { name: 'discount', title: 'Discount (%)' },
          { name: 'distance', title: 'Distance (mi)' },
          { name: 'excluded', title: 'Excluded' },
        ]}
        crudApi={factsApi}
        editable={true}
        uploader={false}
        downloader={true}
      />
    </div>
    )
}

export default DataPreview;
