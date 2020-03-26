import React from 'react';
import PropTypes from 'prop-types';
import { isFunction, isBoolean } from 'ramda-adjunct';

const getResponseError = (response) => {
  if (response) {
    const { data: { message = null } = {} } = response;
    return {
      status: response.status,
      message: message !== null ? message : response.statusText,
    };
  } else {
    return {
      status: 500,
      message: 'Unknown error',
    }
  }
};

class HttpInterceptors extends React.Component {
  requestInterceptor = null;

  responseInterceptor = null;

  constructor(props) {
    super(props);

    const { axios, error401, error404, error400s, error500s, requestIntercepted, responseIntercepted } = this.props;

    this.requestInterceptor = axios.interceptors.request.use((config) => {
      requestIntercepted(config);
      const { headers: headersWrapper = {} } = this.props;
      const headers = isFunction(headersWrapper) ? headersWrapper() : headersWrapper;

      /* eslint-disable no-param-reassign */
      config.headers = {
        ...config.headers,
        ...headers,
      };
      /* eslint-enable no-param-reassign */

      return config;
    });

    this.responseInterceptor = axios.interceptors.response.use(
      response => response,
      (error) => {
        responseIntercepted();
        console.log(error);
        const { response, config: { errorHandlers } = {} } = error;
        const responseError = getResponseError(response);

        if (response.status === 401) {
          error401(responseError);
          return response;
        }

        if (isBoolean(errorHandlers) && errorHandlers === true) {
          return response;
        }

        if (isFunction(errorHandlers)) {
          if (errorHandlers(response)) {
            return response;
          }
        } else if (Array.isArray(errorHandlers)) {
          if (errorHandlers.find(status => response.status === status)) {
            return response;
          }
        }

        if (response.status === 404) {
          error404(responseError);
        } else if (response.status >= 400 && response.status < 500) {
          error400s(responseError);
        }

        if (response.status >= 500 && response.status < 600) {
          error500s(responseError);
        }

        return response;
      },
    );
  }

  componentWillUnmount() {
    const { axios } = this.props;
    axios.interceptors.request.eject(this.requestInterceptor);
    axios.interceptors.response.eject(this.responseInterceptor);
  }

  render() {
    const { children } = this.props;
    return children;
  }
}

HttpInterceptors.propTypes = {
  axios: PropTypes.oneOfType([PropTypes.shape({}), PropTypes.func]).isRequired,
  children: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.node), PropTypes.node]).isRequired,
  headers: PropTypes.oneOfType([PropTypes.shape({}), PropTypes.func]),
  requestIntercepted: PropTypes.func,
  responseIntercepted: PropTypes.func,
  error401: PropTypes.func,
  error404: PropTypes.func,
  error400s: PropTypes.func,
  error500s: PropTypes.func,
};

HttpInterceptors.defaultProps = {
  headers: null,
  error401: () => {
  },
  error404: () => {
  },
  error400s: () => {
  },
  error500s: () => {
  },
  requestIntercepted: () => {
  },
  responseIntercepted: () => {
  },
};

export default HttpInterceptors;
