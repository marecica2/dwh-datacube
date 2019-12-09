package org.bmsource.dwh.charts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;


@ActiveProfiles({"unit-test"})
@DataJdbcTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ChartsConfiguration.class})
public class ChartsRepositoryTest {

    @Autowired
    ChartsRepository repository;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testTaxonomyUpload() throws Exception {
        List<String> measures = new ArrayList<String>() {{
            add("sumCost");
        }};
        List<String> dimensions = new ArrayList<String>() {{
            add("supplierName");
        }};
        List<String> sorts = new ArrayList<String>() {{
            add("ascTransactionId");
        }};
        repository.fetchChart(measures, dimensions, sorts);
    }
}
