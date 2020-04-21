package org.bmsource.dwh.security.repository;

import org.bmsource.dwh.security.model.Role;
import org.bmsource.dwh.security.model.RoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Repository
@RepositoryRestResource(collectionResourceRel = "roles", path = "roles", excerptProjection = RoleProjection.class)
public interface RoleRestRepository extends JpaRepository<Role, Long> {
}
