package org.bmsource.dwh.common.utils;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

public class BeanUtils {
    public static Field[] getProperties(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays
            .stream(fields)
            .filter(f -> !f.isSynthetic()
                && !java.lang.reflect.Modifier.isStatic(f.getModifiers())
            )
            .toArray(Field[]::new);
    }
}
