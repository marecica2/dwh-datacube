package org.bmsource.dwh.common.importer.batch;

import org.bmsource.dwh.common.BaseFact;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

    private FactItemProcessor<Fact> processor;

    private Fact fact;

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

    @Autowired
    @Qualifier("fact")
    public void setFact(Fact fact) {
        this.fact = fact;
    }

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
            //.partitionHandler(partitionHandler())
            .build();
    }

//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setMaxPoolSize(1);
//        taskExecutor.setCorePoolSize(1);
//        taskExecutor.setQueueCapacity(1);
//        taskExecutor.afterPropertiesSet();
//        return taskExecutor;
//    }
//    @Bean
//    public PartitionHandler partitionHandler() {
//        TaskExecutorPartitionHandler retVal = new TaskExecutorPartitionHandler();
//        retVal.setTaskExecutor(taskExecutor());
//        retVal.setStep(slaveStep());
//        retVal.setGridSize(10);
//        return retVal;
//    }

    @Bean
    public Step slaveStep() {
        SimpleStepBuilder<List<Object>, Fact> slaveStep = stepBuilderFactory.get("slaveStep")
            .<List<Object>, Fact>chunk(100)
            .reader(reader())
            .processor(processor)
            .writer(writer());
        if (writeListener != null) {
            slaveStep.listener(writeListener);
        }
        return slaveStep
            .build();
    }

    @Bean
    public Job importJob(@Autowired JobBuilderFactory jobBuilderFactory) {
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
