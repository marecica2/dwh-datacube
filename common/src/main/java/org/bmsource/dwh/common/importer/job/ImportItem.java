package org.bmsource.dwh.common.importer.job;

import org.bmsource.dwh.common.io.DataRow;

public class ImportItem<Fact> {
    private Fact fact;
    private DataRow excelRow;

    public ImportItem(Fact fact, DataRow excelRow) {
        this.fact = fact;
        this.excelRow = excelRow;
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    public DataRow getExcelRow() {
        return excelRow;
    }

    public void setExcelRow(DataRow excelRow) {
        this.excelRow = excelRow;
    }
}
