package org.bmsource.dwh.common.job;

import org.bmsource.dwh.common.appstate.AppStateService;
import org.bmsource.dwh.common.job.step.ZipErrorsTasklet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan
public class JobConfiguration {

    private Logger logger = LoggerFactory.getLogger(JobConfiguration.class);

    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private JobExecutionListener jobListener;

    @Autowired(required = false)
    public void setJobListener(JobExecutionListener jobListener) {
        this.jobListener = jobListener;
    }

    private ZipErrorsTasklet zipErrorsTasklet;

    @Autowired
    public void setZipErrorsTasklet(ZipErrorsTasklet zipErrorsTasklet) {
        this.zipErrorsTasklet = zipErrorsTasklet;
    }

    private Step excelStep;

    @Autowired
    @Qualifier("excelStep")
    public void setExcelStep(Step excelStep) {
        this.excelStep = excelStep;
    }

    @Bean
    public Step postImportStep() {
        return this.stepBuilderFactory.get("cleanupStep")
            .tasklet(zipErrorsTasklet)
            .build();
    }

    private Step normalizerStep;

    @Autowired
    @Qualifier("normalizerStep")
    public void setNormalizerStep(Step step) {
        this.normalizerStep = step;
    }

    @Bean
    public Job importJob(@Autowired JobBuilderFactory jobBuilderFactory) {
        SimpleJobBuilder jobBuilder = jobBuilderFactory
            .get(JobConstants.jobName)
            .start(excelStep)
            .next(postImportStep())
            .next(normalizerStep);

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
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setDatabaseType("POSTGRES");
        factory.setTransactionManager(new ResourcelessTransactionManager());
        factory.setSerializer(new Jackson2ExecutionContextStringSerializer());
        factory.setTablePrefix("batch_");
        JobRepository jobRepository = null;
        try {
            jobRepository = factory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobRepository;
    }
}
