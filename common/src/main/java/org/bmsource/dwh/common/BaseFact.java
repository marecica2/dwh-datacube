package org.bmsource.dwh.common;

import com.google.common.base.CaseFormat;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BaseFact {
    public String selectSQL() {
        String tableName = getTableName();
        return String.format("SELECT * FROM (%s) ORDER BY id ASC", tableName);
    }

    public String insertSQL() {
        String columns = String.join(
            ", ",
            Arrays.stream(this.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Transient.class) == null)
                .map(field -> CaseFormat.LOWER_CAMEL
                    .converterTo(CaseFormat.LOWER_UNDERSCORE)
                    .convert(field.getName()))
                .collect(Collectors.toList())
        );

        String parameters = String.join(
            ", ",
            Arrays.stream(this.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Transient.class) == null)
                .map(field -> ":" + field.getName())
                .collect(Collectors.toList())
        );

        String tableName = getTableName();
        return String.format("INSERT INTO %s (%s) VALUES (%s)",
            tableName,
            columns,
            parameters
        );
    }

    private String getTableName() {
        String tableName;
        try {
            tableName = this.getClass().getAnnotation(Table.class).name();
        } catch (NullPointerException npe) {
            tableName = this.getClass().getSimpleName();
        }
        return tableName;
    }
}
