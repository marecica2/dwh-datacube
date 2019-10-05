import React, { useEffect, useCallback } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  FormControl,
  MenuItem,
  Select, Typography,
} from '@material-ui/core';
import grey from '@material-ui/core/colors/grey';
import Loader from '../Loader';
import ImportApi from './ImportApi';

const useStyles = makeStyles(theme => ({
  formControl: {
    margin: theme.spacing(1),
    minWidth: 200,
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

function MappingStep({transaction, mappingConfig, setMappingConfig, mapping, setMapping, files, setPreview, assignedColumns, setAssignedColumns}) {
  const classes = useStyles();
  const [loaded, setLoaded] = React.useState(false);


  const selectColumn = useCallback(({name, value}) => {
    const prevValue = mapping[name];
    if (value === '') {
      setAssignedColumns(prev => prev.filter(item => item !== prevValue));
    } else {
      setAssignedColumns(prev => ([...prev, value]));
    }
    setMapping(prev => ({...prev, [name]: value}));
    setPreview(null);
  }, [mapping, setMapping, setAssignedColumns]);

  useEffect(() => {
    const autoSuggestMapping = (map) => {
      Object.entries(map).forEach(([k, v]) => {
        selectColumn({name: k, value: v});
      });
    };

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
  }, [transaction, mappingConfig, setMappingConfig, files, setLoaded, selectColumn, setAssignedColumns]);

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

  return (
    <div className={classes.root}>
      { loaded ? (
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
      ) : <Loader />
      }
    </div>
  );
}

export default MappingStep;
