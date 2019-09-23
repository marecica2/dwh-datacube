import React from 'react';
import PropTypes from 'prop-types';
import IconButton from '@material-ui/core/IconButton';
import Tooltip from '@material-ui/core/Tooltip';
import Zoom from '@material-ui/core/Zoom';
import { withStyles } from '@material-ui/core/styles/index';
import grey from '@material-ui/core/colors/grey';

const styles = () => ({
  icon: {
    color: grey[600],
  },
});

const ToolbarIconButton = ({
  classes,
  tooltip,
  disabledTooltip,
  disabled,
  onClick,
  icon,
  transitionDelay,
  timeout,
  buttonRef,
}) => (
  <Tooltip TransitionComponent={Zoom} title={disabled ? disabledTooltip : tooltip}>
    <span>
      <Zoom in style={{ transitionDelay }} timeout={timeout}>
        <IconButton buttonRef={buttonRef} className={classes.icon} onClick={onClick} disabled={disabled}>
          {icon}
        </IconButton>
      </Zoom>
    </span>
  </Tooltip>
);

ToolbarIconButton.propTypes = {
  classes: PropTypes.shape({}).isRequired,
  icon: PropTypes.shape({}).isRequired,
  tooltip: PropTypes.string,
  disabled: PropTypes.bool,
  disabledTooltip: PropTypes.string,
  onClick: PropTypes.func,
  transitionDelay: PropTypes.number,
  timeout: PropTypes.number,
  buttonRef: PropTypes.func,
};

ToolbarIconButton.defaultProps = {
  tooltip: '',
  disabledTooltip: '',
  disabled: false,
  onClick: () => {},
  transitionDelay: 0,
  timeout: 500,
  buttonRef: undefined,
};

export default withStyles(styles)(ToolbarIconButton);
