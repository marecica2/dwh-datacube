import clsx from 'clsx';
import {makeStyles} from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Badge from '@material-ui/core/Badge';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';
import { IconButton, Button } from '@material-ui/core/';
import AccountCircle from '@material-ui/icons/AccountCircle';
import MailIcon from '@material-ui/icons/Mail';
import MenuIcon from '@material-ui/icons/Menu';
import Typography from '@material-ui/core/Typography';
import React from 'react';
import {NavLink as Link} from 'react-router-dom';

const drawerWidth = 240;

const useStyles = makeStyles(theme => ({
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
    textDecoration: 'none',
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  hide: {
    display: 'none',
  },
}));

function AppMenu(props) {
  const classes = useStyles();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);

  function handleMenu(event) {
    setAnchorEl(event.currentTarget);
  }

  function handleClose() {
    setAnchorEl(null);
  }

  return (
    <AppBar
      position="fixed"
      className={clsx(classes.appBar, {
            [classes.appBarShift]: props.open,
          })}
    >
      <Toolbar>
        <IconButton
          color="inherit"
          aria-label="open drawer"
          onClick={props.menuIconClick}
          edge="start"
          className={clsx(classes.menuButton, {
                [classes.hide]: props.open,
              })}
        >
          <MenuIcon />
        </IconButton>


        <Typography
          variant="h5"
          className={clsx(classes.title)}
          color="inherit"
          to='/'
          component={prps => <Link {...prps} />}
        >
          DataCube
        </Typography>

        <IconButton
          color="inherit"
          onClick={props.menuIconClick}
        >
          <Badge className={classes.margin} badgeContent={4} color="error">
            <MailIcon />
          </Badge>
        </IconButton>
        <IconButton
          className={classes.button}
          aria-controls="menu-appbar"
          aria-haspopup="true"
          onClick={handleMenu}
          color="inherit"
        >
          <AccountCircle
            className={clsx(classes.leftIcon, classes.iconSmall)}
          />
        </IconButton>
        <Menu
          id="menu-appbar"
          anchorEl={anchorEl}
          anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
          keepMounted
          transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
          open={open}
          onClose={handleClose}
        >
          <MenuItem onClick={handleClose}>User preferences</MenuItem>
          <MenuItem onClick={handleClose}>Sign out</MenuItem>
        </Menu>
      </Toolbar>
    </AppBar>
  );
}

export default AppMenu;