package org.bmsource.dwh.common.io.writer;

import org.bmsource.dwh.common.io.DataRow;
import org.bmsource.dwh.common.fileManager.FileManager;
import org.bmsource.dwh.common.fileManager.ResourceFileManager;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.util.*;

public class ExcelWriterTest {

    FileManager fileManager = new ResourceFileManager();

    @Test
    public void testExcelWriter() throws Exception {
        ArrayList<String> header = new ArrayList<String>() {{
            add("column1");
            add("column2");
            add("column3");
        }};

        Map<String, List<String>> errors = new HashMap<String, List<String>>() {{
            put("column2", Arrays.asList(new String[]{"should not be null"}));
        }};

        List<DataRow> rows = new ArrayList<>();
        rows.add(createRow(new String[]{"column 1 value 1", "column 2 value 1", "value 31"}, null));
        rows.add(createRow(new String[]{"column 1 value 2", "column 2 value 2", "value 32"}, errors));
        rows.add(createRow(new String[]{"column 1 value 3", "column 2 value 3", "value 33"}, null));

        OutputStream outputStream = fileManager.writeStream("1234", "test.xlsx");
        ExcelWriter writer = new ExcelWriter(outputStream);
        writer.open();
        writer.writeHeader(header);
        writer.writeRows(header, rows);
        writer.close();
    }

    private DataRow createRow(String[] values, Map<String, List<String>> errors) {
        DataRow<Fact> data = new DataRow<>();
        data.setRow(Arrays.asList(values));
        data.setErrors(errors);
        return data;
    }

    private static class Fact {
    }

}
