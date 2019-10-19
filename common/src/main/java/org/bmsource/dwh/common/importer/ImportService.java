package org.bmsource.dwh.common.importer;

import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.importer.batch.ImportJobConfiguration;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ComponentScan
public class ImportService {

    @Autowired
    ImportJobConfiguration importJobConfiguration;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job importJob;

    public void runImport(String tenant, String project, String transaction, List<String> files,
                          Map<String, String> columnMapping) {
        try {
            JobParameters params = new JobParametersBuilder()
                .addString("tenant", tenant)
                .addString("project", project)
                .addString("transaction", transaction)
                .addString("files", String.join(",", files))
                .addString("mapping", new JSONObject(columnMapping).toString())
                .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(importJob, params);
        } catch (JobParametersInvalidException | JobInstanceAlreadyCompleteException | JobRestartException | JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        }
    }
}
