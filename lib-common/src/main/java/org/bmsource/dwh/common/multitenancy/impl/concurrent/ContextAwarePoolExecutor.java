package org.bmsource.dwh.common.multitenancy.impl.concurrent;

import org.bmsource.dwh.common.multitenancy.TenantContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(new ContextAwareCallable(task, TenantContext.getTenantSchema()));
    }

    @Override
    public void execute(Runnable task) {
        super.execute(new ContextAwareRunnable(task, TenantContext.getTenantSchema()));
    }



    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return super.submitListenable(new ContextAwareCallable(task, TenantContext.getTenantSchema()));
    }
}
