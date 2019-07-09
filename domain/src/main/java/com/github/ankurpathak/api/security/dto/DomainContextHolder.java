package com.github.ankurpathak.api.security.dto;


import com.github.ankurpathak.api.security.dto.DomainContext;

import java.util.Optional;

public class DomainContextHolder {
    private static final ThreadLocal<DomainContext> contextHolder = new InheritableThreadLocal<>();
    private static int initializeCount = 0;


    public DomainContextHolder() {
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    public static Optional<DomainContext> getContext() {
        return Optional.ofNullable(contextHolder.get());
    }

    public static int getInitializeCount() {
        return initializeCount;
    }

    private static void initialize() {
        ++initializeCount;
    }

    public static void setContext(DomainContext context) {
        contextHolder.set(context);
    }


    static {
        initialize();
    }
}
