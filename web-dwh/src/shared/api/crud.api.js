import { httpRequest } from '../utils';

export default ({ url, relation, ...config }) => {
  return {
    fileUpload: async ({
                         params,
                         data,
                         onUploadProgress = () => {
                         },
                       }) => {
      const { data: resp } = await httpRequest(`${url}/import`, {
        method: 'POST',
        data,
        params,
        headers: { 'Content-Type': 'multipart/form-data' },
        onUploadProgress,
        ...config,
      })();
      return resp;
    },

    paginate: async ({ currentPage, pageSize }) => {
      const { data: resp } = await httpRequest(url, {
        method: 'GET',
        params: { page: currentPage, size: pageSize },
        ...config,
      })();
      return { items: resp._embedded[relation], pagination: resp.page };
    },

    create: async ({ params, data }) => {
      const { data: resp } = await httpRequest(url, {
        method: 'POST',
        params,
        data,
        ...config,
      })();
      return resp;
    },

    update: async ({ id, data, params }) => {
      const { data: resp } = await httpRequest(`${url}/${id}`, {
        method: 'PATCH',
        params,
        data,
        ...config,
      })();
      return resp;
    },

    patch: async ({ id, data, params }) => {
      const { data: resp } = await httpRequest(`${url}/${id}`, {
        method: 'PATCH',
        params,
        data,
        ...config,
      })();
      return resp;
    },

    delete: async ({ id, params }) => {
      const { data: resp } = await httpRequest(`${url}/${id}`, {
        method: 'DELETE',
        params,
        ...config,
      })();
      return resp;
    },
  }
};
