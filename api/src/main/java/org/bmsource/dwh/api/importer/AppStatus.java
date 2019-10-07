package org.bmsource.dwh.api.importer;

public class AppStatus {

    private ImportStatus importStatus;

    public AppStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }
}
