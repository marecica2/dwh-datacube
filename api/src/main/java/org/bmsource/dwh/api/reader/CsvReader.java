package org.bmsource.dwh.api.reader;

import java.io.InputStream;
import java.util.List;

public class CsvReader implements DataReader {

  @Override
  public MappingResult readHeaderRow(InputStream inputStream) throws Exception {
    return null;
  }

  @Override
  public void readContent(InputStream inputStream, RowsHandler rowsHandler) throws Exception {

  }

  @Override
  public void readContent(InputStream inputStream, RowsHandler rowsHandler, Integer batchSize) throws Exception {

  }

  @Override
  public List<List<Object>> readContent(InputStream inputStream, int rowsToRead) throws Exception {
    return null;
  }
}