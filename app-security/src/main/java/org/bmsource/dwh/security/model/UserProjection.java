package org.bmsource.dwh.security.model;

import org.bmsource.dwh.common.portal.Tenant;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "full", types = { User.class })
public interface UserProjection {
    long getId();

    String getFirstName();

    String getLastName();

    String getUsername();

    String getEmail();

    Set<Role> getRoles();

    Set<Tenant> getTenants();
}
