package org.bmsource.dwh.api.parsers;

import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

public class ColumnHeaderHandler implements SheetContentsHandler {

  private final int minColumns;
  private int currentRow = -1;
  private int currentCol = -1;

  private List<List<Object>> rowsContainer = new LinkedList<>();
  private List<Object> currentRowContainer;

  ColumnHeaderHandler(int minColumns) {
    this.minColumns = minColumns;
  }

  @Override
  public void startRow(int rowNum) {
    System.out.println("Start reading row " + rowNum);
    currentRowContainer = new LinkedList<>();
    rowsContainer.add(currentRowContainer);
  }

  @Override
  public void endRow(int rowNum) {
    System.out.println("Ending row " + rowNum);
  }

  @Override
  public void cell(String cellReference, String formattedValue, XSSFComment comment) {
    if (cellReference == null) {
      cellReference = new CellAddress(currentRow, currentCol).formatAsString();
    }
    // Did we miss any cells?
    int thisCol = (new CellReference(cellReference)).getCol();
    int missedCols = thisCol - currentCol - 1;
    for (int i = 0; i < missedCols; i++) {
      currentRowContainer.add(null);
    }

    currentCol = thisCol;
    currentRowContainer.add(formattedValue);
  }

  public ColumnMapping getColumnMapping() {
    return new ColumnMapping(rowsContainer.get(0), rowsContainer.get(1));
  }

}
