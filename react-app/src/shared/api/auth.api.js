import { httpRequest } from '../utils';

const baseUrl = '/security';

export default {
  login: async (data, options) => {
    const { data: resp } = await httpRequest(`${baseUrl}/oauth/token`, {
      data,
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
        'Authorization': `Basic ${btoa('dwh-client:secret')}`,
      },
      tenantRequest: false,
      ...options,
    })();
    return resp;
  },
  loggedUser: async (options) => {
    const { data: resp } = await httpRequest(`${baseUrl}/me`, {
      method: 'GET',
      tenantRequest: false,
      ...options,
    })();
    return resp;
  },
};
