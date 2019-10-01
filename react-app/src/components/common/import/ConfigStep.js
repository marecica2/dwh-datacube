import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {
  Checkbox,
  Radio,
  RadioGroup,
  FormControlLabel,
  FormGroup,
  FormLabel,
} from '@material-ui/core';
import {contextWrapper} from './ImportContext';

const useStyles = makeStyles(theme => ({
  formControl: {
    margin: theme.spacing(3),
  },
}));

const ConfigStep = ({ actions, skipStrategy, deleteStrategy }) => {
  const classes = useStyles();

  const handleInputChange = (event) => {
    const { target } = event;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const { name } = target;
    actions.setProp({
      [name]: value,
    });
  };

  return (
    <div>
      <RadioGroup component="fieldset" className={classes.formControl}>
        <FormLabel component="legend">Error processing</FormLabel>
        <RadioGroup
          aria-label="Error processing"
          name="skipStrategy"
          value={skipStrategy}
          onChange={handleInputChange}
        >
          <FormControlLabel value="row" control={<Radio />} label="Skip row" />
          <FormControlLabel
            value="cell"
            control={<Radio />}
            label="Skip cell"
          />
        </RadioGroup>
      </RadioGroup>
      <div>
      <FormGroup component="fieldset" className={classes.formControl}>
        <FormControlLabel
          control={(
            <Checkbox
              checked={deleteStrategy}
              onChange={handleInputChange}
              name="deleteStrategy"
              color="primary"
            />
              )}
          label="Delete existing data"
        />
      </FormGroup>
      </div>
    </div>
  );
};

export default contextWrapper(ConfigStep);