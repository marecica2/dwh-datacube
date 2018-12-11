package org.bmsource.dwh.multitenancy.database.repositories;

import org.bmsource.dwh.multitenancy.database.entities.Tenant;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TenantRepository extends PagingAndSortingRepository<Tenant,String> {

}
