package org.bmsource.dwh.charts;

import org.bmsource.dwh.ImporterApplication;
import org.bmsource.dwh.TestHelper;
import org.bmsource.dwh.domain.repository.FactRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles("integration-test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ImporterApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChartControllerIT {

    private boolean printRest = true;
    private String tenant = "000000-00000-00001";
    private String project = "1";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    JdbcTemplate template;

    @Autowired
    TestHelper testHelper;


    @Autowired
    FactRepository factRepository;

    @BeforeAll
    public void beforeAll() throws Exception {
        testHelper.hasRateCards();
        testHelper.hasTaxonomy();
        testHelper.hasServiceTypeMapping();
    }

    @BeforeEach
    public void setup() {
        mvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testChart() throws Exception {
        String[] measures = new String[] {"sumCost"};
        String[] dimensions = new String[] {"supplierName"};
        String[] sorts = new String[] {"ascSumCost"};
        mvc.perform(MockMvcRequestBuilders
            .get("/{projectId}/charts", project)
            .header("x-tenant", tenant)
            .param("measures", measures)
            .param("dimensions", dimensions)
            .param("sorts", sorts)
            .param("supplierName", "Ups,Fedex")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(doPrint())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].supplierName").value("Fedex"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].sumCost").value(7417.21))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].supplierName").value("Ups"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].sumCost").value(16995.90));
    }

    private ResultHandler doPrint() {
        if(printRest)
            return MockMvcResultHandlers.print();
        return result -> { };
    }
}
