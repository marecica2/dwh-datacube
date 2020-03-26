import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import pink from '@material-ui/core/colors/pink';
import green from '@material-ui/core/colors/lightGreen';
import ErrorIcon from '@material-ui/icons/Clear';
import OkIcon from '@material-ui/icons/Check';
import { Table, TableBody, TableCell, TableHead, TableRow } from '@material-ui/core';
import ImportApi from '../../../shared/api/import.api';
import Loader from '../Loader';

export const stepName = 'Preview data';


const useStyles = makeStyles(() => ({
  tableWrapper: {
    maxHeight: '50vh',
    overflow: 'auto',
    width: 'calc(100vw - 320px)',
  },
  head: {
    backgroundColor: 'white',
    position: 'sticky',
    zIndex: '999',
    top: 0,
    minWidth: '150px',
  },
  error: {
    backgroundColor: pink[100],
  },
  success: {
    color: green,
  },
}));

function PreviewStep({ transaction, mappingConfig, mapping, preview, setPreview }) {
  const classes = useStyles();
  const [loaded, setLoaded] = React.useState(false);

  const columns = Object.entries(mappingConfig.destinationColumns).map(([k, v]) => {
    return {
      id: k,
      label: v.label,
      format: value => ['BigDecimal', 'Double', 'Integer'].includes(v.type) ? value.toFixed(2) : value.toLocaleString(),
    }
  });
  columns.unshift({ id: 'valid', label: 'Valid', format: value => value });

  useEffect(() => {
    async function fetchData() {
      if (preview == null) {
        setLoaded(false);
        const response = await ImportApi.getPreview(transaction, mapping);
        const result = response.map(item => ({ ...item, entity: { valid: item.valid, ...item.entity } }));
        setPreview(result);
      }
      setLoaded(true);
    }

    fetchData();
  }, [transaction, mapping, preview, setPreview, setLoaded]);

  const renderRows = () => {
    return (
      preview.map(row => (
        <TableRow key={preview.indexOf(row)}>
          {Object.entries(row.entity).map(([key, cell], idx) => {
            const error = row.errors[key] != null ? row.errors[key].join(',') : null;
            if (key === 'valid')
              return <TableCell key={`${preview.indexOf(row)}_${idx}`}>{cell ? <OkIcon style={{color: green}} alt="okIcon" /> : <ErrorIcon color="secondary" alt="errorIcon"/>}</TableCell>;

            return (
              <TableCell
                className={error && classes.error}
                key={`${preview.indexOf(row)}_${idx}`}
              >
                {error || cell}
              </TableCell>
            )
          })}
        </TableRow>
      ))
    );
  };

  return (
    <div id="previewStep">
      <div className={classes.tableWrapper}>
        {loaded ? (
          <Table stickyHeader>
            <TableHead>
              <TableRow>
                {columns.map(column => (
                  <TableCell
                    className={classes.head}
                    key={column.id}
                    align={column.align}
                  >
                    {column.label}
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {renderRows()}
            </TableBody>
          </Table>
        ) : <Loader variant="wrapped" />
        }
      </div>
    </div>
  );
}

export default PreviewStep;