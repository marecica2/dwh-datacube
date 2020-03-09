package org.bmsource.dwh.security;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles("integration-test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SecurityApplication.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityIT {
    private boolean printRest = true;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    JdbcTemplate template;

    @Autowired
    RedisOperations<String, String> operations;

    @BeforeEach
    public void setup() {
        mvc = webAppContextSetup(this.wac).build();
    }

    @AfterEach
    public void afterEach() {
        flushRedis();
    }

    @Test
    @Sql(scripts = "/test_setup.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/test_teardown.sql", executionPhase = AFTER_TEST_METHOD)
    public void obtainJWTTokenUsingPasswordGrantFlow() throws Exception {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken("dwh-client", "secret");
        authentication.setAuthenticated(true);

        MvcResult tokenResponse = mvc.perform(MockMvcRequestBuilders
            .post("/oauth/token")
            .principal(authentication)
            .param("username", "admin")
            .param("password", "admin")
            .param("grant_type", "password")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
            .andDo(doPrint())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.token_type").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").exists()).andReturn();
        String accessToken = JsonPath.parse(tokenResponse.getResponse().getContentAsString()).read("$.access_token");
        String decodedJwt = JwtHelper.decode(accessToken).getClaims();
        Assert.assertEquals("admin", new JSONObject(decodedJwt).getString("user_name"));
        Assert.assertEquals("dwh-client", new JSONObject(decodedJwt).getString("client_id"));
    }

    private ResultHandler doPrint() {
        if (printRest)
            return MockMvcResultHandlers.print();
        return result -> {
        };
    }

    private void flushRedis() {
        operations.execute((RedisCallback<Void>) connection -> {
            connection.serverCommands().flushAll();
            return null;
        });
    }
}
