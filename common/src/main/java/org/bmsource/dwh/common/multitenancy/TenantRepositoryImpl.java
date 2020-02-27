package org.bmsource.dwh.common.multitenancy;

import org.bmsource.dwh.common.utils.TestUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class TenantRepositoryImpl implements TenantRepository {

    private static Tenant tenant = new Tenant();
    static {
        tenant.setSchemaName(TestUtils.TENANT1);
        tenant.setTenantName(TestUtils.TENANT1);
    }

    @Override
    public List<Tenant> findAll() {
        return Collections.singletonList(tenant);
    }

    @Override
    public Optional<Tenant> findById(String id) {
        return Optional.of(tenant);
    }
}
