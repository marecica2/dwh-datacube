package org.bmsource.dwh.common.io;

import java.util.List;
import java.util.Map;

public class DataRow {
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

    public static DataRowBuilder builder() {
        return new DataRowBuilder();
    }

    public static final class DataRowBuilder {
        private List<Object> row;
        private Map<String, List<String>> errors;

        private DataRowBuilder() {
        }

        public DataRowBuilder row(List<Object> row) {
            this.row = row;
            return this;
        }

        public DataRowBuilder errors(Map<String, List<String>> errors) {
            this.errors = errors;
            return this;
        }

        public DataRow build() {
            DataRow excelRow = new DataRow();
            excelRow.setRow(row);
            excelRow.setErrors(errors);
            return excelRow;
        }
    }
}
