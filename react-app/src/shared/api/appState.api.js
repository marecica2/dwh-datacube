import { portalApiPath } from '../../settings';

const getAppStateUrl = (tenant, project) => `${portalApiPath}/importer/status?tenant=${tenant}&projectId=${project}`;

export default {
  getAppStateUrl,
};
