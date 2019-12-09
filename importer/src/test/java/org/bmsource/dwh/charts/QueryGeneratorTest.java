package org.bmsource.dwh.charts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.*;


@ActiveProfiles({"unit-test"})
@DataJdbcTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ChartsConfiguration.class})
public class QueryGeneratorTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void testQuery() throws Exception {
        String expectedSQL = "select " +
            "supplier_name, sum(cost), avg(discounted_cost) " +
            "from fact " +
            "where (some_field = :someField " +
            "and some_other_field in (:someOtherField)) " +
            "group by supplier_name " +
            "order by transaction_id asc";

        List<String> measures = new ArrayList<String>() {{
            add("sumCost");
            add("avgDiscountedCost");
        }};

        List<String> dimensions = new ArrayList<String>() {{
            add("supplierName");
        }};

        List<String> sorts = new ArrayList<String>() {{
            add("ascTransactionId");
        }};

        Map<String, Object> filters = new LinkedHashMap<String, Object>() {{
            put("someField", 1);
            put("someOtherField", Arrays.asList("11", "22"));
        }};

        QueryGenerator queryGenerator = QueryGenerator.builder()
            .withDataSource(dataSource)
            .withRootTable("fact")
            .build();
        String sql = queryGenerator.query(measures, dimensions, sorts, filters);
        Assertions.assertEquals(expectedSQL, sql);
    }
}
