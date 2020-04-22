import React, { useEffect } from 'react';
import { NavLink as RouterLink, Route, Router, Switch } from "react-router-dom";
import { makeStyles, useTheme } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import CssBaseline from '@material-ui/core/CssBaseline';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ExpandLess from "@material-ui/icons/ExpandLess";
import ExpandMore from "@material-ui/icons/ExpandMore";
import Collapse from "@material-ui/core/Collapse";
import LocalShipping from "@material-ui/icons/LocalShipping";
import AssessmentIcon from "@material-ui/icons/Assessment";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import SettingsIcon from "@material-ui/icons/Settings";

import Menu from './layout/Menu';
import Home from './pages/Home';
import Login from './pages/Login';
import Settings from './pages/Settings';
import Supplier from './pages/categories/Supplier';
import DataPreview from './pages/categories/DataPreview';
import Import from './pages/Import';

import history from './history';
import { AppStateProvider } from './context/AppContext';
import GlobalHttpInterptors from './common/GlobalHttpInterceptors';
import Loader from './common/Loader';
import authApi from '../shared/api/auth.api';

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
    },
    drawer: {
        width: drawerWidth,
        flexShrink: 0,
    },
    drawerPaper: {
        width: drawerWidth,
    },
    drawerContainer: {
        overflow: 'auto',
    },
    content: {
        flexGrow: 1,
        padding: theme.spacing(2),
    },
}));

export default function App() {
    const theme = useTheme();
    const classes = useStyles(theme);

    const [isSidebarOpen, setSidebarOpen] = React.useState(true);
    const [user, setUser] = React.useState(null);

    const [categoryOpen, setCategoryOpen] = React.useState(false);
    const AdapterLink = React.forwardRef((props, ref) => <RouterLink innerRef={ref} {...props} />);

    useEffect(() => {
        const api = async () => {
            const loggedUser = await authApi.loggedUser();
            setUser(loggedUser);
        };
        api();
    }, [setUser]);

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
                        {user ? (
                            <>
                                <Menu open={isSidebarOpen} menuIconClick={handleDrawerOpen}/>
                                <Drawer
                                    className={classes.drawer}
                                    variant="permanent"
                                    classes={{
                                        paper: classes.drawerPaper,
                                    }}
                                >
                                    <Toolbar/>
                                    <div className={classes.drawerContainer}>
                                        <List>
                                            <ListItem button onClick={() => setCategoryOpen(!categoryOpen)}>
                                                <ListItemText primary='Categories'/>
                                                {categoryOpen ? <ExpandLess/> : <ExpandMore/>}
                                            </ListItem>
                                            <Collapse in={categoryOpen} timeout="auto" unmountOnExit>
                                                <List component="div" disablePadding>
                                                    <ListItem
                                                        button
                                                        key='Categories'
                                                        className={classes.nested}
                                                        to='/category/supplier'
                                                        activeStyle={{
                                                            color: theme.palette.primary.main,
                                                        }}
                                                        component={AdapterLink}
                                                    >
                                                        <ListItemIcon><LocalShipping
                                                            className={classes.iconHover}/></ListItemIcon>
                                                        <ListItemText primary='Supplier'/>
                                                    </ListItem>
                                                    <ListItem
                                                        button
                                                        key='Data View'
                                                        className={classes.nested}
                                                        to='/category/service-type'
                                                        activeStyle={{
                                                            color: theme.palette.primary.main,
                                                        }}
                                                        component={AdapterLink}
                                                    >
                                                        <ListItemIcon><AssessmentIcon/></ListItemIcon>
                                                        <ListItemText primary='View Data'/>
                                                    </ListItem>
                                                </List>
                                            </Collapse>
                                        </List>

                                        <List>
                                            <ListItem
                                                button
                                                key='Import'
                                                to='/import'
                                                activeStyle={{
                                                    color: theme.palette.primary.main,
                                                }}
                                                component={AdapterLink}
                                            >
                                                <ListItemIcon><CloudUploadIcon/></ListItemIcon>
                                                <ListItemText primary='Import'/>
                                            </ListItem>

                                            <ListItem
                                                button
                                                key='Settings'
                                                to='/settings'
                                                activeStyle={{
                                                    color: theme.palette.primary.main,
                                                }}
                                                component={AdapterLink}
                                            >
                                                <ListItemIcon><SettingsIcon/></ListItemIcon>
                                                <ListItemText primary='Settings'/>
                                            </ListItem>
                                        </List>
                                    </div>
                                </Drawer>
                                <main className={classes.content}>
                                    <Toolbar/>
                                    <div>
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
                                    </div>
                                </main>
                            </>
                        ) : (
                            <>
                                <Loader/>
                            </>
                        )}
                    </GlobalHttpInterptors>
                </AppStateProvider>
            </Router>
        </div>
    );
}
