package org.bmsource.dwh.common.importer.job;

public interface ImportContext {
    String transactionKey = "transaction";
    String headerKey = "header";
    String mappingKey = "mapping";
    String totalRowsKey = "totalRows";
    String rowsKey = "rows";
    String skippedRowsKey = "skippedRows";
    String fileNameKey = "fileName";
    String filesKey = "files";
    String tenantKey = "tenant";
    String projectKey = "project";
}
