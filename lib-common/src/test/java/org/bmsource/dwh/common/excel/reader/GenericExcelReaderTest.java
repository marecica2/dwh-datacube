package org.bmsource.dwh.common.excel.reader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GenericExcelReaderTest {

    @Test
    public void testRowsReading() throws Exception {
        List<Fact> factItems = new ArrayList();
        final int[] rows = { 0 };
        InputStream stream = new ClassPathResource("/spends5.xlsx").getInputStream();
        GenericExcelReader<Fact> reader = new GenericExcelReader<>(new ExcelReaderHandler<Fact>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onRead(List<Fact> items) {
                factItems.addAll(items);
            }

            @Override
            public void onFinish(int rowCount) {
                rows[0] = rowCount;
                Assertions.assertEquals("BU1", factItems.get(0).getBusinessUnit());
            }

            @Override
            public Fact transform(Fact item) {
                return item;
            }
        }, Fact.class);

        reader.parse(stream);
    }

    public static class Fact {
        private String businessUnit;

        public String getBusinessUnit() {
            return businessUnit;
        }

        public void setBusinessUnit(String businessUnit) {
            this.businessUnit = businessUnit;
        }
    }
}
