package org.bmsource.dwh.olap.charts;

import org.bmsource.dwh.common.multitenancy.TenantContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;


@ActiveProfiles({"unit-test"})
@ExtendWith(SpringExtension.class)
public class QueryGeneratorTest {
    private static String tenant = "000000-00000-00001";

    @Test
    public void testQuery() {
        TenantContext.setTenantSchema(tenant);
        String expectedSQL = "select " +
            "supplier_name, sum(cost) as \"sum_cost\", avg(discounted_cost) as \"avg_discounted_cost\" " +
            "from \"" + tenant + "\".fact " +
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
            .withDataSource(null)
            .withRootTable("fact")
            .usingSnakeCaseConversion()
            .build();
        String sql = queryGenerator.queryAggregate(measures, dimensions, sorts, filters);
        Assertions.assertEquals(expectedSQL, sql);
    }
}
