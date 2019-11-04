package org.bmsource.dwh.common.importer.job;

import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.appstate.AppStateService;
import org.bmsource.dwh.common.io.DataRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan
public class ImportJobConfiguration<Fact extends BaseFact> {

    private Logger logger = LoggerFactory.getLogger(ImportJobConfiguration.class);

    private static final int BATCH_SIZE = 50;

    private static final int MAX_CONCURRENT_FILES = 10;

    private Fact fact;

    private DataSource dataSource;

    private StepBuilderFactory stepBuilderFactory;

    private ImportPartitioner excelImportPartitioner;

    private JobExecutionListener jobListener;

    private ItemWriteListener<Fact> writeListener;

    private ChunkListener chunkListener;

    private AppStateService appStateService;

    private ExcelItemReader<Fact> reader;

    private CompositeImportItemWriter<Fact> compositeItemWriter;

    @Autowired
    @Qualifier("fact")
    public void setFact(Fact fact) {
        this.fact = fact;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Autowired
    public void setExcelImportPartitioner(ImportPartitioner excelImportPartitioner) {
        this.excelImportPartitioner = excelImportPartitioner;
    }

    @Autowired(required = false)
    public void setJobListener(JobExecutionListener jobListener) {
        this.jobListener = jobListener;
    }

    @Autowired(required = false)
    public void setWriteListener(ItemWriteListener<Fact> writeListener) {
        this.writeListener = writeListener;
    }

    @Autowired(required = false)
    public void setChunkListener(ChunkListener chunkListener) {
        this.chunkListener = chunkListener;
    }

    @Autowired
    public void setAppStateService(AppStateService appStateService) {
        this.appStateService = appStateService;
    }

    @Autowired
    public void setReader(ExcelItemReader<Fact> reader) {
        this.reader = reader;
    }

    @Autowired
    public void setCompositeItemWriter(CompositeImportItemWriter<Fact> compositeItemWriter) {
        this.compositeItemWriter = compositeItemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<Fact> jdbcWriter() {
        JdbcBatchItemWriter<Fact> jdbcWriter = new JdbcBatchItemWriterBuilder<Fact>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql(writeSql())
            .dataSource(dataSource)
            .build();
        jdbcWriter.afterPropertiesSet();
        return jdbcWriter;
    }

    @Bean
    public Step excelReadPartitionStep() {
        return stepBuilderFactory.get("excelReadPartitionStep")
            .partitioner("excelReadPartitioner", excelImportPartitioner)
            .partitionHandler(excelReadPartitioner())
            .build();
    }

    @Bean
    public PartitionHandler excelReadPartitioner() {
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
        partitionHandler.setTaskExecutor(taskExecutor());
        partitionHandler.setStep(excelReadStep());
        partitionHandler.setGridSize(MAX_CONCURRENT_FILES);
        return partitionHandler;
    }

    @Bean
    public Step excelReadStep() {
        SimpleStepBuilder<DataRow<Fact>, DataRow<Fact>> step = stepBuilderFactory.get("excelReadStep")
            .<DataRow<Fact>, DataRow<Fact>>chunk(BATCH_SIZE)
            .reader(reader)
            .writer(compositeItemWriter);
        step.listener(promotionListener());
        if (writeListener != null) {
            step.listener(writeListener);
        }
        if (chunkListener != null) {
            step.listener(chunkListener);
        }
        return step
            .build();
    }

    @Bean
    public Job importJob(@Autowired JobBuilderFactory jobBuilderFactory) {
        SimpleJobBuilder jobBuilder = jobBuilderFactory.get("importJob")
            //.incrementer(new RunIdIncrementer())
            .start(excelReadPartitionStep());
        if (jobListener != null) {
            jobBuilder = jobBuilder
                .listener(jobListener);
        }
        return jobBuilder.build();
    }

    @Bean
    public JobExecutionListener jobListener() {
        return new JobExecutionListenerSupport() {

            @Autowired
            AppStateService appStateService;

            @Override
            public void beforeJob(JobExecution jobExecution) {
                if(jobExecution.getJobParameters().getParameters().size() == 0)
                    return;
                String tenant = jobExecution.getJobParameters().getString("tenant");
                String transaction = jobExecution.getJobParameters().getString("transaction");
                String project = jobExecution.getJobParameters().getString("project");
                List<String> files =
                    Arrays.asList((jobExecution.getJobParameters().getString("files")).split(","));
                Map<String, Object> state = new HashMap<>();

                logger.info("Import started for tenant {} project {} transaction {}", tenant, project, transaction);
                state.put("type", "importStatus");
                state.put("running", true);
                state.put("files", files);
                appStateService.updateState(tenant, project, "importStatus", state);
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if(jobExecution.getJobParameters().getParameters().size() == 0)
                    return;
                String tenant = jobExecution.getJobParameters().getString("tenant");
                String project = jobExecution.getJobParameters().getString("project");
                Map<String, Object> state = new HashMap<>();
                state.put("type", "importStatus");
                state.put("running", false);
                appStateService.updateState(tenant, project, "importStatus", state);
            }
        };
    }

    @Bean
    public ChunkListener chunkListener() {
        return new ChunkListener() {

            @Override
            public void beforeChunk(@Nonnull ChunkContext context) {
            }

            @Override
            public void afterChunk(@Nonnull ChunkContext context) {
                Map<String, Object> ec = context.getStepContext().getStepExecutionContext();
                int rows = context.getStepContext().getStepExecution().getWriteCount();
                int totalRows = (Integer) ec.get(ImportContext.totalRowsKey);
                int skippedRows = (Integer) ec.get(ImportContext.skippedRowsKey);

                String tenant = (String) context.getStepContext().getJobParameters().get(ImportContext.tenantKey);
                String project = (String) context.getStepContext().getJobParameters().get(ImportContext.projectKey);
                String file = (String) ec.get(ImportContext.fileNameKey);

                logger.info("Import {} {} of {} rows, skipped {}", file, rows, totalRows, skippedRows);

                Map<String, Object> state = new HashMap<>();
                state.put("type", "importStatusFile");
                state.put("running", true);
                state.put("fileName", file);
                state.put("rowsCount", rows);
                state.put("skippedRows", skippedRows);
                state.put("totalRowsCount", totalRows);
                appStateService.updateState(tenant, project, "importStatus", state);
            }

            @Override
            public void afterChunkError(@Nonnull ChunkContext context) {
            }
        };
    }

    @Bean
    public JobLauncher simpleJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(myJobRepository());
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Primary
    @Bean
    public JobRepository myJobRepository() {
        JobRepositoryFactoryBean jrfb = new JobRepositoryFactoryBean();
        jrfb.setDataSource(dataSource);
        jrfb.setDatabaseType("POSTGRES");
        jrfb.setTransactionManager(new ResourcelessTransactionManager());
        jrfb.setSerializer(new Jackson2ExecutionContextStringSerializer());
        jrfb.setTablePrefix("batch_");
        JobRepository jr = null;
        try {
            jr = jrfb.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jr;
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{ImportContext.skippedRowsKey, ImportContext.totalRowsKey});
        return listener;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("Partition_");
        taskExecutor.setMaxPoolSize(MAX_CONCURRENT_FILES);
        taskExecutor.setCorePoolSize(MAX_CONCURRENT_FILES / 2);
        taskExecutor.setQueueCapacity(MAX_CONCURRENT_FILES / 2);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    private String writeSql() {
        String sql = null;
        try {
            sql = fact.getClass().newInstance().insertSQL();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return sql;
    }
}
