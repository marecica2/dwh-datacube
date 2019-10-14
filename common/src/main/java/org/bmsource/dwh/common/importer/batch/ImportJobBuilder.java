package org.bmsource.dwh.common.importer.batch;

import org.bmsource.dwh.common.AbstractFact;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan
public class ImportJobBuilder<Fact extends AbstractFact> {

    @Autowired
    DataSource dataSource;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    private InputStream inputStream;

    private JobExecutionListenerSupport listener;

    private Map<String, String> mapping;

    private Class<Fact> factClass;

    public ImportJobBuilder fromStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public ImportJobBuilder listener(JobExecutionListenerSupport listener) {
        this.listener = listener;
        return this;
    }

    public ImportJobBuilder mapping(Map<String, String> mapping) {
        this.mapping = mapping;
        return this;
    }

    public ImportJobBuilder factClass(Class<Fact> clazz) {
        this.factClass = clazz;
        return this;
    }

    public Job build() throws Exception {
        ExcelItemReader reader = new ExcelItemReader(inputStream);

        JdbcBatchItemWriter<Fact> writer = new JdbcBatchItemWriterBuilder<Fact>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql(factClass.newInstance().insertSQL())
            .dataSource(dataSource)
            .build();
        writer.afterPropertiesSet();

        Step step = stepBuilderFactory.get("step1")
            .<List<Object>, Fact>chunk(10)
            .reader(reader)
            .processor(new FactItemProcessor<>(factClass, mapping))
            .writer(writer)
            .build();

        return jobBuilderFactory.get("importExcelJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step)
            .end()
            .build();
    }
}
