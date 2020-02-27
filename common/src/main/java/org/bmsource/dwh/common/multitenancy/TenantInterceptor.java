package org.bmsource.dwh.common.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {

    Logger logger = LoggerFactory.getLogger(getClass());

    {
        logger.debug("Creating TenantInterceptor interceptor");
    }

    @Autowired
    TenantRepository repository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantUuid = request.getHeader(Constants.TENANT_HEADER);
        String tenantSchema = tenantUuid != null ? repository.findById(tenantUuid)
            .orElseThrow(() -> new RuntimeException("Tenant not found"))
            .getSchemaName() : null;
        logger.info("Set TenantContext: {}", tenantSchema);
        TenantContext.setTenantSchema(tenantSchema);
        return true;
    }

    @Override
    public void postHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        logger.info("Clear TenantContext: {}", TenantContext.getTenantSchema());
        TenantContext.setTenantSchema(null);
    }

}
