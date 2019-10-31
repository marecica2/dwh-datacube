package org.bmsource.dwh.common.importer.batch;

public interface ImportContext {
    String headerKey = "header";
    String totalRowsKey = "totalRows";
    String rowsKey = "rows";
    String fileNameKey = "fileName";
    String filesKey = "files";
    String tenantKey = "tenant";
    String projectKey = "project";
}
