import React, { useEffect, useState, useReducer } from 'react';
import AppStateApi from '../../shared/api/appState.api';

const INITIAL_APP_STATE = {
  user: localStorage.user,
  project: localStorage.project ? JSON.parse(localStorage.project) : { id: '1', name: 'Sample proj 1' },
  tenant: localStorage.tenant ? JSON.parse(localStorage.tenant) : { id: '000000-00000-00001', name: 'demo' },
  importStatus: {
    running: false,
  },
  progresses: {},
};

localStorage.project = JSON.stringify(INITIAL_APP_STATE.project);
localStorage.tenant = JSON.stringify(INITIAL_APP_STATE.tenant);

const reducer = (state, { type, value }) => {
  switch (type) {
    case 'login':
      return { ...state, user: value };
    case 'logout':
      return { ...state, user: undefined };
    case 'project': {
      localStorage.project = JSON.stringify(value);
      window.location.href = '/';
      return null;
    }
    case 'tenant':
      localStorage.tenant = JSON.stringify(value);
      window.location.href = '/';
      return null;
    case 'importStatus':
      return { ...state, importStatus: { running: value.running } };
    case 'importStatusFile': {
      const newProgress = { ...state.progresses, [value.fileName]: { ...value } };
      return { ...state, importStatus: { running: value.running }, progresses: { ...newProgress } };
    }
    case 'filtersChange':
      return { ...state, filters : {...state.filters, ...value}};
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
