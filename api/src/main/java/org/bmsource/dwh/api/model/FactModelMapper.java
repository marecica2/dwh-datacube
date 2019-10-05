package org.bmsource.dwh.api.model;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
                BeanUtils.setProperty(fact, attribute, row.get(index));
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return fact;
    }

}
