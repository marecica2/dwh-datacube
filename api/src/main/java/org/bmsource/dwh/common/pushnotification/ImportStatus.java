package org.bmsource.dwh.common.pushnotification;

import java.io.Serializable;

public class ImportStatus implements Serializable {

    private static final long serialVersionUID = -6926932815233075496L;

    private boolean running = false;

    private Integer files = null;

    private Integer currentFile = null;

    private String currentFileName = null;

    private Integer currentRows = null;

    private Integer totalRows = null;

    public ImportStatus() {}

    public ImportStatus(boolean running) {
        this.running = running;
    }

    public ImportStatus(boolean running, Integer files, Integer currentFile, String currentFileName, Integer currentRows, Integer totalRows) {
        this.running = running;
        this.files = files;
        this.currentFile = currentFile;
        this.currentFileName = currentFileName;
        this.currentRows = currentRows;
        this.totalRows = totalRows;
    }

    public boolean isRunning() {
        return running;
    }

    public Integer getFiles() {
        return files;
    }

    public Integer getCurrentFile() {
        return currentFile;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public Integer getCurrentRows() {
        return currentRows;
    }

    public Integer getTotalRows() {
        return totalRows;
    }
}
