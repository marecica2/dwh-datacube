package org.bmsource.dwh.api.parsers;

import java.util.List;

public class ColumnMapping {

  List<Object> headerColumn;

  List<Object> previewColumn;

  public ColumnMapping(List<Object> headerColumn, List<Object> previewColumn) {
    this.headerColumn = headerColumn;
    this.previewColumn = previewColumn;
  }

  public List<Object> getHeaderColumn() {
    return headerColumn;
  }

  public void setHeaderColumn(List<Object> headerColumn) {
    this.headerColumn = headerColumn;
  }

  public List<Object> getPreviewColumn() {
    return previewColumn;
  }

  public void setPreviewColumn(List<Object> previewColumn) {
    this.previewColumn = previewColumn;
  }
}
