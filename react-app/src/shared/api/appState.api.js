import { portalApiPath } from '../../settings';

const getAppStateUrl = (tenant, project) => `${portalApiPath}/status?tenant=${tenant}&projectId=${project}`;

export default {
  getAppStateUrl,
};
