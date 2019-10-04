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
import Loading from './Loading';
import ImportApi from './ImportApi';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  formControl: {
    margin: theme.spacing(1),
    minWidth: 200,
  },
  selectEmpty: {
    marginTop: theme.spacing(2),
  },
  label: {
    margin: theme.spacing(2),
    color: grey[500],
  },
}));

export const stepName = 'Column mapping';

function MappingStep({transaction, mappingConfig, setMappingConfig, mapping, setMapping, files}) {
  const classes = useStyles();
  const [loaded, setLoaded] = React.useState(false);
  const [assignedColumns, setAssignedColumns] = React.useState([]);

  const selectColumn = useCallback(({name, value}) => {
    const prevValue = mapping[name];
    if (value === '') {
      setAssignedColumns(prev => prev.filter(item => item !== prevValue));
    } else {
      setAssignedColumns(prev => ([...prev, value]));
    }
    setMapping(prev => ({...prev, [name]: value}));
  }, [mapping, setMapping, setAssignedColumns]);

  useEffect(() => {
    const autoSuggestMapping = (map) => {
      Object.entries(map).forEach(([k, v]) => {
        selectColumn({name: k, value: v});
      });
    };

    async function fetchData() {
      if (mappingConfig == null) {
        const result = await ImportApi.getMapping(transaction, Object.keys(files));
        setMappingConfig(result);
        autoSuggestMapping(result.mapping);
        setLoaded(true);
      }
    }
    fetchData();
  }, [transaction, mappingConfig, setMappingConfig, files, setLoaded, selectColumn]);

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
    <div>
      { loaded ? (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Column name</TableCell>
              <TableCell>Preview</TableCell>
              <TableCell>Mapped column</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {renderColumns()}
          </TableBody>
        </Table>
      ) : <Loading />
      }
    </div>
  );
}

export default MappingStep;
