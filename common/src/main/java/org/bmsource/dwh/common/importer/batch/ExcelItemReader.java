package org.bmsource.dwh.common.importer.batch;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExcelItemReader implements ItemStreamReader<List<Object>>, ImportContext {

    private InputStream inputStream;
    private Iterator<Row> rowIterator;
    private Workbook workbook;

    ExcelItemReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public List<Object> read() {
        return readSingleRow(this.rowIterator);
    }

    @Override
    public void open(@NotNull ExecutionContext executionContext) throws ItemStreamException {
        try (
            Workbook workbook = StreamingReader.builder()
                .rowCacheSize(5000)
                .bufferSize(32000)
                .open(inputStream)) {
            this.workbook = workbook;
            Sheet sheet = workbook.getSheetAt(0);
            this.rowIterator = sheet.rowIterator();
            saveToContext(executionContext, ImportContext.headerKey, readSingleRow(this.rowIterator));
        } catch (IOException e) {
            throw new ItemStreamException(e);
        }
    }

    @Override
    public void update(@NotNull ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {
        try {
            this.workbook.close();
            this.inputStream.close();
        } catch (IOException e) {
            throw new ItemStreamException(e);
        }
    }

    private List<Object> readSingleRow(Iterator<Row> rowIterator) {
        if (rowIterator.hasNext()) {
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
        return null;
    }

    private void fillGaps(List<Object> row, int prevIndex, int currentIndex) {
        for (int i = prevIndex + 1; i < currentIndex; i++) {
            row.add(null);
        }
    }
}