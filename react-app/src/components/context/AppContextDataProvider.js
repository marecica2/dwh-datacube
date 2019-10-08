import React, { useEffect } from 'react';
import AppStateApi from '../../shared/api/appState.api';
import { AppContextProvider, defaultAppState } from './AppContext';

export default function AppContextDataProvider(props) {
  const [eventSource, setEventSource] = React.useState();
  const [appState, setAppState] = React.useState(defaultAppState);

  useEffect(() => {
    async function api() {
      if (eventSource == null) {
        const es = new EventSource(AppStateApi.getAppStateUrl());
        es.onmessage = (event) => {
          const data = JSON.parse(event.data);
          const appContext = { ...defaultAppState, jobs: { importStatus: data.importStatus || { running: false } } };
          setAppState(appContext);
        };
        setEventSource(es);
      }
    }
    api();
    return () => {
      if (eventSource) {
        eventSource.close();
      }
    }
  }, [eventSource, setEventSource]);
  return (
    <AppContextProvider value={appState}>
      {props.children}
    </AppContextProvider>
  );
}