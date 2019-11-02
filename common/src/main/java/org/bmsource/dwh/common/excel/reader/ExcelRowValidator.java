package org.bmsource.dwh.common.excel.reader;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelRowValidator<Bean> {

    private final Validator validator;
    private final Map<String, String> mapping;

    public ExcelRowValidator(Map<String, String> columnPropertyMapping) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.mapping = inverseMapping(columnPropertyMapping);
    }

    public Map<String, List<String>> getValidationErrors(Bean bean) {
        Map<String, List<String>> errors = new HashMap<>();

        Set<ConstraintViolation<Object>> validationErrors = validator.validate(bean);
        if (validationErrors != null && validationErrors.size() > 0) {
            ConstraintViolation<Object> error = validationErrors.iterator().next();
            String beanPropertyName = error.getPropertyPath().toString();
            String columnName = mapping.get(beanPropertyName);
            List<String> errorMessages = validationErrors
                .stream()
                .map(err -> err.getMessage())
                .collect(Collectors.toList());
            errors.put(columnName, errorMessages);
        }
        return errors;
    }

    private Map<String, String> inverseMapping(Map<String, String> columnPropertyMapping) {
        return columnPropertyMapping.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
