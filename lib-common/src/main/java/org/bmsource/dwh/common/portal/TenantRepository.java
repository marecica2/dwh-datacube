package org.bmsource.dwh.common.portal;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository {

    List<Tenant> findAll();

    Optional<Tenant> findById(String id);

}
