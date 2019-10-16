package org.bmsource.dwh.common.reader;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FactModelMapper<Fact> {

    private final Class<Fact> typeParameterClass;

    private final Map<Integer, String> indexMapping = new LinkedHashMap<>();

    public FactModelMapper(Class<Fact> typeParameterClass, List<String> columns, Map<String, String> mapping) {
        this.typeParameterClass = typeParameterClass;
        mapping.forEach((key, value) -> {
            Optional<String> o = columns.stream().filter(obj -> obj.equals(key)).findFirst();
            o.ifPresent(s -> indexMapping.put(columns.indexOf(s), value));
        });
    }

    public Fact mapRow(List<Object> row) {
        try {
            Fact fact = typeParameterClass.newInstance();
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

    public List<Fact> mapList(List<List<Object>> rows) {
        return rows.stream()
            .map(row -> mapRow(row))
            .collect(Collectors.toList());
    }

}
