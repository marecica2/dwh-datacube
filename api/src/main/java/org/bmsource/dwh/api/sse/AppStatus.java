package org.bmsource.dwh.api.sse;

public class AppStatus {

    private ImportStatus importStatus;

    public AppStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }
}