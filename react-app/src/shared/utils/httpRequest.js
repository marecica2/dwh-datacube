import dwhAxios from './dwhAxios';

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
    ...otherOptions
  } = options;

  return ({ headers = {}, params = {}, data, ...other } = {}) => {
    return dwhAxios({
      url,
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
