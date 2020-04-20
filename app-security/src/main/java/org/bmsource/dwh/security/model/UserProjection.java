package org.bmsource.dwh.security.model;

import org.bmsource.dwh.common.portal.Tenant;
import org.bmsource.dwh.common.portal.TenantProjection;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.Set;

@Projection(name = "full", types = { User.class })
public interface UserProjection {
    long getId();

    String getFirstName();

    String getLastName();

    String getUsername();

    String getEmail();

    Set<RoleProjection> getRoles();

    Set<TenantProjection> getTenants();

    Date getCreatedOn();

    Date getModifiedOn();
}
