import { httpRequest } from '../utils';

const baseUrl = '/charts';

export default ({ ...config }) => {
  return {
    getChart: async ({ dimensions, measures, sorts, ...filters }) => {
      const { data: resp } = await httpRequest(baseUrl, {
        method: 'GET',
        params: { dimensions: dimensions.join(','), measures: measures.join(','), sorts: sorts.join(','), ...filters},
        ...config,
      })();
      return resp;
    },
  }
};
