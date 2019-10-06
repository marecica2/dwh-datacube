import axios from 'axios';

// function sleep(ms) {
//   return new Promise(resolve => setTimeout(resolve, ms));
// }

const client = axios.create({
  baseURL: '/api',
  headers: {'X-Custom-Header': 'foobar'},
});

export default {
  initImport: async () => {
    const { data } = await client.get('/import');
    return data;
  },

  uploadFiles: async (transactionId, files, onUploadProgress = () => {}) => {
    const { data } = await client.post(`/import/${transactionId}`, files, {
      onUploadProgress,
    });
    return data;
  },

  getMapping: async (transactionId, files) => {
    const { data } = await client.post(`/import/${transactionId}/mapping`, { files });
    return data;
  },

  getPreview: async (transactionId, mapping) => {
    const { data } = await client.post(`/import/${transactionId}/preview`, { mapping });
    return data;
  },

  doImport: async (transactionId, mapping, config) => {
    const { data } = await client.post(`/import/${transactionId}/start`, { mapping, config });
    return data;
  },
}