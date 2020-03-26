import React, { useState, useEffect, useCallback } from 'react';
import { equals } from 'ramda';
import Promise from 'bluebird';
import { Paper, IconButton } from '@material-ui/core';
import DownloadIcon from '@material-ui/icons/CloudDownload';
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
  TableColumnResizing,
} from '@devexpress/dx-react-grid-material-ui';
import FileUpload from './FileUpload';
import Loader from './Loader';
import './Grid.css'

const getRowId = row => row.id;


export default ({ columnsConfig, crudApi, editable = false, uploader = false, downloader = false }) => {
  const [columns] = useState(columnsConfig);
  const [rows, setRows] = useState([]);
  const [totalCount, setTotalCount] = useState(0);
  const [pageSize, setPageSize] = useState(20);
  const [currentPage, setCurrentPage] = useState(0);
  const [previousRequest, setPreviousRequest] = useState();
  const [loading, setLoading] = useState(false);
  const [pageSizes] = useState([10, 20, 50]);
  const [defaultColumnWidths] = useState(columnsConfig.map(cfg => ({
    columnName: cfg.name,
    width: cfg.width ? cfg.width : 'auto',
  })));

  const awaitable = (fn) => {
    return (args) => {
      setLoading(true);
      return new Promise((resolve, reject) => {
        fn.apply(this, [args]).then((result) => {
          setLoading(false);
          resolve(result);
        }).catch((e) => {
          setLoading(false);
          reject(e);
        })
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
    if (loading)
      return;

    if (added) {
      await Promise.map(added, data => crudApi.create({ data }));
      setPreviousRequest(null);
      fetch();
    }
    if (changed) {
      await Promise.map(Object.entries(changed), ([id, data]) =>
        crudApi.patch({ id, data }));
      setPreviousRequest(null);
      fetch();
    }
    if (deleted) {
      await Promise.map(deleted, id =>
        crudApi.delete({ id }));
      setPreviousRequest(null);
      fetch();
    }
  }, [crudApi, fetch, setPreviousRequest, loading]);


  const afterUpload = useCallback(() => {
    setPreviousRequest(null);
    fetch();
  }, [fetch]);

  return (
    <Paper className="loadable-container">
      <div className={loading ? 'loadable-content' : ''}>
        <div style={{ display: 'flex', justifyContent: 'flex-end', padding: '10px' }}>
          {uploader && (
            <FileUpload uploadApi={crudApi.fileUpload} after={afterUpload}/>
          )}
          {downloader && (
            <IconButton>
              <DownloadIcon/>
            </IconButton>
          )}
        </div>
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
            onPageSizeChange={setPageSize}
          />
          <CustomPaging
            totalCount={totalCount}
          />
          <Table/>
          <TableColumnResizing
            defaultColumnWidths={defaultColumnWidths}
            resizingMode='nextColumn'
          />
          <TableHeaderRow/>
          {editable && (
            <>
              <TableEditRow/>
              <TableEditColumn
                showAddCommand
                showEditCommand
                showDeleteCommand
              />
            </>
          )}
          <PagingPanel
            pageSizes={pageSizes}
          />
        </Grid>
      </div>
      {loading && (
        <div className="loadable-spinner">
          <div className="loadable-spinner-container">
            <Loader/>
          </div>
        </div>
      )}
    </Paper>
  );
};
