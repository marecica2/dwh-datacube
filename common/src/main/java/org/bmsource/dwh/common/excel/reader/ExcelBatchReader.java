package org.bmsource.dwh.common.excel.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelBatchReader implements DataReader {

    private static Integer DEFAULT_BATCH_SIZE = 5000;

    private void readExcelStream(InputStream inputStream, DataHandler dataHandler,
                                 Integer batchSize, Integer rowsLimit, boolean headerExcluded) throws Exception {
        try (
            ExcelRead reader = new ExcelRead(inputStream)) {
            dataHandler.onStart();

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
                    dataHandler.onRead(rows, headerStringRow, totalRows, reader.getTotalRowsCount());
                    rows = new LinkedList<>();
                }
            }
            dataHandler.onRead(rows, headerStringRow, totalRows, reader.getTotalRowsCount());
            dataHandler.onFinish(reader.getTotalRowsCount());
        }
    }

    @Override
    public MappingResult readHeaderRow(InputStream inputStream) throws Exception {
        List<List<Object>> twoRows = new ArrayList<>();
        readExcelStream(inputStream, (rows, headerRow, totalRows, totalRowsCount) -> {
                twoRows.addAll(rows);
            },
            2, 2, false);
        List<String> headerRow = twoRows.get(0).stream().map(Object::toString).collect(Collectors.toList());
        List<Object> previewRow = twoRows.get(1);
        return new MappingResult(headerRow, previewRow);
    }

    @Override
    public void readContent(InputStream inputStream, DataHandler dataHandler) throws Exception {
        readExcelStream(inputStream, dataHandler, DEFAULT_BATCH_SIZE, -1, true);
    }

    @Override
    public void readContent(InputStream inputStream, DataHandler dataHandler, Integer batchSize) throws Exception {
        readExcelStream(inputStream, dataHandler, batchSize, -1, true);
    }

    @Override
    public List<List<Object>> readContent(InputStream inputStream, int rowsToRead) throws Exception {
        if (rowsToRead > 1000) {
            throw new IllegalArgumentException("Rows to read must not be greater than 1000");
        }
        List<List<Object>> result = new ArrayList<>();
        readExcelStream(inputStream, (rows, header, rowsCount, totalRowsCount) -> {
                result.addAll(rows);
            },
            rowsToRead, rowsToRead, true);
        return result;
    }
}