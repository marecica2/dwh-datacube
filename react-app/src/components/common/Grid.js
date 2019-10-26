import React, { useState, useEffect } from 'react';
import Paper from '@material-ui/core/Paper';
import {
  PagingState,
  CustomPaging,
} from '@devexpress/dx-react-grid';
import {
  Grid,
  Table,
  TableHeaderRow,
  PagingPanel,
} from '@devexpress/dx-react-grid-material-ui';
import { equals } from 'ramda';

export default ({ columnsConfig, crudApi }) => {
  const [columns] = useState(columnsConfig);
  const [rows, setRows] = useState([]);
  const [totalCount, setTotalCount] = useState(0);
  const [pageSize] = useState(10);
  const [currentPage, setCurrentPage] = useState(0);
  const [previousRequest, setPreviousRequest] = useState(false);

  useEffect(() => {
    const loadData = async () => {
      try {
        const request = { currentPage, pageSize };
        if (!equals(previousRequest, request)) {
          const { items, pagination } = await crudApi.paginate(request);
          setRows(items);
          setTotalCount(pagination.totalElements);
          setPreviousRequest(request);
        }
      } catch (e) {
        console.log(e);
      }
    };
    loadData();
  }, [crudApi, setRows, setTotalCount, currentPage, pageSize, previousRequest, setPreviousRequest]);

  return (
    <Paper style={{ position: 'relative' }}>
      {rows.length === 0 ?
        (<span>Loading</span>) :
        (
          <Grid
            rows={rows}
            columns={columns}
          >
            <PagingState
              currentPage={currentPage}
              onCurrentPageChange={setCurrentPage}
              pageSize={pageSize}
            />
            <CustomPaging
              totalCount={totalCount}
            />
            <Table/>
            <TableHeaderRow/>
            <PagingPanel/>
          </Grid>
        )
      }

    </Paper>
  );
};