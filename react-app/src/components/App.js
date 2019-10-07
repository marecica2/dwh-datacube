import React from 'react';
import { Router, Route, Switch } from 'react-router-dom';
import {makeStyles, useTheme} from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';

import history from './history';
import Menu from './layout/Menu';
import Sidebar from './layout/Sidebar';
import Home from './pages/Home';
import Supplier from './pages/categories/Supplier';
import ServiceType from './pages/categories/ServiceType';
import Import from './pages/Import';
import { AppContextProvider, defaultAppState } from './context/AppContext';
import AppContextListener from './context/AppContextListener';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
  },
  content: {
    flexGrow: 1,
    paddingTop: theme.spacing(11),
    padding: theme.spacing(3),
  },
}));

function App() {
  const theme = useTheme();
  const classes = useStyles(theme);
  const [isSidebarOpen, setSidebarOpen] = React.useState(true);
  const appContext = { ...defaultAppState, jobs: { importStatus: AppContextListener() || { running: false } } };

  function handleDrawerOpen() {
    setSidebarOpen(true);
  }

  function handleDrawerClose() {
    setSidebarOpen(false);
  }

  return (
    <div className={classes.root}>
      <AppContextProvider value={appContext}>
        <CssBaseline />
        <Router history={history}>
          <Menu open={isSidebarOpen} menuIconClick={handleDrawerOpen} />
          <Sidebar handleDrawerClose={handleDrawerClose} isSidebarOpen={isSidebarOpen} />
          <main className={classes.content}>
            <Switch>
              <Route path="/category/supplier" component={Supplier} />
              <Route path="/category/service-type" component={ServiceType} />
              <Route path="/import" component={Import} />
              <Route path="/" strict component={Home} />
            </Switch>
          </main>
        </Router>
      </AppContextProvider>
    </div>
  );
}

export default App;
