package org.bmsource.dwh.common.reader;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelRowMapper<Bean> {

    private static Logger logger = LoggerFactory.getLogger(ExcelRowMapper.class);

    private static final String[] datePatterns = {
        "yyyy-MM-dd HH:mm:ss",
        "dd.MM.YYYY HH:mm:ss",
        "dd.MM.YYYY",
        "dd/MM/yyyy",
    };

    private final Class<Bean> typeParameterClass;

    private final Map<Integer, String> indexMapping = new LinkedHashMap<>();

    public ExcelRowMapper(Class<Bean> beanClass, List<String> columns, Map<String, String> mapping) {
        this.typeParameterClass = beanClass;
        mapping.forEach((key, value) -> {
            Optional<String> o = columns.stream().filter(obj -> obj.equals(key)).findFirst();
            o.ifPresent(s -> indexMapping.put(columns.indexOf(s), value));
        });
        ExcelConverter converter = new ExcelConverter(datePatterns);
        ConvertUtils.register(converter, Date.class);
    }

    public Bean map(List<Object> row) {
        try {
            Bean bean = typeParameterClass.newInstance();
            this.indexMapping.forEach((index, attribute) -> {
                if (row.size() > index) {
                    try {
                        BeanUtils.copyProperty(bean, attribute, row.get(index));
                    } catch (Exception e) {
                        logger.debug("Conversion failed for bean {} attribute {} value {} Error {}",
                            typeParameterClass.getName(),
                            attribute,
                            row.get(index),
                            e.getMessage());
                    }
                }
            });
            return bean;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Bean> mapList(List<List<Object>> rows) {
        return rows.stream()
            .map(row -> map(row))
            .collect(Collectors.toList());
    }

    private static class ExcelConverter implements Converter {
        private static List<SimpleDateFormat> formats = new ArrayList<>();
        private static String[] patterns;

        public ExcelConverter(String[] patterns) {
            for (String pattern : patterns) {
                formats.add(new SimpleDateFormat(pattern));
            }
        }

        @Override
        public Object convert(Class type, Object value) {
            String stringValue;

            if (value == null)
                return null;

            if (value instanceof String) {
                stringValue = (String) value;
            } else {
                throw new ConversionException("not String");
            }

            if (stringValue.trim().length() == 0) {
                return null;
            }

            for (SimpleDateFormat format : formats) {
                try {
                    Date date = format.parse(stringValue);
                    return date;
                } catch (ParseException pe) {
                    // Omitted
                }
            }

            try {
                return new BigDecimal(stringValue.replaceAll("[^0-9\\.]", ""));
            } catch (NumberFormatException nfe) {
                throw new ConversionException("not Number");
            }
        }
    }
}
