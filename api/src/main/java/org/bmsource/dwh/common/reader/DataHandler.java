package org.bmsource.dwh.common.reader;

import java.util.List;

public interface DataHandler {

    default void onStart() { }

    void onRead(List<List<Object>> rows, List<Object> header, int rowsCount, int totalRowsCount) throws InterruptedException;

    default void onFinish(int totalRowsCount) { }
}
