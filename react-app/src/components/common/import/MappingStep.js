import React, {useEffect} from 'react';
import {makeStyles} from '@material-ui/core/styles';
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

function MappingStep({transaction, mapping, setMapping, uploadedFiles}) {
  const classes = useStyles();
  const [columnMapping, setColumnMapping] = React.useState({});
  const [assignedColumns, setAssignedColumns] = React.useState([]);

  const selectColumn = ({name, value}) => {
    const prevValue = columnMapping[name];
    if (value === '') {
      setAssignedColumns(prev => prev.filter(item => item !== prevValue));
    } else {
      setAssignedColumns(prev => ([...prev, value]));
    }
    setColumnMapping(prev => ({...prev, [name]: value}));
  };

  const autoSuggestMapping = (map) => {
    Object.entries(map).forEach(([k, v]) => {
      selectColumn({name: k, value: v});
    });
  };

  useEffect(() => {
    async function fetchData() {
      if (mapping == null) {
        const map = await ImportApi.getMapping(transaction, uploadedFiles);
        setMapping(map);
        autoSuggestMapping(map.mapping);
      }
    }

    fetchData();
  }, []);

  const renderColumns = () => {
    if (mapping && mapping.sourceColumns) {
      return Object.entries(mapping.sourceColumns).map(([col, val]) => (
        <TableRow key={col}>
          <TableCell>
            {col}
          </TableCell>
          <TableCell>
            {val}
          </TableCell>
          <TableCell>
            <FormControl className={classes.formControl}>
              <Select name={col} value={columnMapping[col] || ''} onChange={e => selectColumn(e.target)}>
                <Typography variant="caption" display="block" className={classes.label}>
                      Required fields
                </Typography>
                {
                    Object.entries(mapping.destinationColumns)
                    .filter(([, val2]) => val2.required)
                    .filter(([col2]) => !assignedColumns.includes(col2)
                        || columnMapping[col] === col2)
                    .map(([col2, val2]) => (
                      <MenuItem key={col2} value={col2}>
                        {val2.label}
                      </MenuItem>
                    ))
                  }
                <Typography variant="caption" display="block" className={classes.label}>
                      Optional fields
                </Typography>
                {
                    Object.entries(mapping.destinationColumns)
                    .filter(([, val2]) => val2.required == null)
                    .filter(([col2]) => !assignedColumns.includes(col2)
                        || columnMapping[col] === col2)
                    .map(([col2, val2]) => (
                      <MenuItem key={col2} value={col2}>
                        {val2.label}
                      </MenuItem>
                    ))
                  }
                <MenuItem value="" style={{color: grey[500]}}>
                  Unmapped
                </MenuItem>
              </Select>
            </FormControl>
          </TableCell>
        </TableRow>
      ));
    }
    return null;
  };

  return (
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
  );
}

export default MappingStep;
