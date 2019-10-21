import React, { useEffect } from 'react';
import AppStateApi from '../../shared/api/appState.api';
import { AppContextProvider, defaultAppState } from './AppContext';

export default function AppContextDataProvider(props) {
  const [eventSource, setEventSource] = React.useState();
  const [appState, setAppState] = React.useState(defaultAppState);
  const [prj, setPrj] = React.useState(defaultAppState.projectId);

  const initLocalStorage = () => {
    sessionStorage.projectId = appState.projectId;
    sessionStorage.tenant = appState.tenant.id;
  };
  initLocalStorage();

  const setProject = (projectId) => {
    sessionStorage.projectId = projectId;
    setAppState(prev => ({ ...prev, projectId }));
    setPrj(projectId);
  };

  const setTenant = (tenant) => {
    sessionStorage.tenant = tenant.id;
    setAppState(prev => ({ ...prev, tenant }));
  };


  useEffect(() => {
    async function api() {
      if(eventSource) {
        eventSource.close();
      }
      const es = new EventSource(AppStateApi.getAppStateUrl(appState.tenant.id, appState.projectId));
      es.onmessage = (event) => {
        if (event.data !== 'heartbeat') {
          const data = JSON.parse(event.data);
          const appContext = data || defaultAppState;
          setAppState(appContext);
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
  }, [prj]);
  return (
    <AppContextProvider value={{ appState, setProject, setTenant }}>
      {props.children}
    </AppContextProvider>
  );
}