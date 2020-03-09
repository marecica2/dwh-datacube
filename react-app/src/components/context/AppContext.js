import React, { useEffect, useState, useReducer } from 'react';
import AppStateApi from '../../shared/api/appState.api';

const INITIAL_APP_STATE = {
  user: null,
  token: sessionStorage.token ? JSON.parse(sessionStorage.token) : null,
  project: sessionStorage.project ? JSON.parse(sessionStorage.project) : { id: '1', name: 'Sample proj 1' },
  tenant: sessionStorage.tenant ? JSON.parse(sessionStorage.tenant) : { id: '000000-00000-00001', name: 'demo' },
  importStatus: {
    running: false,
  },
  progresses: {},
};

sessionStorage.project = JSON.stringify(INITIAL_APP_STATE.project);
sessionStorage.tenant = JSON.stringify(INITIAL_APP_STATE.tenant);

const reducer = (state, { type, value }) => {
  switch (type) {
    case 'loginSuccess':
      console.log(type);
      console.log(value);
      sessionStorage.setItem('token', JSON.stringify(value.token));
      window.location.href = value.redirect;
      return { ...state, user: 'admin', token: value.token };
    case 'logout':
      window.location.href = '/login';
      return { ...state, user: null };
    case 'project': {
      sessionStorage.project = JSON.stringify(value);
      window.location.href = '/';
      return null;
    }
    case 'tenant':
      sessionStorage.tenant = JSON.stringify(value);
      window.location.href = '/';
      return null;
    case 'importStatus':
      return { ...state, importStatus: { running: value.running } };
    case 'importStatusFile': {
      const newProgress = { ...state.progresses, [value.fileName]: { ...value } };
      return { ...state, importStatus: { running: value.running }, progresses: { ...newProgress } };
    }
    case 'filtersChange':
      return { ...state, filters: { ...state.filters, ...value } };
    default: {
      console.warn('AppContext No action registered for ', type);
      return state;
    }
  }
};

const AppContext = React.createContext(INITIAL_APP_STATE);

function AppStateProvider(props) {
  const [state, dispatch] = useReducer(reducer, INITIAL_APP_STATE);
  const [eventSource, setEventSource] = useState();

  useEffect(() => {
    async function api() {
      if (eventSource && eventSource.readyState !== window.EventSource.CLOSED) {
        eventSource.close();
      }
      const es = new window.EventSource(AppStateApi.getAppStateUrl(state.tenant.id, state.project.id));
      es.onmessage = (event) => {
        if (event.data !== 'heartbeat') {
          const data = JSON.parse(event.data);
          const action = { type: data.type, value: data };
          dispatch(action);
        }
      };
      setEventSource(es);
    }

    api();
    return () => {
      if (eventSource) {
        eventSource.close();
      }
    }
  }, [setEventSource, state.tenant, state.project]);


  return (
    // new
    <AppContext.Provider value={{ state, dispatch }}>
      {props.children}
    </AppContext.Provider>
  );
}

export { AppContext, AppStateProvider };
export default AppContext;
