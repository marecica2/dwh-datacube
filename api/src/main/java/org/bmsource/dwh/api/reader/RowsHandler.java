package org.bmsource.dwh.api.reader;

import java.util.List;

@FunctionalInterface
public interface RowsHandler {

    void handleRows(List<List<Object>> rows);
}
