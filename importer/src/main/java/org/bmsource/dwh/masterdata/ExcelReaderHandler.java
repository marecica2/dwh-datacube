package org.bmsource.dwh.masterdata;

import java.util.List;

public interface ExcelReaderHandler<T> {
    default void onStart() {};
    default void onRead(List<T> items) {};
    default void onFinish(int rowCount) {};
}
