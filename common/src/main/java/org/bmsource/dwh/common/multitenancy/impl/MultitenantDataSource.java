package org.bmsource.dwh.common.multitenancy.impl;

import org.bmsource.dwh.common.multitenancy.TenantContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultitenantDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getTenantSchema();
    }
}
