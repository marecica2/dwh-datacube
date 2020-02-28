package org.bmsource.dwh.common.portal;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TenantRepository {

    List<Tenant> findAll();

    Optional<Tenant> findById(String id);

}
