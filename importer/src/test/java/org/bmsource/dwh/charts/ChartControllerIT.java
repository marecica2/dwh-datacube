package org.bmsource.dwh.charts;

import org.apache.commons.io.FileUtils;
import org.bmsource.dwh.ImporterApplication;
import org.bmsource.dwh.IntegrationTestUtils;
import org.bmsource.dwh.common.multitenancy.TenantContext;
import org.bmsource.dwh.domain.repository.FactRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles("integration-test")
@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ImporterApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChartControllerIT {
    private boolean printRest = false;
    private static String tenant = "000000-00000-00001";
    private static String project = "1";
    static {
        TenantContext.setTenantSchema(tenant);
    }

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    JdbcTemplate template;

    @Autowired
    IntegrationTestUtils integrationTestUtils;


    @Autowired
    FactRepository factRepository;

    @Autowired
    RedisOperations<String, String> operations;

    @AfterAll
    public void afterAll() throws Exception {
        File sql = ResourceUtils.getFile("classpath:batch_clean_up.sql");
        template.execute(FileUtils.readFileToString(sql));
    }

    @BeforeEach
    public void setup() {
        mvc = webAppContextSetup(this.wac).build();
    }

    @AfterEach
    public void afterEach() {
        flushRedis();
    }



    @Test
    @Sql(scripts = "/spends.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testChart() throws Exception {
        TenantContext.setTenantSchema(tenant);
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
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].sumCost").value(307.32))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].supplierName").value("Ups"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].sumCost").value(1987.52));
    }

    @Test
    @Sql(scripts = "/spends.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testDimensions() throws Exception {
        TenantContext.setTenantSchema(tenant);
        String dimension = "supplierName";
        mvc.perform(MockMvcRequestBuilders
            .get("/{projectId}/charts/dimensions", project)
            .header("x-tenant", tenant)
            .param("dimension", dimension)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(doPrint())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].supplierName").value("Fedex"));
    }

    private ResultHandler doPrint() {
        if(printRest)
            return MockMvcResultHandlers.print();
        return result -> { };
    }

    private void flushRedis() {
        operations.execute((RedisCallback<Void>) connection -> {
            connection.serverCommands().flushAll();
            return null;
        });
    }
}
