package org.bmsource.dwh.common.multitenancy;

public class TenantContext {

    private static ThreadLocal<String> tenantSchema = new ThreadLocal<>();

    public static String getTenantSchema() {
        return tenantSchema.get();
    }

    public static void remove() {
        tenantSchema.remove();
    }

    public static void setTenantSchema(String uuid) {
        tenantSchema.set(uuid);
    }
}
