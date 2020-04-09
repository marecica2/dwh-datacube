package org.bmsource.dwh.security.repository;

import org.bmsource.dwh.common.portal.Tenant;
import org.bmsource.dwh.security.model.Role;

import java.util.Set;

public interface UserProjection {
    long getId();
    String getFirstName();
    String getLastName();
    String getUsername();
    String getEmail();
    Set<Role> getRoles();
    Set<Tenant> getTenants();
}
