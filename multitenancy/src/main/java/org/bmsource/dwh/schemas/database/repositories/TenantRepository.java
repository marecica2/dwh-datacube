package org.bmsource.dwh.schemas.database.repositories;

import org.bmsource.dwh.schemas.database.entities.Tenant;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TenantRepository extends PagingAndSortingRepository<Tenant,String> {

}
