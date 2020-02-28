package org.bmsource.dwh.common.multitenancy.impl.concurrent;

import org.bmsource.dwh.common.multitenancy.TenantContext;

import java.util.concurrent.Callable;

public class ContextAwareCallable<T> implements Callable<T> {
    private Callable<T> task;
    private String tenant;

    public ContextAwareCallable(Callable<T> task, String tenant) {
        this.task = task;
        this.tenant = tenant;
    }

    @Override
    public T call() throws Exception {
        if (tenant != null) {
            TenantContext.setTenantSchema(tenant);
        }
        try {
            return task.call();
        } finally {
            TenantContext.remove();
        }
    }
}
