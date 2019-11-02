package org.bmsource.dwh.common.importer.job;

import org.bmsource.dwh.common.excel.ExcelRow;

public class ImportItem<Fact> {
    private Fact fact;
    private ExcelRow excelRow;

    public ImportItem(Fact fact, ExcelRow excelRow) {
        this.fact = fact;
        this.excelRow = excelRow;
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    public ExcelRow getExcelRow() {
        return excelRow;
    }

    public void setExcelRow(ExcelRow excelRow) {
        this.excelRow = excelRow;
    }
}
