package org.bmsource.dwh.common.io.writer;

import org.bmsource.dwh.common.io.DataRow;

import java.io.IOException;
import java.util.List;

public interface DataWriter extends AutoCloseable {
    void open();

    void writeHeader(List<String> header);

    void writeRows(List<String> header, List<DataRow> rows);

    void close() throws IOException;
}
