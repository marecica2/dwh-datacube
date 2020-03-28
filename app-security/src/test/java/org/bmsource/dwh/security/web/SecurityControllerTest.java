package org.bmsource.dwh.security.web;

import org.bmsource.dwh.common.portal.TenantRepository;
import org.bmsource.dwh.security.UserServiceImpl;
import org.bmsource.dwh.security.repository.RoleRepository;
import org.bmsource.dwh.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SecurityController.class)
public class SecurityControllerTest {

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private TenantRepository tenantRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void handleException() throws Exception {
        when(userService.loadUserByUsername(any())).thenThrow(new RuntimeException("Exception"));
        mvc.perform(
            MockMvcRequestBuilders
                .get("/me")
        )
            .andExpect(status().is4xxClientError());
    }
}
