import { httpRequest } from '../utils';
import { portalApiPath } from '../../settings';

const baseUrl = `${portalApiPath}/import`;

export default {
  initImport: async (options) => {
    const { data } = await httpRequest(baseUrl, {
      ...options,
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
};
