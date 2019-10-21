import React from 'react';

const DEFAULT_APP_STATE = {
  projectId: '1',
  tenant: { id: '00000-00000-00001', name: 'demo' },
  importStatus: {
    running: false,
  },
  leverStatus: {
    running: false,
  },
};

const AppContext = React.createContext(DEFAULT_APP_STATE);

export const defaultAppState = DEFAULT_APP_STATE;
export const AppContextProvider = AppContext.Provider;
export default AppContext;
