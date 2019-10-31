package org.bmsource.dwh.common.importer.batch;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.bmsource.dwh.common.fileManager.FileManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
@Scope("prototype")
public class ExcelItemReader implements ItemStreamReader<List<Object>>, ImportContext {

    private Logger logger = LoggerFactory.getLogger(ExcelItemReader.class.getName());

    private int rowsCount = -1;

    private Iterator<Row> rowIterator;

    private Workbook workbook;

    private InputStream inputStream;

    @Value("#{jobParameters['transaction']}")
    private String transaction;

    @Value("#{stepExecutionContext['fileName']}")
    private String fileName;

    @Autowired
    private FileManager fileManager;

    @Override
    public List<Object> read() {
        List<Object> row = readSingleRow(this.rowIterator);
        if (row != null && row.size() > 0 && row.get(0) != null) {
            return row;
        }
        return null;
    }

    @Override
    public void open(@NotNull ExecutionContext executionContext) throws ItemStreamException {
        try {
            InputStream inputStream = fileManager.getStream(transaction, fileName);
            Workbook workbook = StreamingReader.builder()
                .rowCacheSize(5000)
                .bufferSize(1024)
                .open(inputStream);
            this.inputStream = inputStream;
            this.workbook = workbook;

            logger.debug("Excel file {} opened for reading", this.fileName);
            Sheet sheet = workbook.getSheetAt(0);
            executionContext.put(ImportContext.totalRowsKey, sheet.getLastRowNum());
            executionContext.put(ImportContext.rowsKey, 0);
            this.rowIterator = sheet.rowIterator();
            List<String> columns =
                readSingleRow(this.rowIterator)
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            executionContext.put(ImportContext.headerKey, String.join(",", columns));
        } catch (Exception e) {
            try {
                this.workbook.close();
                this.inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new ItemStreamException(e);
        }
    }

    @Override
    public void update(@NotNull ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
        try {
            logger.debug("Closing excel file " + this.fileName);
            this.workbook.close();
            this.inputStream.close();
        } catch (IOException e) {
            throw new ItemStreamException(e);
        }
    }

    private List<Object> readSingleRow(Iterator<Row> rowIterator) {
        if (rowIterator.hasNext()) {
            List<Object> row = new LinkedList<>();
            int prevCellIndex = 0;
            Row sheetRow = rowIterator.next();
            for (Cell sheetCell : sheetRow) {
                int currentIndex = sheetCell.getColumnIndex();
                fillGaps(row, prevCellIndex, currentIndex);
                row.add(sheetCell.getStringCellValue());
                prevCellIndex = currentIndex;
            }
            rowsCount++;
            return row;
        }
        return null;
    }

    private void fillGaps(List<Object> row, int prevIndex, int currentIndex) {
        for (int i = prevIndex + 1; i < currentIndex; i++) {
            row.add(null);
        }
    }
}