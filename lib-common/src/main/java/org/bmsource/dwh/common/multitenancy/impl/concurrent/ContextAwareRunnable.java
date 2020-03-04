package org.bmsource.dwh.common.multitenancy.impl.concurrent;

import org.bmsource.dwh.common.multitenancy.TenantContext;

public class ContextAwareRunnable implements Runnable {

    private Runnable task;
    private String tenant;

    public ContextAwareRunnable(Runnable task, String tenant) {
        this.task = task;
        this.tenant = tenant;
    }

    @Override
    public void run() {
        if (tenant != null) {
            TenantContext.setTenantSchema(tenant);
        }
        try {
            task.run();
        } finally {
            TenantContext.remove();
        }
    }
}
