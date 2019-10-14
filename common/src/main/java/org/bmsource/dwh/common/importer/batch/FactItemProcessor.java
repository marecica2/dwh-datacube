package org.bmsource.dwh.common.importer.batch;

import org.bmsource.dwh.common.reader.FactModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.Map;

public class FactItemProcessor<Fact> implements ItemProcessor<List<Object>, Fact>, ImportContext {

    private static final Logger log = LoggerFactory.getLogger(FactItemProcessor.class);

    private Class<Fact> factClass;

    private Map<String, String> mapping;

    private StepExecution stepExecution;

    private FactModelMapper<Fact> rowMapper;

    public FactItemProcessor(Class<Fact> factClass, Map<String, String> mapping) {
        this.factClass = factClass;
        this.mapping = mapping;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public Fact process(final List<Object> row) {
        if (rowMapper == null) {
            List<Object> header = (List<Object>) getFromContext(stepExecution.getExecutionContext(), headerKey);
            rowMapper = new FactModelMapper<>(factClass, header, mapping);
        }
        Fact fact = rowMapper.mapRow(row);
        System.out.println(fact);
        return fact;
    }

}