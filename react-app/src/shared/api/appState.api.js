import { portalApiPath } from '../../settings';

const getAppStateUrl = () => `${portalApiPath}/status`;

export default {
  getAppStateUrl,
};
