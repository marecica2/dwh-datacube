package org.bmsource.dwh.common.portal;

import java.util.List;
import java.util.Optional;

public interface TenantRepository {

    List<Tenant> findAll();

    Optional<Tenant> findById(String id);

}
