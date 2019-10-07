import React, {useEffect} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import {
    Stepper,
    Step,
    StepLabel,
    Button, Paper,
} from '@material-ui/core';
import ImportApi from './ImportApi';
import FileUploadStep, {stepName as fileUploadStepDescription} from './FileUploadStep';
import MappingStep, {stepName as mappingStepDescription} from './MappingStep';
import PreviewStep, {stepName as previewStepDescription} from './PreviewStep';
import ConfigStep, {stepName as configStepDescription} from './ConfigStep';
import ImportProgressStep from './ImportProgressStep';

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
    const [eventSource, setEventSource] = React.useState();
    const [activeStep, setActiveStep] = React.useState(0);
    const [importStatus, setImportStatus] = React.useState({running: false});
    const [transaction, setTransaction] = React.useState();
    const [files, setFiles] = React.useState({});
    const [uploadInProgress, setUploadInProgress] = React.useState(false);
    const [mappingConfig, setMappingConfig] = React.useState();
    const [mapping, setMapping] = React.useState({});
    const [assignedColumns, setAssignedColumns] = React.useState([]);
    const [preview, setPreview] = React.useState();
    const [config, setConfig] = React.useState({skipStrategy: 'cell', deleteStrategy: true});
    const globalState = {
        activeStep,
        setActiveStep,
        transaction,
        setTransaction,
        importStatus,
        setImportStatus,
        files,
        setFiles,
        uploadInProgress,
        setUploadInProgress,
        mappingConfig,
        setMappingConfig,
        mapping,
        setMapping,
        assignedColumns,
        setAssignedColumns,
        preview,
        setPreview,
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
                return Object.keys(files).length > 0 && !uploadInProgress;
            case 1:
                return mapping != null;
            default:
                return true;
        }
    };

    useEffect(() => {
        async function fetch() {
            setTransaction(await ImportApi.initImport());
        }
        fetch();
    }, [setTransaction]);

    useEffect(() => {
        async function fn() {
            if(importStatus && importStatus.running) {
                setActiveStep(0);
                setFiles({});
                setMappingConfig();
                setMapping({});
                setPreview();
            }
        }
        fn();
    }, [importStatus, setActiveStep, setFiles, setMappingConfig, setMapping, setPreview]);

    useEffect(() => {
        async function api() {
            if (transaction && eventSource == null) {
                const es = new EventSource(`/api/import/${transaction}/status`);
                es.onmessage = (event) => {
                    const data = JSON.parse(event.data);
                    setImportStatus(data.importStatus);
                };
                setEventSource(es);
            }
        }
        api();
        return () => {
            if (eventSource) {
                eventSource.close();
            }
        }
    }, [transaction, setImportStatus, eventSource, setEventSource]);

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
          {(activeStep === steps.length || importStatus.running) ?
            <ImportProgressStep {...globalState} /> : (
              <div>
                {getStepContent(activeStep)}
                <div style={{marginTop: '15px'}}>
                  <div style={{float: 'left'}}>
                    <Button
                      disabled={activeStep === 0} onClick={handleBack} className={classes.button}
                      style={{flex: 0}}
                    >
                    Back
                    </Button>
                    <Button
                      disabled={!isNextEnabled(activeStep)} variant="contained" color="primary"
                      onClick={handleNext}
                      className={classes.button}
                      style={{flex: 0}}
                    >
                      {activeStep === steps.length - 1 ? 'Finish' : 'Next'}
                    </Button>
                  </div>
                  <div style={{float: 'right'}}>
                    <Button
                      onClick={handleReset} className={classes.button}
                      style={{flex: 2}}
                    >
                    Cancel
                    </Button>
                  </div>
                </div>
                <div style={{clear: 'both'}} />
              </div>
        )}
        </Paper>
      </div>
    );
}