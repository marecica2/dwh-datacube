package org.bmsource.dwh.api.reader;

import java.io.InputStream;
import java.util.List;

public interface DataReader {

  MappingResult readHeaderRow(InputStream inputStream) throws Exception;

  void readContent(InputStream inputStream, RowsHandler rowsHandler, FinishHandler finishHandler) throws Exception;

  void readContent(InputStream inputStream, RowsHandler rowsHandler, FinishHandler finishHandler, Integer batchSize) throws Exception;

  List<List<Object>> readContent(InputStream inputStream, int rowsToRead) throws Exception;
}
