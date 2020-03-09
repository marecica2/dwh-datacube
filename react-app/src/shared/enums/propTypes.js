import PropTypes from 'prop-types';

export const CHILDREN = PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.node), PropTypes.node]);

export const LOCATION = PropTypes.shape({
  pathname: PropTypes.string.isRequired,
  search: PropTypes.string.isRequired,
  hash: PropTypes.string.isRequired,
});

export const HISTORY = PropTypes.shape({
  action: PropTypes.string.isRequired,
  block: PropTypes.func.isRequired,
  createHref: PropTypes.func.isRequired,
  go: PropTypes.func.isRequired,
  goBack: PropTypes.func.isRequired,
  goForward: PropTypes.func.isRequired,
  listen: PropTypes.func.isRequired,
  length: PropTypes.number.isRequired,
  push: PropTypes.func.isRequired,
  replace: PropTypes.func.isRequired,
  location: LOCATION.isRequired,
});

export const MATCH = PropTypes.shape({
  isExact: PropTypes.bool,
  params: PropTypes.object.isRequired,
  path: PropTypes.string.isRequired,
  url: PropTypes.string.isRequired,
});

// TODO: extend this one properly
export const PROJECT = PropTypes.shape({
  id: PropTypes.number.isRequired,
});
