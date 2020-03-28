package org.bmsource.dwh.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bmsource.dwh.security.model.UserDto;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityApiIT {
    private static final String CLIENT_ID = "dwh-client";
    private static final String CLIENT_SECRET = "secret";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "$2a$04$EZzbSqieYfe/nFWfBWt2KeCdyq0UuDEM1ycFF8HzmlVR6sbsOnw7u";
    private boolean printRest = true;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    JdbcTemplate template;

    @BeforeAll
    public void before() {
        mvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .alwaysDo(doPrint())
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    @Sql(scripts = "/test_setup.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/test_teardown.sql", executionPhase = AFTER_TEST_METHOD)
    public void obtainJWTTokenUsingPasswordGrantFlow() throws Exception {
        String accessToken = getAccessToken("admin", "admin");
        String decodedJwt = JwtHelper.decode(accessToken).getClaims();
        JSONObject jsonToken = new JSONObject(decodedJwt);
        Assert.assertEquals("admin", jsonToken.getString("user_name"));
        Assert.assertEquals("dwh-client", jsonToken.getString("client_id"));
    }

    @Test
    @Sql(scripts = "/test_setup.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/test_teardown.sql", executionPhase = AFTER_TEST_METHOD)
    @WithMockUser(username = ADMIN_USERNAME, password = ADMIN_PASSWORD, roles = "USER")
    public void currentLoggedUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders
            .get("/me")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("admin"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.authorities").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true));
    }

    @Test
    public void testWrongCredentials() throws Exception {
        ResultActions result = obtainToken("invalid", "user");
        result.andExpect(status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("invalid_grant"));
    }

    @Test
    public void registerUser() throws Exception {
        UserDto user = new UserDto();
        user.setFirstName("foo");
        user.setLastName("bar");
        user.setUsername("foobar");
        user.setPassword("foobar");
        user.setEmail("foo@bar.com");

        mvc.perform(MockMvcRequestBuilders
            .post("/register")
            .content(new ObjectMapper().writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());

        String token = getAccessToken("foobar", "foobar");

        mvc.perform(MockMvcRequestBuilders
            .get("/me")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());
    }

    private String getAccessToken(String username, String password) throws Exception {
        ResultActions result = obtainToken(username, password);
        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    private ResultActions obtainToken(String username, String password) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("password", password);

        return mvc.perform(MockMvcRequestBuilders.post("/oauth/token")
            .params(params)
            .with(SecurityMockMvcRequestPostProcessors.httpBasic(CLIENT_ID, CLIENT_SECRET))
            .accept(MediaType.APPLICATION_JSON));
    }

    private ResultHandler doPrint() {
        if (printRest)
            return MockMvcResultHandlers.print();
        return result -> {
        };
    }
}
