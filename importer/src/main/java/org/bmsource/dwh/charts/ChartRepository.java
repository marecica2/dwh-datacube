package org.bmsource.dwh.charts;

import org.bmsource.dwh.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ChartRepository {

    @Autowired
    NamedParameterJdbcTemplate template;

    public List<Map<String, Object>> fetch(String projectId, List<String> measures, List<String> dimensions,
                                           List<String> sorts,
                                           Map<String, ?> filters) throws SQLException {
        QueryGenerator queryGenerator = QueryGenerator.builder()
            .withDataSource(template.getJdbcTemplate().getDataSource())
            .withRootTable("fact")
            .build();

        String sql = queryGenerator.query(measures, dimensions, sorts, filters);
        List<Map<String, Object>> result = template.queryForList(sql, filters);
        return result
            .stream()
            .map(i -> i.entrySet()
                .stream()
                .collect(Collectors.toMap(
                    e -> StringUtils.snakeToCamel(e.getKey()),
                    e -> e.getValue())))
            .collect(Collectors.toList());
    }

}
