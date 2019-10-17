package org.bmsource.dwh.common.importer.batch;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@StepScope
@Component
@Scope("prototype")
public class ExcelItemReader implements ItemStreamReader<List<Object>>, ImportContext {

    private Iterator<Row> rowIterator;

    private Workbook workbook;

    private InputStream inputStream;

    private ExecutionContext executionContext;

    private int line = 0;

    @Override
    public List<Object> read() {
        List<Object> row = readSingleRow(this.rowIterator);
        if (row != null && row.size() > 0 && row.get(0) != null) {
            System.out.println(line + "  " + row.get(0) + " " + Thread.currentThread().getName() + " " + executionContext.get("fileName") + " wb" + workbook.toString());
            return row;
        }
        return null;
    }

    @Override
    public void open(@NotNull ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        String fileName = (String) executionContext.get("fileName");
        File file = null;
        try {
            file = ResourceUtils.getFile(this.getClass().getResource(fileName));
        } catch (FileNotFoundException e) {
            throw new ItemStreamException(e);
        }
        try (
            InputStream inputStream = FileUtils.openInputStream(file);
            Workbook workbook = StreamingReader.builder()
                .rowCacheSize(5000)
                .bufferSize(32000)
                .open(inputStream)) {
            this.inputStream = inputStream;
            this.workbook = workbook;
            Sheet sheet = workbook.getSheetAt(0);
            this.rowIterator = sheet.rowIterator();
            List<String> columns =
                readSingleRow(this.rowIterator)
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            saveToContext(executionContext, ImportContext.headerKey, String.join(",", columns));
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
            line++;
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