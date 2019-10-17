package org.bmsource.dwh.common.importer.batch;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@Configuration
@SpringBatchTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, ImportJobConfiguration.class})
class ImportJobConfigurationIT {

    static List<String> files = new ArrayList<>(
        Arrays.asList("/spends.xlsx", "/spends2.xlsx", "/spends3.xlsx"));

    static Map<String, String> columnMapping = new LinkedHashMap<>();

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
    ImportJobConfiguration job;

    @Test
    public void testJob() throws Exception {
        JobParametersBuilder params = new JobParametersBuilder();
        params.addString("tenant", "000000-00000-00001");
        params.addString("project", "1");
        params.addString("transaction", "123456789");
        params.addString("files", String.join(",", files));
        params.addString("mapping", new JSONObject(columnMapping).toString());
        JobExecution jobExecution = jobUtils.launchJob(params.toJobParameters());
        Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}
