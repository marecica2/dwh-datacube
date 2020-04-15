package org.bmsource.dwh.common.portal;

import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@Projection(name = "full", types = {Tenant.class})
public interface TenantProjection {
    String getId();

    String getSchemaName();

    String getDescription();

    Date getCreatedAt();

    Date getUpdatedAt();
}
