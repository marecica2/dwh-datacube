package org.bmsource.dwh.common.io.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelBatchReader implements BatchReader {

    private static Integer DEFAULT_BATCH_SIZE = 5000;

    private void readExcelStream(InputStream inputStream, BatchHandler batchHandler,
                                 Integer batchSize, Integer rowsLimit, boolean headerExcluded) throws Exception {
        try (
            ExcelReader reader = new ExcelReader(inputStream)) {
            batchHandler.onStart();

            int totalRows = 0;
            List<List<Object>> rows = new LinkedList<>();
            List<Object> headerRow = reader.nextRow();
            List<String> headerStringRow = headerRow
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());
            if (!headerExcluded) {
                rows.add(headerRow);
            }

            while ((reader.hasNextRow()) && (totalRows < rowsLimit || rowsLimit == -1)) {
                List<Object> row = reader.nextRow();
                if (row == null) break; // TODO remove if possible
                rows.add(row);
                totalRows++;
                if (totalRows % batchSize == 0) {
                    batchHandler.onRead(rows, headerStringRow, totalRows, reader.getTotalRowsCount());
                    rows = new LinkedList<>();
                }
            }
            batchHandler.onRead(rows, headerStringRow, totalRows, reader.getTotalRowsCount());
            batchHandler.onFinish(reader.getTotalRowsCount());
        }
    }

    @Override
    public void readContent(InputStream inputStream, BatchHandler batchHandler) throws Exception {
        readExcelStream(inputStream, batchHandler, DEFAULT_BATCH_SIZE, -1, true);
    }

    @Override
    public void readContent(InputStream inputStream, BatchHandler batchHandler, Integer batchSize) throws Exception {
        readExcelStream(inputStream, batchHandler, batchSize, -1, true);
    }

    @Override
    public List<List<Object>> readContent(InputStream inputStream, int rowsToRead) throws Exception {
        return readContent(inputStream, rowsToRead, true);
    }

    @Override
    public List<List<Object>> readContent(InputStream inputStream, int rowsToRead, boolean excludeHeader) throws Exception {
        if (rowsToRead > 1000) {
            throw new IllegalArgumentException("Rows to read must not be greater than 1000");
        }
        List<List<Object>> result = new ArrayList<>();
        readExcelStream(inputStream, (rows, header, rowsCount, totalRowsCount) -> {
                result.addAll(rows);
            },
            rowsToRead, rowsToRead, excludeHeader);
        return result;
    }
}