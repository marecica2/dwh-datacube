package org.bmsource.dwh.common.reader;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BeanMapper<Bean> {

    private final Class<Bean> typeParameterClass;

    private final Map<Integer, String> indexMapping = new LinkedHashMap<>();

    public BeanMapper(Class<Bean> beanClass, List<String> columns, Map<String, String> mapping) {
        this.typeParameterClass = beanClass;
        mapping.forEach((key, value) -> {
            Optional<String> o = columns.stream().filter(obj -> obj.equals(key)).findFirst();
            o.ifPresent(s -> indexMapping.put(columns.indexOf(s), value));
        });

        MyConverter converter = new MyConverter("yyyy-MM-dd HH:mm:ss");
        ConvertUtils.register(converter, Date.class);
    }

    public Bean mapRow(List<Object> row) {
        try {
            Bean bean = typeParameterClass.newInstance();
            this.indexMapping.forEach((index, attribute) -> {
                if (row.size() > index) {
                    try {
                        BeanUtils.copyProperty(bean, attribute, row.get(index));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
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
            .map(row -> mapRow(row))
            .collect(Collectors.toList());
    }

    private static class MyConverter implements Converter {
        private static SimpleDateFormat format;

        public MyConverter(String pattern) {
            format = new SimpleDateFormat(pattern);
        }

        @Override
        public Object convert(Class type, Object value) {
            if (value == null) {
                return null;
            }

            if (value instanceof String) {
                String tmp = (String) value;
                if (tmp.trim().length() == 0) {
                    return null;
                } else {
                    try {
                        return new BigDecimal(tmp);
                    } catch (NumberFormatException nfe) {
                        try {
                            return format.parse(tmp);
                        } catch (ParseException pe) {
                            return tmp;
                        }
                    }
                }
            } else {
                throw new ConversionException("not String");
            }
        }
    }

}
