import React, { useEffect } from 'react';
import AppStateApi from '../../shared/api/appState.api';

export default function AppContextListener() {
  const [eventSource, setEventSource] = React.useState();
  const [appState, setAppState] = React.useState();

  useEffect(() => {
    async function api() {
      if (eventSource == null) {
        const es = new EventSource(AppStateApi.getAppStateUrl());
        es.onmessage = (event) => {
          const data = JSON.parse(event.data);
          setAppState(data.importStatus);
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

  console.log(appState);
  return appState;
}