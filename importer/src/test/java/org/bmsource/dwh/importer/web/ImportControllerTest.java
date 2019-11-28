package org.bmsource.dwh.importer.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bmsource.dwh.ImporterApplication;
import org.bmsource.dwh.importer.ImporterConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("unit-test")
@Component
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ImporterApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImportControllerTest {

    private String tenant = "000000-00000-00001";
    private String project = "1";

    private List<String> files = new ArrayList<String>() {{
        add("/spends.xlsx");
    }};

    private Map<String, String> mapping = new LinkedHashMap<String, String>() {{
        put("Service Type", "serviceType");
        put("Business Unit", "businessUnit");
        put("Supplier Name", "supplierName");
        put("S. No.", "transactionId");
        put("Origin-City", "originCity");
        put("Zone", "zone");
        put("Cost (USD)", "cost");
        put("Billable Weight (lb)", "billableWeight");
    }};

    @Bean
    MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        mvc = webAppContextSetup(this.wac).build();
    }

    @Autowired
    JdbcTemplate template;

    @Test
    public void testImport() throws Exception {
        MvcResult transactionResult = mvc.perform(MockMvcRequestBuilders
            .get("/{projectId}/import", project)
            .header("x-tenant", tenant)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn();
        String transactionId = transactionResult.getResponse().getContentAsString();
        UUID uuid = UUID.fromString(transactionId);
        Assertions.assertNotNull(uuid);

        MockMultipartFile mockMultipartFile = getMultipartFile();
        MediaType mediaType = getMediaType();

        mvc.perform(MockMvcRequestBuilders
            .multipart("/{projectId}/import/{transactionId}", project, transactionId)
            .file(mockMultipartFile)
            .header("x-tenant", tenant)
            .contentType(mediaType))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(content().json(new JSONArray().put(files.get(0)).toString()));

        mvc.perform(MockMvcRequestBuilders
            .post("/{projectId}/import/{transactionId}/mapping", project, transactionId)
            .header("x-tenant", tenant)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.sourceColumns").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.sourceColumns.*", hasSize(38)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.sourceColumns['Supplier Name']").value("UPS"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.destinationColumns").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.destinationColumns.*", hasSize(29)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.destinationColumns.transactionId").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.destinationColumns.transactionId.type").value("String"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.destinationColumns.transactionId.label").value("Transaction " +
                "Id"));

        mvc.perform(MockMvcRequestBuilders
            .post("/{projectId}/import/{transactionId}/preview", project, transactionId)
            .header("x-tenant", tenant)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JSONObject().put("mapping", new JSONObject(mapping)).toString()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(100)));

        mvc.perform(MockMvcRequestBuilders
            .post("/{projectId}/import/{transactionId}/start", project, transactionId)
            .header("x-tenant", tenant)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new JSONObject().put("mapping", new JSONObject(mapping)).toString()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());

        waitUntilCompleted();

        mvc.perform(MockMvcRequestBuilders
            .get("/{projectId}/import/stats", project)
            .header("x-tenant", tenant)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("COMPLETED"));
    }

    private void waitUntilCompleted() {
        String status = null;
        while (status == null || "STARTED".equals(status)) {
            try {
                Thread.sleep(500);
                MvcResult result = mvc.perform(MockMvcRequestBuilders
                    .get("/{projectId}/import/stats", project)
                    .header("x-tenant", tenant)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();
                status = new JSONObject(result.getResponse().getContentAsString()).getString("status");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private MockMultipartFile getMultipartFile() throws IOException, URISyntaxException {
        URL file = this.getClass().getResource(files.get(0));
        return new MockMultipartFile("file", files.get(0),
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            Files.readAllBytes(Paths.get(file.toURI())));
    }

    private MediaType getMediaType() {
        Map<String, String> contentTypeParams = new HashMap<String, String>();
        contentTypeParams.put("boundary", "------SomeBoundary");
        return new MediaType("multipart", "form-data", contentTypeParams);
    }
}
