package org.bmsource.dwh.security.model;

import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@Projection(name = "full", types = { Role.class })
public interface RoleProjection {
    long getId();

    String getName();

    String getDescription();

    Long getCreatedOn();

    Long getModifiedOn();

}
