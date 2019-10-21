import dwhAxios from './dwhAxios';
import { portalApiPath } from '../../settings';

const constantHeaders = {
  Accept: 'application/json',
  'Content-Type': 'application/json',
};

const httpRequest = (url, options = {}) => {
  const projectId = sessionStorage.projectId;
  const {
    method = 'GET',
    headers: defaultHeaders = { 'x-tenant': sessionStorage.tenant },
    params: defaultParams = { },
    data: defaultData,
    ...otherOptions
  } = options;

  return ({ headers = {}, params = {}, data, ...other } = {}) => {
    return dwhAxios({
      url: `${portalApiPath}/${projectId}${url}`,
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
