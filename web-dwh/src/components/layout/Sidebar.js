import React, { useContext } from 'react';
import clsx from 'clsx';
import {makeStyles, useTheme} from '@material-ui/core/styles';
import Collapse from '@material-ui/core/Collapse';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import LocalShipping from '@material-ui/icons/LocalShipping';
import AssessmentIcon from '@material-ui/icons/Assessment';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import SettingsIcon from '@material-ui/icons/Settings';
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import {NavLink as RouterLink} from 'react-router-dom';
import { AppContext } from '../context/AppContext';

const drawerWidth = 240;

const useStyles = makeStyles(theme => ({
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
    whiteSpace: 'nowrap',
  },
  drawerOpen: {
    width: drawerWidth,
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerClose: {
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: theme.spacing(7) + 1,
    [theme.breakpoints.up('sm')]: {
      width: theme.spacing(9) + 1,
    },
  },
  toolbar: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    ...theme.mixins.toolbar,
  },
  link: {
    textDecoration: 'none',
  },
}));

function Sidebar(props) {
  const classes = useStyles();
  const theme = useTheme();
  const [categoryOpen, setCategoryOpen] = React.useState(false);
  const AdapterLink = React.forwardRef((props, ref) => <RouterLink innerRef={ref} {...props} />);

  const { state } = useContext(AppContext);

  return state.token && (
    <Drawer
      variant="permanent"
      className={clsx(classes.drawer, {
            [classes.drawerOpen]: props.isSidebarOpen,
            [classes.drawerClose]: !props.isSidebarOpen,
          })}
      classes={{
            paper: clsx({
              [classes.drawerOpen]: props.isSidebarOpen,
              [classes.drawerClose]: !props.isSidebarOpen,
            }),
          }}
      open={props.isSidebarOpen}
    >
      <div className={classes.toolbar}>
        <IconButton onClick={props.handleDrawerClose}>
          {theme.direction === 'rtl' ? <ChevronRightIcon /> :
          <ChevronLeftIcon />}
        </IconButton>
      </div>
      <Divider />
      <List>
        <ListItem button onClick={() => setCategoryOpen(!categoryOpen)}>
          <ListItemText primary='Categories' />
          {categoryOpen ? <ExpandLess /> : <ExpandMore />}
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
              <ListItemIcon><LocalShipping className={classes.iconHover} /></ListItemIcon>
              <ListItemText primary='Supplier' />
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
              <ListItemIcon><AssessmentIcon /></ListItemIcon>
              <ListItemText primary='View Data' />
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
          <ListItemIcon><CloudUploadIcon /></ListItemIcon>
          <ListItemText primary='Import' />
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
          <ListItemIcon><SettingsIcon /></ListItemIcon>
          <ListItemText primary='Settings' />
        </ListItem>
      </List>
    </Drawer>
  );
}

export default Sidebar;
