package org.bmsource.dwh.common.importer.job.importer;

import org.bmsource.dwh.common.filemanager.FileManager;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class ZipErrorsTasklet implements Tasklet {

    @Autowired
    FileManager fileManager;

    @Value("#{jobParameters['transaction']}")
    private String transaction;

    @Value("#{jobParameters['tenant']}")
    private String tenant;

    @Value("#{jobParameters['project']}")
    private String project;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if(transaction == null || tenant == null || project == null) {
            return null;
        }
        fileManager.exportErrors(transaction, tenant, project);
        fileManager.delete(transaction);
        return RepeatStatus.FINISHED;
    }
}
