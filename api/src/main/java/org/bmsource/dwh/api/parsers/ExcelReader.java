package org.bmsource.dwh.api.parsers;

import com.monitorjbl.xlsx.StreamingReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelReader implements DataReader {

  @Override
  public List<List<Object>> readContent(InputStream inputStream, int rowsToRead) throws Exception {
    List<List<Object>> rows = new LinkedList<>();
    try (
        Workbook workbook = StreamingReader.builder()
            .rowCacheSize(100)
            .bufferSize(4096)
            .open(inputStream)) {
      Sheet sheet = workbook.getSheetAt(0);
      Iterator<Row> rowIterator = sheet.rowIterator();

      int index = 0;
      while((rowIterator.hasNext()) && (index < rowsToRead || rowsToRead == -1)) {
        List<Object> rowContainer = new LinkedList<>();
        int prevCellIndex = 0;

        Row sheetRow = rowIterator.next();
        for (Cell sheetCell : sheetRow) {
          int currentIndex = sheetCell.getColumnIndex();
          fillGaps(rowContainer, prevCellIndex, currentIndex);
          rowContainer.add(sheetCell.getStringCellValue());
          prevCellIndex = currentIndex;
        }
        rows.add(rowContainer);
        index++;
      }
      return rows;
    }
  }

  @Override
  public MappingResult readHeaderRow(InputStream inputStream) throws Exception {
    List<List<Object>> result = readContent(inputStream, 2);
    return new MappingResult(result.get(0), result.get(1));
  }

  @Override
  public List<List<Object>> readPreview(InputStream inputStream, int numOfRows) throws Exception {
    List<List<Object>> result = readContent(inputStream, numOfRows + 1);
    result.remove(0);
    return result;
  }

  private void fillGaps(List<Object> row, int prevIndex, int currentIndex) {
    for(int i = prevIndex + 1; i < currentIndex; i++) {
      row.add(null);
    }
  }



}