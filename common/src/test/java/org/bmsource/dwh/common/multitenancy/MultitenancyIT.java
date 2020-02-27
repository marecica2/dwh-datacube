package org.bmsource.dwh.common.multitenancy;

import org.bmsource.dwh.common.multitenancy.app.TestApplication;
import org.bmsource.dwh.common.utils.TestUtils;
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
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.text.MessageFormat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("integration-test")
public class MultitenancyIT {


    private boolean printRest = false;
    private String tenant = TestUtils.TENANT1;
    private String project = "1";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    JdbcTemplate template;

    @BeforeEach
    public void setup() {
        mvc = webAppContextSetup(this.wac).build();
    }

    @BeforeAll
    public void setupSchemas() {
        String beforeScript = String.format("CREATE SCHEMA \"%s\"; " +
                "CREATE TABLE \"%s\".person ( " +
                "id int primary key " +
                "not null, name varchar(50) " +
                ");"
            ,
            TestUtils.TENANT1,
            TestUtils.TENANT1
        );
        template.execute(beforeScript);
    }

    @AfterAll
    public void dropSchemas() {
       template.execute(String.format("DROP SCHEMA \"%s\" CASCADE;", TestUtils.TENANT1));
    }

    @Test
    public void testMultitenancy() throws Exception {
        mvc.perform(MockMvcRequestBuilders
            .get("/test", project)
            .header(Constants.TENANT_HEADER, tenant)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(doPrint())
            .andExpect(status().isOk());
    }

    private ResultHandler doPrint() {
        if (printRest)
            return MockMvcResultHandlers.print();
        return result -> {
        };
    }

}
