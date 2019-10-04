import React, { useEffect } from 'react';
import { Table, TableBody, TableCell, TableHead, TableRow } from '@material-ui/core';
import ImportApi from './ImportApi';
import Loading from './Loading';

export const stepName = 'Preview data';

function PreviewStep({ transaction, mapping, preview, setPreview }) {
    const [loaded, setLoaded] = React.useState(false);

    useEffect(() => {
        async function fetchData() {
            if (preview == null) {
                const result = await ImportApi.getPreview(transaction, mapping);
                setPreview(result);
                setLoaded(true);
            }
        }
        fetchData();
    }, [transaction, mapping, preview, setPreview]);

    const renderColumns = () => {
        return (
            preview.map(item => (
              <TableRow key={item.transactionId}>
                <TableCell>{item.transactionId}</TableCell>
                <TableCell>{item.supplierName}</TableCell>
                <TableCell>{item.serviceType}</TableCell>
              </TableRow>
            ))
        );
    };

    return (
      <div>
        { loaded ? (
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Transaction Id</TableCell>
                <TableCell>Supplier name</TableCell>
                <TableCell>Service type</TableCell>
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

export default PreviewStep;