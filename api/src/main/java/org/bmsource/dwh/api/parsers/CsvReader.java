package org.bmsource.dwh.api.parsers;

import java.io.InputStream;
import java.util.List;

public class CsvReader implements DataReader {

  @Override
  public List<List<Object>> readContent(InputStream inputStream, int rowsToRead) throws Exception {
    return null;
  }

  @Override
  public MappingResult readHeaderRow(InputStream inputStream) throws Exception {
    return null;
  }

  @Override
  public List<List<Object>> readPreview(InputStream inputStream, int numOfRows) throws Exception {
    return null;
  }
}