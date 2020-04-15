package org.bmsource.dwh.common.portal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "tenants", path = "tenants", excerptProjection = TenantProjection.class)
public interface TenantRepository extends JpaRepository<Tenant, String> {
}
