package org.bmsource.dwh.charts;

import com.google.common.base.CaseFormat;
import org.bmsource.dwh.common.utils.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.jooq.impl.DSL.*;

public class QueryGenerator {
    private static final String SUM = "sum_";
    private static final String COUNT = "count_";
    private static final String AVG = "avg_";
    private static final String ORDER_ASC = "asc_";
    private static final String ORDER_DESC = "desc_";

    private static final List<String> measurePrefixes = new ArrayList<>();

    static {
        measurePrefixes.add(COUNT);
        measurePrefixes.add(AVG);
        measurePrefixes.add(SUM);
    }

    private static final List<String> orderPrefixes = new ArrayList<>();

    static {
        orderPrefixes.add(ORDER_ASC);
        orderPrefixes.add(ORDER_DESC);
    }

    @Autowired
    DataSource dataSource;

    private String rootTable;

    private QueryGenerator(Builder builder) {
        dataSource = builder.dataSource;
        rootTable = builder.rootTable;
    }

    public static IDataSource builder() {
        return new Builder();
    }

    public String query(List<String> measures,
                        List<String> dimensions,
                        List<String> ordering,
                        Map<String, ?> filters) throws SQLException {
        Select<?> q = generate(measures, dimensions, ordering, filters);
        DSLContext create = initCreate();
        return create.renderNamedParams(q);
    }

    private Select<?> generate(List<String> measuresParam,
                               List<String> dimensionsParam,
                               List<String> orderingParam,
                               Map<String, ?> filtersParam) throws SQLException {
        List<String> measures = snake(measuresParam);
        List<String> dimensions = snake(dimensionsParam);
        List<String> ordering = snake(orderingParam);

        DSLContext create = initCreate();

        List<Field<?>> dimensionsFields = dimensions
            .stream()
            .map(DSL::field).collect(Collectors.toList());

        List<Field<?>> aggregateMeasureFields = getAggregateMeasureFields(measures);
        List<Field<?>> simpleMeasureFields = getSimpleMeasureFields(measures);
        List<OrderField<?>> orderFields = getOrderFields(ordering);

        List<Field<?>> selectFields = new ArrayList<>();
        selectFields.addAll(dimensionsFields);
        selectFields.addAll(aggregateMeasureFields);
        selectFields.addAll(simpleMeasureFields);

        List<Condition> conditions = getFilters(filtersParam);

        return create.select(selectFields)
            .from(table(rootTable))
            .where(conditions)
            .groupBy(dimensionsFields)
            .orderBy(orderFields);
    }

    private List<Condition> getFilters(Map<String, ?> filters) {
        return filters
            .entrySet()
            .stream()
            .map(e -> {
                Condition c = null;
                if (e.getValue() instanceof Collection) {
                    c = field(snake(e.getKey()))
                        .in(field(param(e.getKey(), "")));
                } else {
                    c = field(snake(e.getKey()))
                        .equal(param(e.getKey(), e.getValue()));
                }
                return c;
            })
            .collect(Collectors.toList());
    }

    private List<Field<?>> getSimpleMeasureFields(List<String> measures) {
        return measures
            .stream()
            .filter(item ->
                !org.apache.commons.lang3.StringUtils.startsWithAny(item,
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
                    case SUM:
                        measure =
                            sum(field(item[1], BigDecimal.class)).as(SUM + item[1]);
                        break;
                    case AVG:
                        measure =
                            avg(field(item[1], BigDecimal.class)).as(AVG + item[1]);
                        break;
                    case COUNT:
                        measure =
                            count(field(item[1])).as(item[1]).as(COUNT + item[1]);
                        break;
                    default:
                        break;
                }
                return measure;
            })
            .collect(Collectors.toList());
    }

    private List<OrderField<?>> getOrderFields(List<String> orderFields) {
        return orderFields
            .stream()
            .map(sort -> StringUtils.splitByPrefix(orderPrefixes, sort))
            .filter(Objects::nonNull)
            .map(item -> {
                OrderField<?> measure = null;
                switch (item[0]) {
                    case ORDER_ASC:
                        measure = field(item[1]).asc();
                        break;
                    case ORDER_DESC:
                        measure = field(item[1]).desc();
                        break;
                    default:
                        break;
                }
                return measure;
            })
            .collect(Collectors.toList());
    }

    private DSLContext initCreate() throws SQLException {
        return using(dataSource.getConnection(), SQLDialect.POSTGRES);
    }

    private List<String> snake(List<String> list) {
        return list
            .stream()
            .map(this::snake)
            .collect(Collectors.toList());
    }

    private String snake(String s) {
        return StringUtils.camelToSnake(s);
    }


    interface IBuild {
        QueryGenerator build();
    }

    interface IRootTable {
        IBuild withRootTable(String val);
    }

    interface IDataSource {
        IRootTable withDataSource(DataSource val);
    }

    public static final class Builder implements IRootTable, IDataSource, IBuild {
        private String rootTable;
        private DataSource dataSource;

        private Builder() {
        }

        @Override
        public IBuild withRootTable(String val) {
            rootTable = val;
            return this;
        }

        @Override
        public IRootTable withDataSource(DataSource val) {
            dataSource = val;
            return this;
        }

        public QueryGenerator build() {
            return new QueryGenerator(this);
        }
    }
}
