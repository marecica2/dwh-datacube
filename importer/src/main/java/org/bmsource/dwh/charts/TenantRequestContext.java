package org.bmsource.dwh.charts;

public class TenantRequestContext {

    private static ThreadLocal<String> tenant = new ThreadLocal<>();
    private static ThreadLocal<String> project = new ThreadLocal<>();

    public static void setTenant(String tenant) {
        TenantRequestContext.tenant.set(tenant);
    }

    public static String getTenant() {
        return tenant.get();
    }

    public static String getProject() {
        return project.get();
    }

    public static void setProject(String project) {
        TenantRequestContext.project.set(project);
    }
}
