package org.bmsource.dwh.api.parsers;

import java.io.InputStream;
import java.util.List;

public interface DataReader {

  List<List<Object>> readContent(InputStream inputStream, int rowsToRead) throws Exception;

  MappingResult readHeaderRow(InputStream inputStream) throws Exception;

  List<List<Object>> readPreview(InputStream inputStream, int numOfRows) throws Exception;
}
