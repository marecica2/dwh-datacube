import React, { useEffect } from 'react';
import { Router, Route, Switch } from 'react-router-dom';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';

import history from './history';
import Menu from './layout/Menu';
import Sidebar from './layout/Sidebar';
import Home from './pages/Home';
import Login from './pages/Login';
import Settings from './pages/Settings';
import Supplier from './pages/categories/Supplier';
import DataPreview from './pages/categories/DataPreview';
import Import from './pages/Import';
import { AppStateProvider } from './context/AppContext';
import GlobalHttpInterptors from './common/GlobalHttpInterceptors';
import authApi from '../shared/api/auth.api';

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


  useEffect(() => {
    const api = async () => {
      const user = await authApi.loggedUser();
      console.log(user);
    };
    api();
  }, []);

  function handleDrawerOpen() {
    setSidebarOpen(true);
  }

  function handleDrawerClose() {
    setSidebarOpen(false);
  }

  return (
    <div className={classes.root}>
      <CssBaseline/>
      <Router history={history}>
        <AppStateProvider>
          <GlobalHttpInterptors>
            <Menu open={isSidebarOpen} menuIconClick={handleDrawerOpen}/>
            <Sidebar handleDrawerClose={handleDrawerClose} isSidebarOpen={isSidebarOpen}/>
            <main className={classes.content}>
              <Switch>
                <Route path="/category/supplier" component={Supplier}/>
                <Route path="/category/service-type" component={DataPreview}/>
                <Route path="/import" component={Import}/>
                <Route path="/settings" component={Settings}/>
                <Route path="/login" component={Login}/>
                <Route path="/" strict component={Home}/>
              </Switch>
            </main>
          </GlobalHttpInterptors>
        </AppStateProvider>
      </Router>
    </div>
  );
}

export default App;
