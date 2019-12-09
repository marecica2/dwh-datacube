package org.bmsource.dwh.charts;

import org.bmsource.dwh.common.utils.StringUtils;
import org.bmsource.dwh.domain.model.Fact;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

@Component
public class ChartsRepository {

    private static List<String> measurePrefixes = new ArrayList<>();
    static {
        measurePrefixes.add("count");
        measurePrefixes.add("avg");
        measurePrefixes.add("sum");
    }

    @Autowired
    private JdbcTemplate template;

    public void fetchChart(List<String> measures, List<String> dimensions, List<String> ordering) throws SQLException {
        DSLContext create = initCreate();

        List<Field<?>> dimensionsFields = dimensions
            .stream()
            .map(DSL::field).collect(Collectors.toList());

        List<Field<?>> aggregateMeasureFields = getAggregateMeasureFields(measures);
        List<Field<?>> simpleMeasureFields = getSimpleMeasureFields(measures);

        List<Field<?>> selectFields = new ArrayList<>();
        selectFields.addAll(dimensionsFields);
        selectFields.addAll(aggregateMeasureFields);
        selectFields.addAll(simpleMeasureFields);

        Select<?> select = create.select(selectFields)
            .from(table(Fact.TABLE_NAME))
            .groupBy(dimensionsFields);

        System.out.println(select.getSQL());
        System.out.println(select.getBindValues());
    }

    private List<Field<?>> getSimpleMeasureFields(List<String> measures) {
        return measures
            .stream()
            .filter(item ->
                org.apache.commons.lang3.StringUtils.startsWithAny(item,
                    measurePrefixes.toArray(new String[]{})))
            .map(item -> field(item, Object.class))
            .collect(Collectors.toList());
    }

    private List<Field<?>> getAggregateMeasureFields(List<String> measures) {
        return measures
            .stream()
            .map(dim -> StringUtils.splitByPrefix(measurePrefixes, dim))
            .filter(Objects::nonNull)
            .map(item -> {
                Field<?> measure = null;
                switch (item[0]) {
                    case "sum":
                        measure = sum(field(item[1], BigDecimal.class));
                        break;
                    case "avg":
                        measure = avg(field(item[1], BigDecimal.class));
                        break;
                    case "count":
                        measure = count(field(item[1]));
                        break;
                    default:
                        break;
                }
                return measure;
            })
            .collect(Collectors.toList());
    }

    private DSLContext initCreate() throws SQLException {
        return DSL.using(template.getDataSource().getConnection(), SQLDialect.POSTGRES);
    }
}
