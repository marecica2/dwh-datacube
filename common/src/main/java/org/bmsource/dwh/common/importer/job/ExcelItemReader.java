package org.bmsource.dwh.common.importer.job;

import org.apache.poi.ss.usermodel.Row;
import org.bmsource.dwh.common.fileManager.FileManager;
import org.bmsource.dwh.common.io.reader.ExcelReader;
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
import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
@Scope("prototype")
public class ExcelItemReader<T> implements ItemStreamReader<List<Object>> {

    private Logger logger = LoggerFactory.getLogger(ExcelItemReader.class.getName());

    private int rowsCount = -1;

    private Iterator<Row> rowIterator;

    private ExcelReader excelReader;


    @Value("#{jobParameters['transaction']}")
    private String transaction;

    @Value("#{stepExecutionContext['fileName']}")
    private String fileName;

    @Autowired
    private FileManager fileManager;

    @Override
    public List<Object> read() {
        return excelReader.nextRow();
    }

    @Override
    public void open(@NotNull ExecutionContext executionContext) throws ItemStreamException {
        try {
            InputStream inputStream = fileManager.getStream(transaction, fileName);
            excelReader = new ExcelReader(inputStream);
            logger.debug("Excel file {} opened for reading", this.fileName);

            executionContext.put(ImportContext.totalRowsKey, excelReader.getTotalRowsCount());
            executionContext.put(ImportContext.rowsKey, 0);
            List<String> columns = getHeader();
            executionContext.put(ImportContext.headerKey, String.join(",", columns));
        } catch (Exception e) {
            try {
                excelReader.close();
            } catch (IOException ex) {
                throw new ItemStreamException(e);
            }
            throw new ItemStreamException(e);
        }
    }

    @Override
    public void update(@NotNull ExecutionContext executionContext) throws ItemStreamException {
    }

    private List<String> getHeader() {
        return excelReader.nextRow()
            .stream()
            .map(Object::toString)
            .collect(Collectors.toList());
    }

    @Override
    public void close() throws ItemStreamException {
        try {
            logger.debug("Closing excel file " + this.fileName);
            excelReader.close();
        } catch (IOException e) {
            throw new ItemStreamException(e);
        }
    }

}