package org.bmsource.dwh.charts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ChartRepositoryImpl implements ChartRepository {

    @Autowired
    NamedParameterJdbcTemplate template;

    @Override
    public List<Map<String, Object>> queryAggregate(String projectId, List<String> measures, List<String> dimensions,
                                                    List<String> sorts,
                                                    Map<String, ?> filters) {
        QueryGenerator queryGenerator = getQueryGenerator();

        String sql = queryGenerator.queryAggregate(measures, dimensions, sorts, filters);
        List<Map<String, Object>> result = template.queryForList(sql, filters);
        return queryGenerator.convertCase(result);
    }

    @Override
    public List<Map<String, Object>> queryDistinctValues(String dimension) {
        QueryGenerator queryGenerator = getQueryGenerator();
        String sql = queryGenerator.queryDistinctValues(dimension);
        List<Map<String, Object>> result = template.getJdbcTemplate().queryForList(sql, 1000);
        return queryGenerator.convertCase(result);
    }

    private QueryGenerator getQueryGenerator() {
        return QueryGenerator.builder()
            .withDataSource(template.getJdbcTemplate().getDataSource())
            .withRootTable("fact")
            .usingSnakeCaseConversion()
            .build();
    }
}
