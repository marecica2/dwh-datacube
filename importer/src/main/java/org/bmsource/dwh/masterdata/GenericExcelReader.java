package org.bmsource.dwh.masterdata;

import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.io.reader.ExcelRowMapper;
import org.bmsource.dwh.common.io.reader.BatchHandler;
import org.bmsource.dwh.common.io.reader.BatchReader;
import org.bmsource.dwh.common.io.reader.ExcelBatchReader;
import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GenericExcelReader<T extends BaseFact> {
    private Consumer<Void> onStart;
    private Consumer<List<T>> onRead;
    private Consumer<Integer> onFinish;
    private Class<T> classType;

    public GenericExcelReader(Consumer<Void> onStart, Consumer<List<T>> onRead,
                              Consumer<Integer> onFinish,
                              Class<T> classType) {
        this.onStart = onStart;
        this.onRead = onRead;
        this.onFinish = onFinish;
        this.classType = classType;
    }

    @Async("asyncExecutor")
    public void parse(InputStream stream) throws Exception {
        BatchReader reader = new ExcelBatchReader();
        final List<ExcelRowMapper<T>> rowMapper = new ArrayList<>();

        reader.readContent(stream, new BatchHandler() {
            @Override
            public void onStart() {
                if (onStart != null)
                    onStart.accept(null);
            }

            @Override
            public void onRead(List<List<Object>> rows, List<String> header, int rowsCount,
                               int totalRowsCount) {
                if (rowMapper.size() == 0) {
                    Map<String, String> simpleMapping =
                        header.stream().collect(Collectors.toMap(String::toString, item -> item));
                    rowMapper.add(new ExcelRowMapper<T>(classType, header, simpleMapping));
                }
                List<T> items = rowMapper.get(0).mapList(rows);
                if (onRead != null)
                    onRead.accept(items);
            }

            @Override
            public void onFinish(int totalRowsCount) {
                if (onFinish != null)
                    onFinish.accept(totalRowsCount);
            }
        });
    }
}
