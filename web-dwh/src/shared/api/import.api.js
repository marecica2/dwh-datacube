import { httpRequest } from '../utils';

const baseUrl = '/importer/import';

export default {
  initImport: async (options) => {
    const { data } = await httpRequest(`${baseUrl}/init`, {
      ...options,
      method: 'POST',
    })();
    return data;
  },

  uploadFiles: async (transactionId, files, onUploadProgress = () => {
  }) => {
    const { data } = await httpRequest(`${baseUrl}/${transactionId}`, {
      method: 'POST',
      data: files,
      onUploadProgress,
    })();
    return data;
  },

  getMapping: async (transactionId, files) => {
    const { data } = await httpRequest(`${baseUrl}/${transactionId}/mapping`, {
      method: 'POST',
      data: { files },
    })();
    return data;
  },

  getPreview: async (transactionId, mapping) => {
    const { data } = await httpRequest(`${baseUrl}/${transactionId}/preview`, {
      method: 'POST',
      data: { mapping },
    })();
    return data;
  },

  doImport: async (transactionId, mapping, config) => {
    const { data } = await httpRequest(`${baseUrl}/${transactionId}/start`, {
      method: 'POST',
      data: { mapping, config },
    })();
    return data;
  },

  createMappingPreset: async (mapping) => {
    const { data } = await httpRequest(`${baseUrl}/mapping-presets`, {
      method: 'POST',
      data: mapping,
    })();
    return data;
  },

  getMappingPresets: async () => {
    const { data } = await httpRequest(`${baseUrl}/mapping-presets`, {
      method: 'GET',
    })();
    return data;
  },

  getStats: async () => {
    const { data } = await httpRequest(`${baseUrl}/stats`, {
      method: 'GET',
    })();
    return data;
  },

  getErrors: async () => {
    const resp = await httpRequest(`${baseUrl}/errors.zip`, {
      method: 'GET',
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
        'Content-Type': 'application/octet-stream',
      },
    })();
    return resp;
  },
};
