package org.bmsource.dwh.api.model;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class FactModelMapper {

    private final Map<String, String> mapping;

    private final Map<Integer, String> indexMapping = new LinkedHashMap<>();

    public FactModelMapper(List<Object> columns, Map<String, String> mapping) {
        this.mapping = mapping;
        this.mapping.forEach((key, value) -> {
            Optional<Object> o = columns.stream().filter(obj -> obj.toString().equals(key)).findFirst();
            indexMapping.put(columns.indexOf(o.get()), value);
        });
    }

    public Fact mapRow(List<Object> row) {
        Fact fact = new Fact();
        this.indexMapping.forEach((index, attribute) -> {
            try {
                if (row.size() > index) {
                    BeanUtils.setProperty(fact, attribute, row.get(index));
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return fact;
    }

    public List<Fact> mapList(List<List<Object>> rows) {
        return rows.stream()
            .map(row -> mapRow(row))
            .collect(Collectors.toList());
    }

}
