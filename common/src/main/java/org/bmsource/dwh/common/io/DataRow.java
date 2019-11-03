package org.bmsource.dwh.common.io;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataRow<Fact> {
    private List<Object> row;
    private Map<String, List<String>> errors;
    private Fact fact;
    private Map<String, String> mapping;
    private boolean valid = true;

    public List<Object> getRow() {
        return row;
    }

    public void setRow(List<Object> row) {
        this.row = row;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));;
    }

    public void validate() {
        for (Field field : fact.getClass().getDeclaredFields()) {
            if (field.getAnnotation(NotNull.class) != null && errors.get(mapping.get(field.getName())) != null) {
                valid = false;
            } else if(errors.get(field.getName()) != null) {
                try {
                    field.set(fact, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isValid() {
        return valid;
    }
}
