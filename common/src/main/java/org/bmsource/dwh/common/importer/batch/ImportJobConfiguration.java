package org.bmsource.dwh.common.importer.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan
public class ImportJobConfiguration<Fact extends BaseFact> {

    private DataSource dataSource;

    private StepBuilderFactory stepBuilderFactory;

    private ImportPartitioner partitioner;

    private JobExecutionListener jobListener;

    private ItemWriteListener writeListener;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setPartitioner(ImportPartitioner partitioner) {
        this.partitioner = partitioner;
    }

    @Autowired(required = false)
    public void setJobListener(JobExecutionListener jobListener) {
        this.jobListener = jobListener;
    }

    @Autowired(required = false)
    public void setItemWriteListener(ItemWriteListener writeListener) {
        this.writeListener = writeListener;
    }

    @Autowired
    public void setProcessor(FactItemProcessor<Fact> processor) {
        this.processor = processor;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Autowired
    private FactItemProcessor<Fact> processor;

    @Autowired
    @Qualifier("sample")
    private Fact fact;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }


    @Autowired
    private JdbcTemplate template;


    @Bean
    JdbcBatchItemWriter<Fact> writer() {
        JdbcBatchItemWriter<Fact> writer = null;
        try {
            writer = new JdbcBatchItemWriterBuilder<Fact>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(fact.getClass().newInstance().insertSQL())
                .dataSource(dataSource)
                .build();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return writer;
    }

    @Bean
    public ExcelItemReader reader() {
        return new ExcelItemReader();
    }

    @Bean
    public Step partitionStep() {
        return stepBuilderFactory.get("partitionStep")
            .partitioner("slaveStep", partitioner)
            .step(slaveStep())
            .taskExecutor(taskExecutor())
            .build();
    }

    @Bean
    public Step slaveStep() {
        SimpleStepBuilder<List<Object>, Fact> slaveStep = stepBuilderFactory.get("slaveStep")
            .<List<Object>, Fact>chunk(5000)
            .reader(reader())
            .processor(processor)
            .writer(writer());
        if(writeListener != null) {
            slaveStep.listener(writeListener);
        }
        return slaveStep
            .build();
    }

    @Bean
    public Job createJob(@Autowired JobBuilderFactory jobBuilderFactory) {
        SimpleJobBuilder jobBuilder = jobBuilderFactory.get("importJob")
            .incrementer(new RunIdIncrementer())
            .start(partitionStep());
        if (jobListener != null) {
            jobBuilder = jobBuilder
                .listener(jobListener);
        }
        return jobBuilder.build();
    }
}
