package org.bmsource.dwh.common.importer.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.reader.FactModelMapper;
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
public class FactItemProcessor<Fact extends BaseFact> implements ItemProcessor<List<Object>, Fact>, ImportContext {

    private static final Logger log = LoggerFactory.getLogger(FactItemProcessor.class);

    @Value("#{jobParameters['mapping']}")
    private String mappingString;

    private Map<String, String> mapping;

    private StepExecution stepExecution;

    private FactModelMapper<Fact> rowMapper;

    private Fact fact;

    @Autowired
    @Qualifier("fact")
    public void setFact(Fact fact) {
        this.fact = fact;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        try {
            String mappingString = stepExecution.getJobExecution().getJobParameters().getString("mapping");
            this.mapping = new ObjectMapper().readValue(mappingString, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Fact process(final List<Object> row) {
        if (rowMapper == null) {
            String[] header = ((String) stepExecution.getExecutionContext().get(headerKey)).split(",");
            rowMapper = new FactModelMapper<>((Class<Fact>)fact.getClass(), Arrays.asList(header), mapping);
        }
        Fact fact = rowMapper.mapRow(row);
        return fact;
    }

}