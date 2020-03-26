package org.bmsource.dwh.migrator;

import org.bmsource.dwh.common.portal.Tenant;
import org.bmsource.dwh.common.portal.TenantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MigratorController.class)
public class MigratorControllerTest {

    boolean printRest = true;

    @MockBean
    private Migrator migrator;

    @MockBean
    private TenantRepository repository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void triggerMigrations() throws Exception {
        mvc.perform(
            MockMvcRequestBuilders
                .post("/migrate")
        )
            .andDo(doPrint())
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void triggerAddTenant() throws Exception {
        Tenant tenant = new Tenant();
        tenant.setId("abc");
        when(repository.findById("abc")).thenReturn(Optional.of(tenant));
        when(repository.findById("cde")).thenReturn(Optional.empty());
        mvc.perform(
            MockMvcRequestBuilders
                .post("/migrate/tenant/{id}", "abc")
        )
            .andDo(doPrint())
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void triggerAddTenantForInvalidTenant() throws Exception {
        when(repository.findById("cde")).thenReturn(Optional.empty());
        mvc.perform(
            MockMvcRequestBuilders
                .post("/migrate/tenant/{id}", "cde")
        )
            .andDo(doPrint())
            .andExpect(status().is4xxClientError());
    }

    private ResultHandler doPrint() {
        if (printRest)
            return MockMvcResultHandlers.print();
        return result -> {
        };
    }

}
