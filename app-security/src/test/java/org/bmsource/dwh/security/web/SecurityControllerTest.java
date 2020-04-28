package org.bmsource.dwh.security.web;

import org.bmsource.dwh.security.UserServiceImpl;
import org.bmsource.dwh.security.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
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
    private RoleRestRepository roleRestRepository;

    @MockBean
    private UserRestRepository userRestRepository;

    @MockBean
    private TenantRestRepository tenantRepository;

    @MockBean
    private InternalUserRepository userRepository;

    @MockBean
    private InternalRoleRepository roleRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser()
    public void handleException() throws Exception {
        when(userRepository.findByUsername(any())).thenThrow(new RuntimeException("Exception"));
        mvc.perform(MockMvcRequestBuilders
            .get("/me"))
            .andExpect(status().is5xxServerError());
    }
}
