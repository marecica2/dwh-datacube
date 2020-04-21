package org.bmsource.dwh.migrator;

import org.bmsource.dwh.common.portal.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, String> {
}
