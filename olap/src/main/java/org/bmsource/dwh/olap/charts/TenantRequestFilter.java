package org.bmsource.dwh.olap.charts;

import org.bmsource.dwh.common.multitenancy.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter("/*")
public class TenantRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        TenantRequestContext.setTenant(httpRequest.getHeader(Constants.TENANT_HEADER));
        if (httpRequest.getParameterMap().get("projectId") != null) {
            TenantRequestContext.setProject(httpRequest.getParameterMap().get("projectId")[0]);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
