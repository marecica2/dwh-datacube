package org.bmsource.dwh.masterdata.web;

import org.bmsource.dwh.ImporterApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles({"unit-test"})
@SpringBootTest
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
        upload(file, url, fileName);
        int importedRows = template.queryForObject("SELECT count(*) FROM service_type_taxonomy", Integer.class);
        Assertions.assertEquals(34, importedRows);
    }

    @Test
    public void testServiceTypeUpload() throws Exception {
        URL file = this.getClass().getResource("/matrix.xlsx");
        String url = "/service-types/import";
        String fileName = "matrix.xlsx";
        upload(file, url, fileName);
        int importedRows = template.queryForObject("SELECT count(*) FROM service_type_mapping", Integer.class);
        Assertions.assertEquals(56, importedRows);
    }

    @Test
    public void testRateCardUpload() throws Exception {
        URL file = this.getClass().getResource("/standard_rate_card.xlsx");
        String url = "/rate-cards/import";
        String fileName = "standard_rate_card.xlsx";
        upload(file, url, fileName);
        int importedRows = template.queryForObject("SELECT count(*) FROM standard_rate_card", Integer.class);
        Assertions.assertEquals(4554, importedRows);
    }

    private void upload(URL file, String url, String fileName) throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName,
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Files.readAllBytes(Paths.get(file.toURI())));

        Map<String, String> contentTypeParams = new HashMap<String, String>();
        contentTypeParams.put("boundary", "------SomeBoundary");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
            .multipart(url)
            .file(mockMultipartFile)
            .contentType(mediaType);

        MvcResult resultActions = mockMvc
            .perform(builder)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(request().asyncStarted())
            .andReturn();

        mockMvc.perform(asyncDispatch(resultActions))
            .andExpect(status().is2xxSuccessful());
    }

}
