package com.github.ankurpathak.api.batch.task;



import com.github.ankurpathak.api.security.dto.DomainContext;

import java.util.Optional;

public class TaskContextHolder {
    private static final ThreadLocal<ITaskContext> contextHolder = new InheritableThreadLocal<>();
    private static int initializeCount = 0;


    public TaskContextHolder() {
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    public static Optional<ITaskContext> getContext() {
        return Optional.ofNullable(contextHolder.get());
    }

    public static int getInitializeCount() {
        return initializeCount;
    }

    private static void initialize() {
        ++initializeCount;
    }

    public static void setContext(ITaskContext context) {
        contextHolder.set(context);
    }


    static {
        initialize();
    }
}
