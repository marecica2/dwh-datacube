package org.bmsource.dwh.common.reader;

import java.io.InputStream;
import java.util.List;

public interface DataReader {

    MappingResult readHeaderRow(InputStream inputStream) throws Exception;

    void readContent(InputStream inputStream, DataHandler rowsHandler) throws Exception;

    void readContent(InputStream inputStream, DataHandler rowsHandler, Integer batchSize) throws Exception;

    List<List<Object>> readContent(InputStream inputStream, int rowsToRead) throws Exception;
}
