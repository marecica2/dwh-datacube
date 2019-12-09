package org.bmsource.dwh.charts;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{projectId}/charts")
public class ChartController {

    @Autowired
    ChartRepository repository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getChart(@RequestHeader("x-tenant") String tenant,
                                               @PathVariable("projectId") String projectId,
                                               @RequestParam("dimensions") String[] dimensions,
                                               @RequestParam("measures") String[] measures,
                                               @RequestParam("sorts") String[] sorts,
                                               @RequestParam Map<String, String> queryParams
    ) throws SQLException {
        List<Map<String, Object>> result = repository.fetch(
            projectId,
            Arrays.asList(measures),
            Arrays.asList(dimensions),
            Arrays.asList(sorts),
            queryParams.entrySet()
                .stream()
                .filter(e -> !StringUtils.
                    equalsAny(e.getKey(), "dimensions", "measures", "sorts"))
                .collect(Collectors.toMap(e -> e.getKey(), e -> Arrays.asList(e.getValue().split(","))))
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
