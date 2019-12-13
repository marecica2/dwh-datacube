package org.bmsource.dwh.charts;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter("/*")
public class TenantFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        ThreadLocalStorage.setTenant(httpRequest.getHeader("x-tenant"));
        String[] urlParts = httpRequest.getRequestURI().split("/");
        if(urlParts.length > 0) {
            ThreadLocalStorage.setProject(urlParts[0]);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}