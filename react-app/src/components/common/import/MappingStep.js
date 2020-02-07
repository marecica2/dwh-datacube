import React, { useEffect, useCallback } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {
  Button,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  FormControl,
  MenuItem,
  Select,
  TextField,
  Typography,
  InputLabel, FormGroup, Box,
} from '@material-ui/core';
import grey from '@material-ui/core/colors/grey';
import Loader from '../Loader';
import ImportApi from '../../../shared/api/import.api';

const useStyles = makeStyles(theme => ({
  formControl: {
    margin: theme.spacing(1),
    minWidth: 200,
  },
  inlineButton: {
    marginTop: '15px', // TODO how to fix the alignment?
  },
  selectEmpty: {
    marginTop: theme.spacing(2),
  },
  tableWrapper: {
    maxHeight: '50vh',
    overflow: 'auto',
  },
  label: {
    margin: theme.spacing(2),
    color: grey[500],
  },
  head: {
    backgroundColor: 'white',
    position: 'sticky',
    zIndex: '999',
    top: 0,
  },
}));

export const stepName = 'Column mapping';

function MappingStep({
                       transaction, mappingConfig, setMappingConfig, mapping, setMapping, files, setPreview,
                       assignedColumns, setAssignedColumns,
                     }) {
  const classes = useStyles();
  const [loaded, setLoaded] = React.useState(false);
  const [mappingPresets, setMappingPresets] = React.useState([]);
  const [mappingPreset, setMappingPreset] = React.useState('');
  const [mappingPresetName, setMappingPresetName] = React.useState(Object.keys(files)[0]);

  const selectColumn = useCallback(({ name, value }) => {
    const prevValue = mapping[name];
    if (value === '') {
      setAssignedColumns(prev => prev.filter(item => item !== prevValue));
    } else {
      setAssignedColumns(prev => ([...prev, value]));
    }
    setMapping(prev => ({ ...prev, [name]: value }));
    setPreview(null);
  }, [mapping, setMapping, setAssignedColumns, setPreview]);

  const autoSuggestMapping = useCallback((map) => {
    Object.entries(map).forEach(([k, v]) => {
      selectColumn({ name: k, value: v });
    });
  }, [selectColumn]);

  useEffect(() => {
    async function fetchData() {
      if (mappingConfig == null) {
        setLoaded(false);
        const result = await ImportApi.getMapping(transaction, Object.keys(files));
        setMappingConfig(result);
        autoSuggestMapping(result.mapping);
      }
      setLoaded(true);
    }

    fetchData();
  }, [transaction, mappingConfig, autoSuggestMapping,
    setMappingConfig, files, setLoaded, selectColumn, setAssignedColumns]);

  useEffect(() => {
    async function api() {
      const result = await ImportApi.getMappingPresets();
      setMappingPresets(result);
    }

    api();
  }, [setMappingPresets]);

  const selectMapping = useCallback((value) => {
    const [mappingPresetItem] = mappingPresets.filter(item => item.id === value);
    autoSuggestMapping(mappingPresetItem.mapping);
    setMappingPreset(value);
  }, [mappingPresets, setMappingPreset, autoSuggestMapping]);

  const saveMapping = useCallback(() => {
    async function api() {
      await ImportApi.createMappingPreset({ name: mappingPresetName, mapping });
      const result = await ImportApi.getMappingPresets();
      setMappingPresets(result);
    }

    api();
  }, [mapping, mappingPresetName, setMappingPresets]);

  const renderMenuItems = (col, comparator = val => val.required) => {
    return Object.entries(mappingConfig.destinationColumns)
      .filter(([, val2]) => comparator(val2))
      .filter(([col2]) => !assignedColumns.includes(col2)
        || mapping[col] === col2)
      .map(([col2, val2]) => (
        <MenuItem key={col2} value={col2}>
          {val2.label}
        </MenuItem>
      ));
  };

  const renderColumns = () => {
    if (mappingConfig && mappingConfig.sourceColumns) {
      return Object.entries(mappingConfig.sourceColumns).map(([col, val]) => (
        <TableRow key={col}>
          <TableCell>
            {col}
          </TableCell>
          <TableCell>
            {val}
          </TableCell>
          <TableCell>
            <FormControl className={classes.formControl}>
              <Select name={col} value={mapping[col] || ''} onChange={e => selectColumn(e.target)}>
                <MenuItem value="">
                  None
                </MenuItem>
                <Typography variant="caption" display="block" className={classes.label}>
                  Required fields
                </Typography>
                {renderMenuItems(col)}
                <Typography variant="caption" display="block" className={classes.label}>
                  Optional fields
                </Typography>
                {renderMenuItems(col, v => v.required == null)}
              </Select>
            </FormControl>
          </TableCell>
        </TableRow>
      ));
    }
    return null;
  };

  const renderPresetMapping = () => {
    return (
      <Box display="flex" style={{ marginTop: '30px', marginBottom: '30px' }}>
        <Box flexGrow={1}>
          <FormGroup row>
            <FormControl className={classes.formControl}>
              <TextField
                id="preset-name"
                className={classes.textField}
                label="Preset name"
                value={mappingPresetName}
                onChange={(e) => {
                  setMappingPresetName(e.target.value)
                }}
              />
            </FormControl>
            <FormControl className={classes.formControl}>
              <Button
                color="primary"
                className={classes.inlineButton}
                onClick={saveMapping}
              >
                Save this preset
              </Button>
            </FormControl>
          </FormGroup>
        </Box>
        <Box>
          <FormControl className={classes.formControl} flexGrow={1}>
            <InputLabel id="preset-label">Load preset</InputLabel>
            <Select
              labelId="preset-label"
              name="mappingPreset"
              value={mappingPreset || ''}
              onChange={e => selectMapping(e.target.value)}
            >
              {mappingPresets.map(preset => <MenuItem key={preset.id} value={preset.id}>{preset.name}</MenuItem>)}
            </Select>
          </FormControl>
        </Box>
      </Box>
    )
  };

  return (
    <div className={classes.root}>
      {loaded ? (
        <>
          <div className={classes.tableWrapper}>
            <Table stickyHeader>
              <TableHead>
                <TableRow>
                  <TableCell className={classes.head}>Column name</TableCell>
                  <TableCell className={classes.head}>Preview</TableCell>
                  <TableCell className={classes.head}>Mapped column</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {renderColumns()}
              </TableBody>
            </Table>
          </div>
          {renderPresetMapping()}
        </>
      ) : (
        <Loader variant="wrapped"/>
      )}
    </div>
  );
}

export default MappingStep;
