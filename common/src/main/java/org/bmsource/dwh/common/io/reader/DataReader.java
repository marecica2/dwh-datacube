package org.bmsource.dwh.common.io.reader;

import java.io.IOException;
import java.util.List;

public interface DataReader extends AutoCloseable {

    int getTotalRowsCount();

    int getReadRowsCount();

    void reset();

    void close() throws IOException;

    List<Object> nextRow();

    boolean hasNextRow();
}
