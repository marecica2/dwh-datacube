package org.bmsource.dwh.common.reader;

import com.monitorjbl.xlsx.StreamingReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelReader implements DataReader {

    private static Integer DEFAULT_BATCH_SIZE = 5000;

    private void readExcelStream(InputStream inputStream, DataHandler dataHandler,
                                 Integer batchSize, Integer rowsLimit, boolean headerExcluded) throws Exception {
        try (
            Workbook workbook = StreamingReader.builder()
                .rowCacheSize(batchSize)
                .bufferSize(32000)
                .open(inputStream)) {
            dataHandler.onStart();
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            int totalRows = 0;
            List<List<Object>> rows = new LinkedList<>();
            List<Object> headerRow = readSingleRow(rowIterator);
            List<String> headerStringRow = headerRow
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());
            if (!headerExcluded) {
                rows.add(headerRow);
            }
            while ((rowIterator.hasNext()) && (totalRows < rowsLimit || rowsLimit == -1)) {
                rows.add(readSingleRow(rowIterator));
                totalRows++;
                if (totalRows % batchSize == 0) {
                    dataHandler.onRead(rows, headerStringRow, totalRows, sheet.getLastRowNum());
                    rows = new LinkedList<>();
                }
            }
            dataHandler.onRead(rows, headerStringRow, totalRows, sheet.getLastRowNum());
            dataHandler.onFinish(sheet.getLastRowNum());
        }
    }

    private List<Object> readSingleRow(Iterator<Row> rowIterator) {
        List<Object> rowContainer = new LinkedList<>();
        int prevCellIndex = 0;

        Row sheetRow = rowIterator.next();
        for (Cell sheetCell : sheetRow) {
            int currentIndex = sheetCell.getColumnIndex();
            fillGaps(rowContainer, prevCellIndex, currentIndex);
            rowContainer.add(sheetCell.getStringCellValue());
            prevCellIndex = currentIndex;
        }
        return rowContainer;
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
        if (rowsToRead > 10000) {
            throw new IllegalArgumentException("Rows to read must not be greater than 10000");
        }
        List<List<Object>> result = new ArrayList<>();
        readExcelStream(inputStream, (rows, header, rowsCount, totalRowsCount) -> {
                result.addAll(rows);
            },
            rowsToRead, rowsToRead, true);
        return result;
    }

    private void fillGaps(List<Object> row, int prevIndex, int currentIndex) {
        for (int i = prevIndex + 1; i < currentIndex; i++) {
            row.add(null);
        }
    }


}