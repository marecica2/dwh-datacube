package org.bmsource.dwh.common.pushnotification;

import java.io.Serializable;

public class AppState implements Serializable {

    private static final long serialVersionUID = -1305843684463073641L;

    private ImportStatus importStatus;

    public AppState() {}

    public AppState(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }
}
