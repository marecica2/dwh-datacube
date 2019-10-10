package org.bmsource.dwh.common.reader;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FactModelMapper<T> {

    final Class<T> typeParameterClass;

    private final Map<String, String> mapping;

    private final Map<Integer, String> indexMapping = new LinkedHashMap<>();

    public FactModelMapper(Class<T> typeParameterClass, List<Object> columns, Map<String, String> mapping) {
        this.typeParameterClass = typeParameterClass;
        this.mapping = mapping;
        this.mapping.forEach((key, value) -> {
            Optional<Object> o = columns.stream().filter(obj -> obj.toString().equals(key)).findFirst();
            indexMapping.put(columns.indexOf(o.get()), value);
        });
    }

    public T mapRow(List<Object> row) {
        try {
            T fact = typeParameterClass.newInstance();
            this.indexMapping.forEach((index, attribute) -> {
                if (row.size() > index) {
                    try {
                        BeanUtils.setProperty(fact, attribute, row.get(index));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
            return fact;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> mapList(List<List<Object>> rows) {
        return rows.stream()
            .map(row -> mapRow(row))
            .collect(Collectors.toList());
    }

}
