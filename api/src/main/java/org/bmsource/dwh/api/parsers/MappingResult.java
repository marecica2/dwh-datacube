package org.bmsource.dwh.api.parsers;

import java.util.List;

public class MappingResult {

  private final List<Object> headerRow;
  private final List<Object> previewRow;

  public MappingResult(List<Object> headerRow, List<Object> previewRow) {
    this.headerRow = headerRow;
    this.previewRow = previewRow;
  }

  public List<Object> getHeaderRow() {
    return headerRow;
  }

  public List<Object> getPreviewRow() {
    return previewRow;
  }
}
