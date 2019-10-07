export const apiUrl = '/api';
export const apiHost = 'http://localhost:8081';
export const pathPrefix = process.env.REACT_APP_PATH_PREFIX;
export const portalApiPath = apiHost + apiUrl;

export const loginPath = `${pathPrefix}/login`;
export const logoutPath = `${pathPrefix}/logout`;
export const errorPath = `${pathPrefix}/error`;