package org.bmsource.dwh.common.portal;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TenantRepository {
    private List<String> tenants = Arrays.asList("0000-0000-0000-0001");

    public List<String> getTenants() {
        return tenants;
    }
}
