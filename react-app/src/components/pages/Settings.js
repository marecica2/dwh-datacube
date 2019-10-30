import React from 'react';
import { Typography, Tabs, Tab, Box } from '@material-ui/core';
import crudApi from '../../shared/api/crud.api'
import Grid from '../common/Grid';

const zipApi = crudApi( { url: '/zip-code-locations', relation: 'zip-code-locations', tenantRequest: false });
const taxonomyApi = crudApi( { url: '/taxonomy', relation: 'taxonomy', tenantRequest: false });
const rateCardApi = crudApi( { url: '/rate-cards', relation: 'rate-cards', tenantRequest: false });
const serviceTypeApi = crudApi( { url: '/service-types', relation: 'service-types', tenantRequest: false });

function tabProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

function TabPanel(props) {
  const { children, value, index, ...other } = props;
  return (
    <Typography
      component="div"
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      <Box p={0}>{children}</Box>
    </Typography>
  );
}

function SettingsPage() {
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <div>
      <Typography variant="h3" gutterBottom>
        Settings
      </Typography>

      <Tabs value={value} onChange={handleChange} aria-label="simple tabs example">
        <Tab label="Zip codes" {...tabProps(0)} />
        <Tab label="Taxonomy" {...tabProps(1)} />
        <Tab label="Rate cards" {...tabProps(2)} />
        <Tab label="Service types" {...tabProps(3)} />
      </Tabs>

      <TabPanel value={value} index={0}>
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
      </TabPanel>

      <TabPanel value={value} index={1}>
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
      </TabPanel>
      <TabPanel value={value} index={2}>
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
      </TabPanel>

      <TabPanel value={value} index={3}>
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
      </TabPanel>

    </div>
  )
}

export default SettingsPage;