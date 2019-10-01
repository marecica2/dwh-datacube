import React, { useEffect } from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {
  Stepper,
  Step,
  StepLabel,
  Button, Paper,
} from '@material-ui/core';
import {Provider} from './ImportContext';
import ImportApi from './ImportApi';
import FileUploadStep from './FileUploadStep';
import MappingStep from './MappingStep';
import PreviewStep from './PreviewStep';
import ConfigStep from './ConfigStep';
import FinishStep from './FinishStep';

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
  },
  paper: {
    padding: theme.spacing(2),
    textAlign: 'left',
    color: theme.palette.text.secondary,
  },
  button: {
    marginRight: theme.spacing(1),
  },
  instructions: {
    marginTop: theme.spacing(1),
    marginBottom: theme.spacing(1),
  },
}));

function getStepContent(step) {
  switch (step) {
    case 0:
      return <FileUploadStep />;
    case 1:
      return <MappingStep />;
    case 2:
      return <PreviewStep />;
    case 3:
      return <ConfigStep />;
    default:
      return '';
  }
}

export default function Import() {
  const classes = useStyles();
  const steps = ['Get the data', 'Column mapping', 'Data preview', 'Options'];
  const [activeStep, setActiveStep] = React.useState(0);
  const [importState, setImportState] = React.useState({
    actions: {
      setProp: (prop) => {
        setImportState(state => ({...state, ...prop }));
      },
      setFiles: (files) => {
        setImportState(state => ({...state, files }));
      },
    },
    transaction: null,
    files: [],
    mapping: {},
    config: {
      skipStrategy: 'cell',
      deleteStrategy: true,
    },
  });

  useEffect(() => {
    async function fetchData() {
      const tx = await ImportApi.initImport();
      setImportState(state => ({...state, transaction: tx }));
    }
    fetchData();
  }, []);

  const handleNext = () => {
    console.log(importState);
    setActiveStep(prevActiveStep => prevActiveStep + 1);
  };

  const handleBack = () => {
    setActiveStep(prevActiveStep => prevActiveStep - 1);
  };

  const handleReset = () => {
    setActiveStep(0);
  };

  return (
    <Provider value={importState}>
      <div className={classes.root}>
        <Paper className={classes.paper}>
          <Stepper activeStep={activeStep}>
            {steps.map((label) => {
                const stepProps = {};
                const labelProps = {};
                return (
                  <Step key={label} {...stepProps}>
                    <StepLabel {...labelProps}>{label}</StepLabel>
                  </Step>
                );
              })}
          </Stepper>
          {activeStep === steps.length ? (
            <div>
              <FinishStep />
              <Button onClick={handleReset} className={classes.button}>
                    Cancel
              </Button>
            </div>
            ) : (
              <div>
                {getStepContent(activeStep)}
                <Button
                  disabled={activeStep === 0}
                  onClick={handleBack}
                  className={classes.button}
                >
                    Back
                </Button>
                <Button
                  variant="contained"
                  color="primary"
                  onClick={handleNext}
                  className={classes.button}
                >
                  {activeStep === steps.length - 1 ? 'Finish' : 'Next'}
                </Button>
              </div>
            )}
        </Paper>
      </div>
    </Provider>
  );
}