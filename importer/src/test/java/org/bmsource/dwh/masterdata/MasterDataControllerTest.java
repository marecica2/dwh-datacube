package org.bmsource.dwh.masterdata;

import org.bmsource.dwh.common.importer.ImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Component
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MasterDataController.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MasterDataControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testDeferred() throws Exception {
        URL file = this.getClass().getResource("/zipcode_locations.xlsx");
        String url = "/1/master-data/zip-code-location/import";

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file",
            Files.readAllBytes(Paths.get(file.toURI())));

        Map<String, String> contentTypeParams = new HashMap<String, String>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
            .multipart(url)
            .file(mockMultipartFile)
            .contentType(mediaType);

        MvcResult resultActions = mockMvc
            .perform(builder)
            .andExpect(request().asyncStarted())
            .andReturn();

        System.out.println();

        mockMvc.perform(asyncDispatch(resultActions))
            .andExpect(status().is2xxSuccessful());
    }

}
