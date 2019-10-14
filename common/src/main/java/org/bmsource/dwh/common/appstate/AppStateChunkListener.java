package org.bmsource.dwh.common.appstate;

import org.bmsource.dwh.common.appstate.AppStateService;
import org.bmsource.dwh.common.importer.batch.ImportContext;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppStateChunkListener implements ChunkListener, ImportContext {

    @Autowired
    AppStateService appStateService;

    @Override
    public void beforeChunk(ChunkContext context) {
    }

    @Override
    public void afterChunk(ChunkContext context) {
        int count = context.getStepContext().getStepExecution().getReadCount();
        ExecutionContext ec = context.getStepContext().getStepExecution().getExecutionContext();
        String tenant = (String) getFromContext(ec, ImportContext.tenantKey);
        String project = (String) getFromContext(ec, ImportContext.projectKey);
        String file = (String) getFromContext(ec, ImportContext.fileNameKey);
        List<String> files = (List<String>) getFromContext(ec, ImportContext.filesKey);

        Map<String, Object> state = new HashMap<>();
        state.put("running", true);
        state.put("file", files.indexOf(file) + 1);
        state.put("files", files.size());
        state.put("fileName", file);
        state.put("rowsCount", count);
        state.put("totalRowsCount", getFromContext(ec, ImportContext.totalRowsKey));
        appStateService.updateState(tenant, project, "importStatus", state);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
    }
}
