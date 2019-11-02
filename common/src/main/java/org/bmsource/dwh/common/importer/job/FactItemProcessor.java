package org.bmsource.dwh.common.importer.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.io.reader.ExcelRowMapper;
import org.bmsource.dwh.common.io.DataRow;
import org.bmsource.dwh.common.io.reader.ExcelRowValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@StepScope
@Component
public class FactItemProcessor<Fact extends BaseFact> implements ItemProcessor<List<Object>, ImportItem<Fact>>, ImportContext {

    private static final Logger logger = LoggerFactory.getLogger(FactItemProcessor.class);

    @Value("#{jobParameters['mapping']}")
    private String mappingString;

    @Value("#{stepExecutionContext['fileName']}")
    private String fileName;

    private Map<String, String> mapping;

    private StepExecution stepExecution;

    private ExcelRowMapper<Fact> rowMapper;

    @Autowired
    @Qualifier("fact")
    private Fact fact;

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        try {
            String mappingString = stepExecution.getJobExecution().getJobParameters().getString(ImportContext.mappingKey);
            mapping = new ObjectMapper().readValue(mappingString, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ImportItem<Fact> process(final List<Object> row) {
        if (rowMapper == null) {
            String[] header = ((String) stepExecution.getExecutionContext().get(headerKey)).split(",");
            rowMapper = new ExcelRowMapper<>((Class<Fact>)fact.getClass(), Arrays.asList(header), mapping);
        }
        Fact fact = rowMapper.map(row);
        ExcelRowValidator<Fact> validator = new ExcelRowValidator(mapping);
        Map<String, List<String>> validationErrors = validator.getValidationErrors(fact);
        DataRow excelRow = DataRow
            .builder()
            .row(row)
            .errors(validationErrors)
            .build();
        logger.debug("XXXXX Parsing errors Thread: {}, FileName: {}, Errors: {}, Bean: {}",
            Thread.currentThread().getName(),
            fileName,
            validationErrors,
            fact.toString()
        );
        return new ImportItem<>(fact, excelRow);
    }

}