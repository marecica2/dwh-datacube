package org.bmsource.dwh.common.excel.reader;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExcelRead implements AutoCloseable {

    private static final int DEFAULT_CACHE_SIZE = 5000;

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private Workbook workbook;

    private InputStream inputStream;

    private Iterator<Row> rowIterator;

    private Sheet sheet;

    private int rowsRead = -1;

    private int rowsCount = 0;

    public ExcelRead(InputStream inputStream) {
        this(inputStream, DEFAULT_CACHE_SIZE, DEFAULT_BUFFER_SIZE);
    }

    public ExcelRead(InputStream inputStream, int cacheSize, int bufferSize) {
        this.inputStream = inputStream;
        this.workbook = StreamingReader.builder()
            .rowCacheSize(cacheSize)
            .bufferSize(bufferSize)
            .open(inputStream);
        sheet = workbook.getSheetAt(0);
        rowIterator = sheet.rowIterator();
        rowsCount = sheet.getLastRowNum();
    }

    public int getTotalRowsCount() {
        return rowsCount;
    }

    public int getReadRowsCount() {
        return rowsRead;
    }

    public void reset() {
        rowsRead = -1;
        rowIterator = sheet.rowIterator();
    }

    public void close() throws IOException {
        workbook.close();
        inputStream.close();
    }

    public List<Object> nextRow() {
        List<Object> row = new LinkedList<>();
        if (rowIterator.hasNext()) {
            int prevCellIndex = 0;
            Row sheetRow = rowIterator.next();
            for (Cell sheetCell : sheetRow) {
                int currentIndex = sheetCell.getColumnIndex();
                fillGaps(row, prevCellIndex, currentIndex);
                row.add(sheetCell.getStringCellValue());
                prevCellIndex = currentIndex;
            }
            rowsRead++;
            if (row.size() > 0 && row.get(0) != null) {
                return row;
            }
        }
        return null;
    }

    public boolean hasNextRow() {
        return rowIterator.hasNext() && rowsRead < rowsCount;
    }

    private void fillGaps(List<Object> row, int prevIndex, int currentIndex) {
        for (int i = prevIndex + 1; i < currentIndex; i++) {
            row.add(null);
        }
    }

//    private List<Object> readSingleRow(Iterator<Row> rowIterator) {
//        List<Object> rowContainer = new LinkedList<>();
//        int prevCellIndex = 0;
//
//        Row sheetRow = rowIterator.next();
//        for (Cell sheetCell : sheetRow) {
//            int currentIndex = sheetCell.getColumnIndex();
//            fillGaps(rowContainer, prevCellIndex, currentIndex);
//
//            Object value = null;
//            List<Function<Cell, Object>> parsers = new ArrayList<Function<Cell, Object>>() {{
//                add(Cell::getDateCellValue);
//                add(Cell::getBooleanCellValue);
//                add(Cell::getNumericCellValue);
//                add(Cell::getStringCellValue);
//            }};
//            value = getDateValue(sheetCell, parsers);
//            rowContainer.add(value);
//            prevCellIndex = currentIndex;
//        }
//        if (rowContainer.size() == 1 && rowContainer.get(0).equals(""))
//            return null;
//        return rowContainer;
//    }
//
//    private Object getDateValue(Cell sheetCell, List<Function<Cell, Object>> parsers) {
//        Object value;
//
//        for (Function<Cell, Object> parser : parsers) {
//            try {
//                return parser.apply(sheetCell);
//                //return sheetCell.getDateCellValue();
//            } catch (Exception e) {
//                // Omitted
//            }
//        }
//        return null;
//    }
}
