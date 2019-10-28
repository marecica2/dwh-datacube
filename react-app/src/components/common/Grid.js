import './Grid.css'
import React, { useState, useEffect, useCallback } from 'react';
import { equals } from 'ramda';
import Promise from 'bluebird';
import Paper from '@material-ui/core/Paper';
import {
  PagingState,
  CustomPaging,
  EditingState,
} from '@devexpress/dx-react-grid';
import {
  Grid,
  Table,
  TableHeaderRow,
  PagingPanel,
  TableEditRow,
  TableEditColumn,
} from '@devexpress/dx-react-grid-material-ui';
import FileUpload from './FileUpload';
import Loader from './Loader';

const getRowId = row => row.id;


function LoadingRow(props) {
  return (
    <tbody>
      <tr className="gridSpinner">
        <td
          style={{ textAlign: 'center', padding: '10px' }}
          colSpan={props.children[0].props.columns.length}
        >
          <Loader/>
        </td>
      </tr>
      {props.children}
    </tbody>
  )
};

export default ({ columnsConfig, crudApi, editable = false, uploader = false }) => {
  const [columns] = useState(columnsConfig);
  const [rows, setRows] = useState([]);
  const [totalCount, setTotalCount] = useState(0);
  const [pageSize] = useState(10);
  const [currentPage, setCurrentPage] = useState(0);
  const [previousRequest, setPreviousRequest] = useState();
  const [loading, setLoading] = useState(false);

  const awaitable = (fn) => {
    // eslint-disable-next-line consistent-return
    return (args) => {
      setLoading(true);
      return new Promise((resolve, reject) => {
        try {
          // eslint-disable-next-line no-undef
          fn.apply(this, [args]).then((result) => {
            setLoading(false);
            resolve(result);
          });
        } catch (ex) {
          setLoading(false);
          reject(ex);
        }
      });
    };
  };

  const fetch = useCallback(async () => {
    const request = { currentPage, pageSize };
    if (!equals(previousRequest, request)) {
      const { items, pagination } = await awaitable(crudApi.paginate)(request);
      if (currentPage > 0 && currentPage >= pagination.totalPages) {
        setCurrentPage(pagination.totalPages - 1);
      }
      setRows(items);
      setTotalCount(pagination.totalElements);
      setPreviousRequest(request);
    }
  }, [crudApi, setRows, setTotalCount, currentPage, setCurrentPage, pageSize, previousRequest, setPreviousRequest]);

  useEffect(() => {
    fetch();
  }, [crudApi, fetch]);

  const rowChange = useCallback(async ({ added, changed, deleted }) => {
    if (added) {
      setPreviousRequest(null);
      await Promise.map(added, data => crudApi.create({ data }));
      await fetch();
    }
    if (changed) {
      setPreviousRequest(null);
      await Promise.map(Object.entries(changed), ([id, data]) =>
        crudApi.patch({ id, data }));
      await fetch();
    }
    if (deleted) {
      setPreviousRequest(null);
      await Promise.map(deleted, id =>
        crudApi.delete({ id }));
      await fetch();
    }
  }, [crudApi, fetch]);


  const afterUpload = useCallback(() => {
    setPreviousRequest(null);
    fetch();
  }, [fetch]);

  return (
    <Paper className={ loading ? 'gridSpinnerActive' : 'gridSpinnerInactive'}>
      {uploader && (
        <FileUpload uploadApi={crudApi.fileUpload} after={afterUpload}/>
      )}
      <Grid
        rows={rows}
        columns={columns}
        getRowId={getRowId}
      >
        <EditingState
          onCommitChanges={rowChange}
        />
        <PagingState
          currentPage={currentPage}
          onCurrentPageChange={setCurrentPage}
          pageSize={pageSize}
        />
        <CustomPaging
          totalCount={totalCount}
        />
        <Table bodyComponent={LoadingRow} loading={loading}/>
        <TableHeaderRow/>
        {!loading && editable && (
          <TableEditRow/>
        )}
        {editable && (
          <TableEditColumn
            showAddCommand={!loading}
            showEditCommand={!loading}
            showDeleteCommand={!loading}
          />
        )}
        <PagingPanel/>
      </Grid>
    </Paper>
  );
};