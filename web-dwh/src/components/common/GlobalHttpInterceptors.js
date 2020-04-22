import React from 'react';
// import { string, func } from 'prop-types';
import { withRouter } from 'react-router-dom';
// import { connect } from 'react-redux';
// import { bindActionCreators } from 'redux';
import dwhAxios from '../../shared/utils/dwhAxios';
import { CHILDREN, HISTORY, LOCATION } from '../../shared/enums';
import HttpInterceptors from '../../shared/components/HttpInterceptors';
import { loginPath } from '../../settings';
// import { userActions, userSelectors } from '../store/user';
// import renewToken from '../utils/renewToken';
import { AppContext } from '../context/AppContext';

class GlobalHttpInterceptors extends React.Component {
  static contextType = AppContext;

  redirectToLoginScreen = () => {
    const { history, location } = this.props;
    window.location.href = `${loginPath}?redirectUri=${encodeURIComponent(location.pathname)}`;
  };

  redirectToErrorScreen = () => {
    // const { history } = this.props;
  };

  getHeaders = () => {
    const { state } = this.context;
    const { token, tenant } = state;
    const headers = {};
    if (token) {
      headers.Authorization = `Bearer ${token.access_token}`;
    }
    if (token) {
      headers['x-tenant'] = tenant.id;
    }
    return headers;
  };

  requestIntercepted = () => {
    // const { token, setToken } = this.props;
    // renewToken(token, setToken);
  };

  render() {
    const { children } = this.props;

    return (
      <HttpInterceptors
        axios={dwhAxios}
        headers={this.getHeaders}
        requestIntercepted={this.requestIntercepted}
        error401={this.redirectToLoginScreen}
        error400s={this.redirectToErrorScreen}
        error404={this.redirectToErrorScreen}
        error500s={this.redirectToErrorScreen}
      >
        {children}
      </HttpInterceptors>
    );
  }
}

GlobalHttpInterceptors.propTypes = {
  children: CHILDREN.isRequired,
  history: HISTORY.isRequired,
  location: LOCATION.isRequired,
  // tenant: string.isRequired,
  // token: string.isRequired,
  // setToken: func.isRequired,
};

// const mapStateToProps = state => ({
//   token: userSelectors.token(state),
//   tenant: userSelectors.tenant(state),
// });
//
// const mapDispatchToProps = dispatch =>
//   bindActionCreators(
//     {
//       setToken: userActions.setToken,
//     },
//     dispatch,
//   );
//
// export default connect(
//   mapStateToProps,
//   mapDispatchToProps,
// )(withRouter(GlobalXRayHttpInterceptors));

export default withRouter(GlobalHttpInterceptors);
