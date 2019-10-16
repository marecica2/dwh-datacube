package org.bmsource.dwh.common.importer.batch;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan
public class ImportJobConfiguration<Fact extends BaseFact> {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ImportPartitioner partitioner;


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
        taskExecutor.setQueueCapacity(2);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
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
            .taskExecutor(taskExecutor())
            .build();
    }

    @Bean
    public Step slaveStep() {
        return stepBuilderFactory.get("slaveStep")
            .<List<Object>, Fact>chunk(10)
            .reader(reader())
            .processor(processor)
            .writer(writer())
            .listener(listener())
            .build();
    }

    @Bean
    public ItemWriteListener listener() {
        return new ItemWriteListener() {

            @Override
            public void beforeWrite(List items) {

            }

            @Override
            public void afterWrite(List items) {
                items.forEach(item -> {
                    System.out.println(item);
                });
            }

            @Override
            public void onWriteError(Exception exception, List items) {

            }
        };
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("importJob")
            .incrementer(new RunIdIncrementer())
            .start(partitionStep())
            .build();
    }
}
