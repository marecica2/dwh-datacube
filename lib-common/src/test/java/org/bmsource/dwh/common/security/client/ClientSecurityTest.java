package org.bmsource.dwh.common.security.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = { ClientSecurityTest.ClientApplication.class, ResourceServerConfig.class })
public class ClientSecurityTest {

    @MockBean
    RemoteTokenServices remoteTokenServices;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @BeforeAll
    public void before() {
        mvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .alwaysDo(doPrint())
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    @WithMockUser
    public void authorizedRequestToSecuredResource() throws Exception {
        mvc.perform(
            MockMvcRequestBuilders
                .get("/secured-endpoint"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    public void authorizedRequestToAdminSecuredResource() throws Exception {
        mvc.perform(
            MockMvcRequestBuilders
                .get("/admin-endpoint"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    public void authorizedAdminRequestToAdminSecuredResource() throws Exception {
        mvc.perform(
            MockMvcRequestBuilders
                .get("/admin-endpoint"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void anonymousRequestToSecuredResource() throws Exception {
        mvc.perform(
            MockMvcRequestBuilders
                .get("/secured-endpoint"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void anonymousRequestToHealthResource() throws Exception {
        mvc.perform(
            MockMvcRequestBuilders
                .get("/actuator/health"))
            .andDo(doPrint())
            .andExpect(status().is2xxSuccessful());
    }

    @Controller
    @Import({
        ClientSecurityConfig.class
    })
    public static class ClientApplication {

        @GetMapping("/secured-endpoint")
        public String protectedEndpoint() {
            return "hello";
        }

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/admin-endpoint")
        public String adminProtectedEndpoint() {
            return "hello";
        }

        @GetMapping("/actuator/health")
        public String healthEndpoint() {
            return "hello";
        }
    }

    private ResultHandler doPrint() {
        return MockMvcResultHandlers.print();
    }

    private OAuth2Authentication createToken(String user, String password, String role) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
        User userPrincipal = new User(user, password, true, true, true,
            true, authorities);
        HashMap<String, String> details = new HashMap<String, String>();
        details.put("user_name", user);
        details.put("email", user + "@example.com");
        details.put("name", user);
        TestingAuthenticationToken token = new TestingAuthenticationToken(userPrincipal, null, authorities);
        token.setAuthenticated(true);
        token.setDetails(details);
        return new OAuth2Authentication(null, token);
    }
}
