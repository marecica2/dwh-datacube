package org.bmsource.dwh.common.job.step;

import org.bmsource.dwh.common.filemanager.FileManager;
import org.bmsource.dwh.common.job.ImportContext;
import org.bmsource.dwh.common.job.ImportJobConfiguration;
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
public class CleanUpTasklet<RawFact> implements Tasklet {

    @Value("#{jobParameters['tenant']}")
    private String tenant;

    @Value("#{jobParameters['project']}")
    private String project;

    private ImportJobConfiguration<RawFact, ?> importJobConfiguration;

    @Autowired
    public void setImportJobConfiguration(ImportJobConfiguration<RawFact, ?> importJobConfiguration) {
        this.importJobConfiguration = importJobConfiguration;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        if(tenant == null || project == null) {
            return null;
        }
        ImportContext ctx = new ImportContext();
        ctx.setProjectId(project);
        ctx.setTenant(tenant);
        importJobConfiguration.performCleanUp(ctx);
        return RepeatStatus.FINISHED;
    }
}
