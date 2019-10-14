package org.bmsource.dwh.common.importer.batch;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Configuration
@SpringBatchTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ImportJobBuilder.class})
class ImportJobBuilderIT {

    static Map<String, String> columnMapping = new HashMap<>();
    static {
        columnMapping.put("Service Type", "serviceType");
        columnMapping.put("Business Unit", "businessUnit");
        columnMapping.put("Supplier Name", "supplierName");
        columnMapping.put("S. No.", "transactionId");
        columnMapping.put("Origin-City", "originCity");
        columnMapping.put("Zone", "zone");
    }

    @Autowired
    JobLauncherTestUtils jobUtils;

    @Autowired
    private ImportJobBuilder<Fact> jobBuilder;

    @Bean
    public JobExecutionListenerSupport listener() {
        return new JobExecutionListenerSupport() {
            private final Logger log = LoggerFactory.getLogger(ImportJobBuilderIT.class);

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    log.info("Job finished successfully");
                }
            }
        };
    }

    @Bean
    Job importJob() throws Exception {
        File file = ResourceUtils.getFile(this.getClass().getResource("/spends.xlsx"));
        return jobBuilder.factClass(Fact.class)
            .fromStream(FileUtils.openInputStream(file))
            .listener(listener())
            .mapping(columnMapping)
            .build();
    }

    @Test
    void testJob() throws Exception {
        JobExecution jobExecution = jobUtils.launchJob();
        Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}
