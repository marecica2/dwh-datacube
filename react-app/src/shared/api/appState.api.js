import { portalApiPath } from '../../settings';

const getAppStateUrl = (tenant, project) => `${portalApiPath}/${tenant}/${project}/status`;

export default {
  getAppStateUrl,
};
