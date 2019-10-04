package org.bmsource.dwh.api.model;


import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FactModelMapper {

    private final Map<String, String> mapping;

    private final Map<Integer, String> indexMapping = new LinkedHashMap<>();

    public FactModelMapper(Map<String, String> mapping) {
        this.mapping = mapping;
        this.mapping.forEach((key, value) -> {
            indexMapping.put( new ArrayList<>(mapping.keySet()).indexOf(key), value);
        });
    }

    public Fact mapRow(List<Object> row) {
        Fact fact = new Fact();
        this.indexMapping.forEach((index, attribute) ->{
            try {
                BeanUtils.setProperty(fact,attribute,row.get(index));
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return fact;
    }

}
