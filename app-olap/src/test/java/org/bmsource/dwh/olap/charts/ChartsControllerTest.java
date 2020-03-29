package org.bmsource.dwh.olap.charts;

import org.bmsource.dwh.common.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ChartController.class, ChartsExceptionHandler.class, TenantRequestFilter.class })
@WebMvcTest(controllers = ChartController.class)
public class ChartsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    ChartRepositoryImpl repository;

    @Test
    @WithMockUser()
    public void handleException() throws Exception {
        when(repository.queryAggregate(any(), any(), any(), any(), any())).thenThrow(new RuntimeException("Exception"));
        mvc.perform(MockMvcRequestBuilders
            .get("/charts")
            .header("x-tenant", TestUtils.TENANT1)
            .param("projectId", "1")
            .param("dimensions", "foo")
            .param("measures", "foo")
            .param("sorts", "bar"))
            .andExpect(status().is5xxServerError());
    }
}
