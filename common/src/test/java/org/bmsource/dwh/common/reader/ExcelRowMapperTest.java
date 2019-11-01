package org.bmsource.dwh.common.reader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

public class ExcelRowMapperTest {

    @Test
    public void testBeanMappingForTypes() {
        List<String> headerColumn = new ArrayList<String>() {{
            add("integerColumn");
            add("doubleColumn");
            add("stringColumn");
            add("dateColumn");
            add("dateColumn1");
            add("bigDecimalColumn");
        }};
        List<Object> row = new ArrayList<Object>() {{
            add("123");
            add("0.1234");
            add("string");
            add("2019-10-24 12:00:00");
            add("02/05/2018");
            add("999999.99");
        }};
        Map<String, String> mapping = new HashMap<String, String>() {{
            put("integerColumn", "integerProp");
            put("doubleColumn", "doubleProp");
            put("stringColumn", "stringProp");
            put("dateColumn", "dateProp");
            put("dateColumn1", "dateProp1");
            put("bigDecimalColumn", "bigDecimalProp");
        }};
        ExcelRowMapper<Bean> mapper = new ExcelRowMapper<>(Bean.class, headerColumn, mapping);
        Bean bean = mapper.map(row);
        Assertions.assertEquals(0.1234, bean.getDoubleProp());
        Assertions.assertEquals("string", bean.getStringProp());
        Calendar cal = new Calendar
            .Builder()
            .set(Calendar.YEAR, 2019)
            .set(Calendar.MONTH, Calendar.OCTOBER)
            .set(Calendar.DAY_OF_MONTH, 24)
            .set(Calendar.HOUR, 12)
            .build();
        Calendar cal1 = new Calendar
            .Builder()
            .set(Calendar.YEAR, 2018)
            .set(Calendar.MONTH, Calendar.MAY)
            .set(Calendar.DAY_OF_MONTH, 2)
            .build();
        Assertions.assertEquals(cal.getTime(),bean.getDateProp());
        Assertions.assertEquals(cal1.getTime(),bean.getDateProp1());
        Assertions.assertEquals(new BigDecimal("999999.99"),bean.getBigDecimalProp());
    }

    public static class Bean {
        private int integerProp;

        private double doubleProp;

        private String stringProp;

        private Date dateProp;

        private Date dateProp1;

        private BigDecimal bigDecimalProp;

        public int getIntegerProp() {
            return integerProp;
        }

        public void setIntegerProp(int integerProp) {
            this.integerProp = integerProp;
        }

        public double getDoubleProp() {
            return doubleProp;
        }

        public void setDoubleProp(double doubleProp) {
            this.doubleProp = doubleProp;
        }

        public String getStringProp() {
            return stringProp;
        }

        public void setStringProp(String stringProp) {
            this.stringProp = stringProp;
        }

        public Date getDateProp() {
            return dateProp;
        }

        public void setDateProp(Date dateProp) {
            this.dateProp = dateProp;
        }

        public BigDecimal getBigDecimalProp() {
            return bigDecimalProp;
        }

        public void setBigDecimalProp(BigDecimal bigDecimalProp) {
            this.bigDecimalProp = bigDecimalProp;
        }

        public Date getDateProp1() {
            return dateProp1;
        }

        public void setDateProp1(Date dateProp1) {
            this.dateProp1 = dateProp1;
        }

        @Override
        public String toString() {
            return "Bean{" +
                "integerProp=" + integerProp +
                ", doubleProp=" + doubleProp +
                ", stringProp='" + stringProp + '\'' +
                ", dateProp=" + dateProp +
                ", dateProp1=" + dateProp1 +
                ", bigDecimalProp=" + bigDecimalProp +
                '}';
        }
    }
}
