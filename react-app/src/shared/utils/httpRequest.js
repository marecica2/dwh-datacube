import dwhAxios from './dwhAxios';
import { portalApiPath } from '../../settings';

const constantHeaders = {
  Accept: 'application/json',
  'Content-Type': 'application/json',
};

const httpRequest = (url, options = {}) => {
  const {
    method = 'GET',
    headers: defaultHeaders = {},
    params: defaultParams = {},
    data: defaultData,
    tenantRequest = true,
    ...otherOptions
  } = options;

  let requestUrl = `${portalApiPath}${url}`;
  if (tenantRequest) {
    const projectId = JSON.parse(localStorage.project).id;
    defaultHeaders['x-tenant'] = JSON.parse(localStorage.tenant).id;
    requestUrl = `${portalApiPath}/${projectId}${url}`;
  }

  return ({ headers = {}, params = {}, data, ...other } = {}) => {
    return dwhAxios({
      url: requestUrl,
      method,
      headers: {
        ...constantHeaders,
        ...defaultHeaders,
        ...headers,
      },
      params: {
        ...defaultParams,
        ...params,
      },
      data: data || defaultData,
      ...otherOptions,
      ...other,
    });
  };
};

export default httpRequest;
