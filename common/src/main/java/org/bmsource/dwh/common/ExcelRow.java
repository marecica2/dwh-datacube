package org.bmsource.dwh.common;

import java.util.List;
import java.util.Map;

public class ExcelRow {
    private List<Object> row;
    private Map<String, List<String>> errors;

    public List<Object> getRow() {
        return row;
    }

    public void setRow(List<Object> row) {
        this.row = row;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public static ExcelRowBuilder builder() {
        return new ExcelRowBuilder();
    }

    public static final class ExcelRowBuilder {
        private List<Object> row;
        private Map<String, List<String>> errors;

        private ExcelRowBuilder() {
        }

        public ExcelRowBuilder row(List<Object> row) {
            this.row = row;
            return this;
        }

        public ExcelRowBuilder errors(Map<String, List<String>> errors) {
            this.errors = errors;
            return this;
        }

        public ExcelRow build() {
            ExcelRow excelRow = new ExcelRow();
            excelRow.setRow(row);
            excelRow.setErrors(errors);
            return excelRow;
        }
    }
}
