package com.gogidix.courier.assignmentservice.shared.context;

import org.slf4j.MDC;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Thread-local holder for request context.
 */
public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    private static final String TENANT_ID_KEY = "tenantId";
    private static final String USER_ID_KEY = "userId";
    private static final String CORRELATION_ID_KEY = "correlationId";

    /**
     * Set the request context for the current thread.
     */
    public static void setContext(RequestContext context) {
        CONTEXT.set(context);
        updateMDC(context);
    }

    /**
     * Get the request context for the current thread.
     */
    public static RequestContext getContext() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            return RequestContext.anonymous();
        }
        return context;
    }

    /**
     * Clear the request context for the current thread.
     */
    public static void clearContext() {
        CONTEXT.remove();
        MDC.remove(TENANT_ID_KEY);
        MDC.remove(USER_ID_KEY);
        MDC.remove(CORRELATION_ID_KEY);
    }

    /**
     * Execute a runnable with the given context.
     */
    public static void runWithContext(RequestContext context, Runnable runnable) {
        RequestContext previous = getContext();
        try {
            setContext(context);
            runnable.run();
        } finally {
            if (previous.tenantId().equals("system") && previous.userId().equals("system")) {
                clearContext();
            } else {
                setContext(previous);
            }
        }
    }

    /**
     * Execute a callable with the given context.
     */
    public static <T> T callWithContext(RequestContext context, Callable<T> callable) throws Exception {
        RequestContext previous = getContext();
        try {
            setContext(context);
            return callable.call();
        } finally {
            if (previous.tenantId().equals("system") && previous.userId().equals("system")) {
                clearContext();
            } else {
                setContext(previous);
            }
        }
    }

    /**
     * Execute a supplier with the given context.
     */
    public static <T> T supplyWithContext(RequestContext context, Supplier<T> supplier) {
        RequestContext previous = getContext();
        try {
            setContext(context);
            return supplier.get();
        } finally {
            if (previous.tenantId().equals("system") && previous.userId().equals("system")) {
                clearContext();
            } else {
                setContext(previous);
            }
        }
    }

    private static void updateMDC(RequestContext context) {
        if (context != null) {
            MDC.put(TENANT_ID_KEY, context.tenantId());
            MDC.put(USER_ID_KEY, context.userId());
            MDC.put(CORRELATION_ID_KEY, context.correlationId());
        }
    }
}
