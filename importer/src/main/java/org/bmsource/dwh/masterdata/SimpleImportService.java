package org.bmsource.dwh.masterdata;

import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.common.fileManager.FileManager;
import org.bmsource.dwh.common.fileManager.TmpFileManager;
import org.bmsource.dwh.common.reader.BeanMapper;
import org.bmsource.dwh.common.reader.DataHandler;
import org.bmsource.dwh.common.reader.DataReader;
import org.bmsource.dwh.common.reader.ExcelReader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SimpleImportService {

    @Async("asyncExecutor")
    public <Model extends BaseFact> void start(Class classType, InputStream stream, Consumer onFinish) throws Exception {
        DataReader reader = new ExcelReader();
        final BeanMapper[] rowMapper = {null};

        reader.readContent(stream, new DataHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onRead(List<List<Object>> rows, List<String> header, int rowsCount,
                               int totalRowsCount) {
                if (rowMapper[0] == null) {
                    Map<String, String> simpleMapping =
                        header.stream().collect(Collectors.toMap(String::toString, item -> item));
                    rowMapper[0] = new BeanMapper<Model>(classType, header, simpleMapping);
                }
                List<Model> items = rowMapper[0].mapList(rows);
                System.out.println(items);
            }

            @Override
            public void onFinish(int totalRowsCount) {
                onFinish.accept(null);
            }
        });
    }
}
