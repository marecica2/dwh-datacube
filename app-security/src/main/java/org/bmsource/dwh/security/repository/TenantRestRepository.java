package org.bmsource.dwh.security.repository;

import org.bmsource.dwh.common.portal.Tenant;
import org.bmsource.dwh.common.portal.TenantProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Repository
@RepositoryRestResource(collectionResourceRel = "tenants", path = "tenants", excerptProjection = TenantProjection.class)
public interface TenantRestRepository extends JpaRepository<Tenant, String> {
}
