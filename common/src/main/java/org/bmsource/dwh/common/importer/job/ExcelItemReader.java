package org.bmsource.dwh.common.importer.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.fileManager.FileManager;
import org.bmsource.dwh.common.io.DataRow;
import org.bmsource.dwh.common.io.reader.ExcelBeanReader;
import org.bmsource.dwh.common.io.reader.ExcelRowValidator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Component
@StepScope
public class ExcelItemReader<Fact extends BaseFact> implements ItemStreamReader<DataRow<Fact>> {

    private Logger logger = LoggerFactory.getLogger(ExcelItemReader.class.getName());

    private int rowsCount = -1;

    private Iterator<Row> rowIterator;

    private ExcelBeanReader<Fact> excelReader;

    @Autowired
    @Qualifier("fact")
    private Fact fact;

    @Value("#{jobParameters['mapping']}")
    private String mappingValue;

    @Value("#{jobParameters['transaction']}")
    private String transaction;

    @Value("#{stepExecutionContext['fileName']}")
    private String fileName;

    @Autowired
    private FileManager fileManager;

    private HashMap<String, String> mapping;

    private ExcelRowValidator<Fact> validator;

    @Override
    public void open(@NotNull ExecutionContext executionContext) throws ItemStreamException {
        try {
            InputStream inputStream = fileManager.getStream(transaction, fileName);
            logger.debug("Excel file {} opened for reading", this.fileName);

            mapping = new ObjectMapper().readValue(mappingValue, new TypeReference<HashMap<String, String>>() {
            });
            excelReader = new ExcelBeanReader<>(inputStream, (Class<Fact>) fact.getClass(), mapping);
            validator = new ExcelRowValidator<>(mapping);

            executionContext.put(ImportContext.totalRowsKey, excelReader.getTotalRowsCount());
            executionContext.put(ImportContext.rowsKey, 0);
            List<String> columns = excelReader.getHeader();
            executionContext.put(ImportContext.headerKey, String.join(",", columns));
        } catch (Throwable t) {
            t.printStackTrace();
            try {
                excelReader.close();
            } catch (IOException ex) {
                throw new ItemStreamException(t);
            }
            throw new ItemStreamException(t);
        }
    }

    @Override
    public DataRow<Fact> read() {
        DataRow<Fact> dataRow = excelReader.nextValidatedRow();
        if (dataRow != null) {
            logger.trace("Parsing errors Thread: {}, FileName: {}, Errors: {}, Bean: {}",
                Thread.currentThread().getName(),
                fileName,
                dataRow.getErrors(),
                dataRow.getFact(),
                dataRow.toString()
            );
            return dataRow;
        }
        return null;
    }

    @Override
    public void update(@NotNull ExecutionContext executionContext) throws ItemStreamException {
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