package org.bmsource.dwh.olap.charts;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/charts")
public class ChartController {

    @Autowired
    ChartRepositoryImpl repository;

    @GetMapping
    public List<Map<String, Object>> getChart(@RequestHeader("x-tenant") String tenant,
                                              @RequestParam("projectId") String projectId,
                                              @RequestParam("dimensions") String[] dimensions,
                                              @RequestParam("measures") String[] measures,
                                              @RequestParam("sorts") String[] sorts,
                                              @RequestParam Map<String, String> queryParams
    ) {
        Map<String, List<String>> filters = extractFilters(queryParams);
        List<Map<String, Object>> result = repository.queryAggregate(
            projectId,
            Arrays.asList(measures),
            Arrays.asList(dimensions),
            Arrays.asList(sorts),
            filters
        );
        return result;
    }

    @GetMapping("/dimensions")
    public ResponseEntity<List<Map<String, Object>>> getDimensionValues(@RequestHeader("x-tenant") String tenant,
                                                                        @RequestParam("projectId") String projectId,
                                                                        @RequestParam("dimension") String dimension) {
        List<Map<String, Object>> result = repository.queryDistinctValues(dimension);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Map<String, List<String>> extractFilters(Map<String, String> queryParams) {
        return queryParams.entrySet()
            .stream()
            .filter(e -> !StringUtils.
                equalsAny(e.getKey(), "dimensions", "measures", "sorts"))
            .collect(Collectors.toMap(e -> e.getKey(), e -> Arrays.asList(e.getValue().split(","))));
    }
}
