import axios from 'axios';

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

const client = axios.create({
  baseURL: '/api',
  headers: {'X-Custom-Header': 'foobar'},
});

export default {
  initImport: async () => {
    const { data } = await client.get('/import');
    return data;
  },

  uploadFiles: async (data, onUploadProgress = () => {}) => {
    const { data: files } = await client.post('/import', data, {
      onUploadProgress,
    });
    return files;
  },

  getMapping: async (data) => {
    const resp = await client.post('/import', data);
    console.log(resp);
    return {
      destColumns: {
        'transaction': { required: true },
        'cost': { required: true,  number: true },
        'originCity': { },
        'originZip': { },
        'originState': { },
        'destinationCity': { },
        'destinationZip': { },
        'destinationState': { },
      },
      sourceColumns : {
        'transaction': '1234',
        'cost': 20.0,
        'originCity': 'Boston',
        'originZip': '896454',
        'originState': 'Winnipeg',
        'destinationCity': 'Atlanta',
        'destinationZip': '854564',
        'destinationState': 'Wyoming',
      },
    }
  },

  getPreview: async (tx) => {
    await sleep(1000);
    return [
        {
        'transaction': '1234',
        'cost': 20.0,
        'origin': 'Chicago',
        'destination': 'Atlanta',
        },
        {
        'transaction': '1237',
        'cost': 80.0,
        'origin': 'Boston',
        'destination': 'New Jersey',
        },
    ];
  },
}