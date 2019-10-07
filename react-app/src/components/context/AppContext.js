import React from 'react';

const DEFAULT_APP_STATE = {
  user: {
    email: 'john.doe@gmail.com',
    role: 'user',
  },
  gui: {
    sidebarExpanded: true,
  },
  data: {
    count: 0,
  },
  jobs: {
    importStatus: {
      running: false,
    },
    leverStatus: {
      running: false,
    },
  },
};

const AppContext = React.createContext(DEFAULT_APP_STATE);

export const defaultAppState = DEFAULT_APP_STATE;
export const AppContextProvider = AppContext.Provider;
export const AppContextConsumer = AppContext.Consumer;
export default AppContext;
