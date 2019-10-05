import React, { useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { Table, TableBody, TableCell, TableHead, TableRow } from '@material-ui/core';
import ImportApi from './ImportApi';
import Loader from '../Loader';

export const stepName = 'Preview data';

const useStyles = makeStyles({
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
});

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

    useEffect(() => {
        async function fetchData() {
            if (preview == null) {
                setLoaded(false);
                const result = await ImportApi.getPreview(transaction, mapping);
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
                {Object.values(row).map((cell, idx) => (
                  <TableCell key={`${preview.indexOf(row)}_${idx}`}>{cell}</TableCell>
                ))}
              </TableRow>
            ))
        );
    };

    return (
      <div>
        <div className={classes.tableWrapper}>
          { loaded ? (
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
          ) : <Loader />
            }
        </div>
      </div>
    );
}

export default PreviewStep;