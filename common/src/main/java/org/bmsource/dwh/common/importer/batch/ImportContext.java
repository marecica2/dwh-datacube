package org.bmsource.dwh.common.importer.batch;

import org.springframework.batch.item.ExecutionContext;

public interface ImportContext {
    String headerKey = "header";
    String totalRowsKey = "totalRows";
    String rowsKey = "rows";
    String fileNameKey = "fileName";
    String filesKey = "files";
    String tenantKey = "tenant";
    String projectKey = "project";

    default void saveToContext(ExecutionContext executionContext, String key, Object value) {
        executionContext.put(key, value);
    }

    default Object getFromContext(ExecutionContext executionContext, String key) {
        return executionContext.get(key);
    }
}
