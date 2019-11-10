package org.bmsource.dwh.common.importer;

import org.bmsource.dwh.common.appstate.AppStateService;
import org.bmsource.dwh.common.appstate.EnableImportEvents;
import org.bmsource.dwh.common.importer.job.ImportJobConfiguration;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@ComponentScan
@EnableImportEvents
public class ImportService {

    ImportJobConfiguration importJobConfiguration;

    JobLauncher jobLauncher;

    JobExplorer jobExplorer;

    Job importJob;

    AppStateService appStateService;

    ImportJobRepository repository;

    @Autowired
    public void setImportJobConfiguration(ImportJobConfiguration importJobConfiguration) {
        this.importJobConfiguration = importJobConfiguration;
    }

    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Autowired
    public void setImportJob(Job importJob) {
        this.importJob = importJob;
    }

    @Autowired
    public void setAppStateService(AppStateService appStateService) {
        this.appStateService = appStateService;
    }

    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }

    @Autowired
    public void setRepository(ImportJobRepository repository) {
        this.repository = repository;
    }

    @Bean
    public List<String> channels() {
        return new ArrayList<String>() {{
            add("importStatus");
        }};
    }

    @Async("asyncExecutor")
    public void runImport(String tenant, String project, String transaction, List<String> files,
                          Map<String, String> columnMapping) {
        try {
            JobParameters params = new JobParametersBuilder()
                .addString("tenant", tenant)
                .addString("project", project)
                .addString("tenantProject", tenant + ":" + project)
                .addString("transaction", transaction)
                .addString("files", String.join(",", files))
                .addString("mapping", new JSONObject(columnMapping).toString())
                .toJobParameters();
            jobLauncher.run(importJob, params);
        } catch (JobParametersInvalidException | JobInstanceAlreadyCompleteException | JobRestartException | JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getStatistics(String tenant, String project) {
        Long id = repository.getJobExecutionId(tenant, project);
        if (id != null) {
            JobExecution jobExecution = jobExplorer.getJobExecution(id);
            Map<String, Object> params = jobExecution.getExecutionContext()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            params.put("status", jobExecution.getStatus());
            params.put("startTime", jobExecution.getStartTime());
            params.put("endTime", jobExecution.getEndTime());
            params.put("duration",
                jobExecution.getEndTime() != null ?
                    jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime() : 0);
            return params;
        }
        return null;
    }
}
