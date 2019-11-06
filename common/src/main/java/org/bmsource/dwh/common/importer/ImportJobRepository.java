package org.bmsource.dwh.common.importer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class ImportJobRepository {

    @Autowired
    JdbcTemplate template;

    public Long getJobExecutionId(String tenant, String project) {
        String sql = "select job_execution_id\n" +
            "from batch_job_execution_params\n" +
            "         join batch_job_execution bje using (job_execution_id)\n" +
            "         join batch_job_instance bji using (job_instance_id)\n" +
            "where key_name = 'tenantProject'\n" +
            "  and string_val = ?\n" +
            "  and bji.job_name = 'importJob'\n" +
            "order by job_execution_id desc\n" +
            "limit 1";
        return template.queryForObject(sql, Long.class, tenant + ":" + project);
    }
}
