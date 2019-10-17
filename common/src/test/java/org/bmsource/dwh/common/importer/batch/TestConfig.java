package org.bmsource.dwh.common.importer.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
class TestConfig {

    @Autowired
    JdbcTemplate template;

    @Bean
    public JobExecutionListener jobListener() {
        JobExecutionListener listener = new JobExecutionListenerSupport() {

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    Integer count = template.queryForObject("SELECT count(*) FROM FACT", Integer.class);
                    System.out.println("Job imported successfully " + count + " rows");
                }
            }
        };
        return listener;
    }

    @Bean
    public ItemWriteListener writeListener() {
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
}
