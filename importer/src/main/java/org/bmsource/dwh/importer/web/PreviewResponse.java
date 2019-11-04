package org.bmsource.dwh.importer.web;

import org.bmsource.dwh.importer.Fact;

import java.util.List;
import java.util.Map;

public class PreviewResponse {
    private Fact entity;
    private boolean valid;
    private Map<String, List<String>> errors;

    public PreviewResponse(Fact entity, Map<String, List<String>> errors, boolean valid) {
        this.entity = entity;
        this.errors = errors;
        this.valid = valid;
    }

    public Fact getEntity() {
        return entity;
    }

    public void setEntity(Fact entity) {
        this.entity = entity;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
