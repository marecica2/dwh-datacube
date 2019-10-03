import React, { useEffect } from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {
  Stepper,
  Step,
  StepLabel,
  Button, Paper,
} from '@material-ui/core';
import ImportApi from './ImportApi';
import FileUploadStep, { stepName as fileUploadStepDescription } from './FileUploadStep';
import MappingStep, { stepName as mappingStepDescription } from './MappingStep';
import PreviewStep, { stepName as previewStepDescription } from './PreviewStep';
import ConfigStep, { stepName as configStepDescription } from './ConfigStep';
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

export default function Import() {
  const classes = useStyles();
  const steps = [fileUploadStepDescription, mappingStepDescription, previewStepDescription, configStepDescription];
  const [activeStep, setActiveStep] = React.useState(0);
  const [transaction, setTransaction] = React.useState();
  const [files, setFiles] = React.useState([]);
  const [uploadedFiles, setUploadedFiles] = React.useState([]);
  const [mapping, setMapping] = React.useState();
  const [config, setConfig] = React.useState({ skipStrategy: 'cell', deleteStrategy: true });
  const globalState =  {
    transaction,
    setTransaction,
    files,
    setFiles,
    uploadedFiles,
    setUploadedFiles,
    mapping,
    setMapping,
    config,
    setConfig,
  };

  const getStepContent = (step) => {
    switch (step) {
      case 0:
        return <FileUploadStep {...globalState} />;
      case 1:
        return <MappingStep {...globalState} />;
      case 2:
        return <PreviewStep {...globalState} />;
      case 3:
        return <ConfigStep {...globalState} />;
      default:
        return '';
    }
  };

  const isNextEnabled = (step) => {
    switch (step) {
      case 0:
        return files.length > 0;
      case 1:
        return mapping != null;
      default:
        return true;
    }
  };

  useEffect(() => {
    async function api() {
      setTransaction(await ImportApi.initImport());
    }
    api();
  }, []);

  const handleNext = () => {
    setActiveStep(prevActiveStep => prevActiveStep + 1);
  };

  const handleBack = () => {
    setActiveStep(prevActiveStep => prevActiveStep - 1);
  };

  const handleReset = () => {
    setActiveStep(0);
  };

  return (
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
        {activeStep === steps.length ? <FinishStep /> : getStepContent(activeStep)}
        {activeStep === steps.length ? (
          <p>
            <Button onClick={handleReset} className={classes.button}>
                    Cancel
            </Button>
          </p>
            ) : (
              <p>
                <Button disabled={activeStep === 0} onClick={handleBack} className={classes.button}>
                Back
                </Button>
                <Button disabled={!isNextEnabled(activeStep)} variant="contained" color="primary" onClick={handleNext} className={classes.button}>
                  {activeStep === steps.length - 1 ? 'Finish' : 'Next'}
                </Button>
              </p>
        )}
      </Paper>
    </div>
  );
}