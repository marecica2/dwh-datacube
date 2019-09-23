export const apiUrl = process.env.REACT_APP_API_URL;
export const apiHost = process.env.REACT_APP_API_HOST;
export const nvtGatewayHost = process.env.REACT_APP_NVT_GATEWAY_HOST;
export const publicUrl = process.env.REACT_APP_PUBLIC_URL;
export const pathPrefix = process.env.REACT_APP_PATH_PREFIX;
export const appWebPathPrefix = process.env.REACT_APP_APP_WEB_PATH_PREFIX;
export const complexityApiHost = process.env.REACT_APP_COMPLEXITY_API_HOST;
export const portalApiPath = process.env.REACT_APP_PORTAL_API_HOST;
export const documentationPrefix = process.env.REACT_APP_DOCUMENTATION_PREFIX;

export const loginPath = `${pathPrefix}/login`;
export const logoutPath = `${pathPrefix}/logout`;
export const errorPath = `${pathPrefix}/error`;

export const enableSentry = process.env.REACT_APP_ENABLE_SENTRY === 'true';
export const sentryPrefix = process.env.REACT_APP_SENTRY_PREFIX;
export const sentryUser = process.env.REACT_APP_SENTRY_USER;
export const sentryProjectID = process.env.REACT_APP_SENTRY_PROJECT_ID;
export const isSentryEnabled = !!sentryPrefix && enableSentry;
export const errorPageEnabled = process.env.REACT_APP_ENABLE_FRONTEND_ERROR_PAGE;

export const isDocumentationEnabled = process.env.REACT_APP_ENABLE_DOCUMENTATION === 'true';