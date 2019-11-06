package org.bmsource.dwh.common.importer.job;

import org.bmsource.dwh.common.fileManager.FileManager;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AfterImportTasklet implements Tasklet {

    @Autowired
    FileManager fileManager;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext context = getJobContext(chunkContext);
        System.out.println(context.get(ImportContext.skippedRowsKey));
        return RepeatStatus.FINISHED;
    }

    private ExecutionContext getJobContext(ChunkContext chunkContext) {
        return chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
    }
}
