package org.bmsource.dwh.masterdata.web;

import org.bmsource.dwh.ImporterApplication;
import org.bmsource.dwh.IntegrationTestUtils;
import org.bmsource.dwh.common.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@ActiveProfiles({"integration-test"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ImporterApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MasterDataControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    JdbcTemplate template;

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testTaxonomyUpload() throws Exception {
        URL file = this.getClass().getResource("/taxonomy.xlsx");
        String url = "/taxonomy/import";
        String fileName = "taxonomy.xlsx";
        IntegrationTestUtils.fileUpload(mockMvc, file, url, fileName);
        int importedRows = template.queryForObject("SELECT count(*) FROM \"" + TestUtils.TENANT1 + "\".taxonomy",
            Integer.class);
        Assertions.assertEquals(34, importedRows);
    }

    @Test
    public void testServiceTypeUpload() throws Exception {
        URL file = this.getClass().getResource("/matrix.xlsx");
        String url = "/service-types/import";
        String fileName = "matrix.xlsx";
        IntegrationTestUtils.fileUpload(mockMvc, file, url, fileName);
        int importedRows = template.queryForObject("SELECT count(*) FROM \"" + TestUtils.TENANT1 + "\"" +
            ".service_type_mapping", Integer.class);
        Assertions.assertEquals(56, importedRows);
    }

    @Test
    public void testRateCardUpload() throws Exception {
        URL file = this.getClass().getResource("/standard_rate_card_small.xlsx");
        String url = "/rate-cards/import";
        String fileName = "standard_rate_card_small.xlsx";
        IntegrationTestUtils.fileUpload(mockMvc, file, url, fileName);
        int importedRows = template.queryForObject("SELECT count(*) FROM \"" + TestUtils.TENANT1 + "\"" +
            ".standard_rate_card", Integer.class);
        Assertions.assertEquals(0, importedRows);
    }
}
