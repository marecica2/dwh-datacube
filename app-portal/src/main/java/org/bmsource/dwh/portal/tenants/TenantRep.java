package org.bmsource.dwh.portal.tenants;

import org.bmsource.dwh.common.portal.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "tenants", path = "tenants")
public interface TenantRep extends JpaRepository<Tenant, String> {
}
