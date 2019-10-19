package org.bmsource.dwh.common;

import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BaseFact {
    public String insertSQL() {
        String columns = String.join(
            ", ",
            Arrays.stream(this.getClass().getDeclaredFields())
                .map(field -> CaseFormat.LOWER_CAMEL
                    .converterTo(CaseFormat.LOWER_UNDERSCORE)
                    .convert(field.getName()))
                .collect(Collectors.toList())
        );

        String parameters = String.join(
            ", ",
            Arrays.stream(this.getClass().getDeclaredFields())
                .map(field -> ":" + field.getName())
                .collect(Collectors.toList())
        );

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
            this.getClass().getSimpleName(),
            columns,
            parameters
        );
    }
}
