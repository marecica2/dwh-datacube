package org.bmsource.dwh.common.importer.job;

import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.excel.ExcelRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@StepScope
public class CompositeImportItemWriter<Fact extends BaseFact> implements ItemStreamWriter<ImportItem<Fact>> {

    private static Logger logger = LoggerFactory.getLogger(CompositeImportItemWriter.class);

    @Value("#{stepExecutionContext['fileName']}")
    private String fileName;

    @Autowired
    private JdbcBatchItemWriter<Fact> jdbcBatchItemWriter;

    @Autowired
    private ExcelItemWriter excelItemWriter;

    @Override
    public void write(List<? extends ImportItem<Fact>> items) throws Exception {

        List<Fact> validItems = new LinkedList<>();
        List<ExcelRow> errorRows = new LinkedList<>();
        for (ImportItem<Fact> item : items) {
            Map<String, List<String>> errors = item.getExcelRow().getErrors();
            if (errors == null || errors.isEmpty()) {
                validItems.add(item.getFact());
            } else {
                errorRows.add(item.getExcelRow());
            }
        }
        logger.trace("Thread {} File: {}, Items count: {}, Valid items: {}, Invalid rows: {}",
            Thread.currentThread().getName(),
            fileName,
            items.size(),
            validItems.size(),
            errorRows.size()
        );

        jdbcBatchItemWriter.write(validItems);
        excelItemWriter.write(errorRows);
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        excelItemWriter.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        excelItemWriter.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        excelItemWriter.close();
    }
}
