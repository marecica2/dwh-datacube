package org.bmsource.dwh.common.importer;

import org.bmsource.dwh.common.appstate.AppStateService;
import org.bmsource.dwh.common.appstate.EnableImportEvents;
import org.bmsource.dwh.common.importer.batch.ImportJobConfiguration;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@ComponentScan
@EnableImportEvents
public class ImportService {

    ImportJobConfiguration importJobConfiguration;

    JobLauncher jobLauncher;

    Job importJob;

    AppStateService appStateService;

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

    @Bean
    public List<String> channels() {
        return new ArrayList<String>() {{
            add("importStatus");
        }};
    }

    ;

    public void runImport(String tenant, String project, String transaction, List<String> files,
                          Map<String, String> columnMapping) {
        try {
            JobParameters params = new JobParametersBuilder()
                .addString("uid", UUID.randomUUID().toString())
                .addString("tenant", tenant)
                .addString("project", project)
                .addString("transaction", transaction)
                .addString("files", String.join(",", files))
                .addString("mapping", new JSONObject(columnMapping).toString())
                .toJobParameters();
            jobLauncher.run(importJob, params);
        } catch (JobParametersInvalidException | JobInstanceAlreadyCompleteException | JobRestartException | JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        }
    }
}
