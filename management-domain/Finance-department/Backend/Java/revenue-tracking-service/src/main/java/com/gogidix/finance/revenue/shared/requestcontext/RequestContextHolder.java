package com.gogidix.finance.revenue.shared.requestcontext;

import java.util.Optional;

/**
 * Request Context Holder
 * Thread-local storage for request context
 */
public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    public static void set(RequestContext context) {
        CONTEXT.set(context);
    }

    public static RequestContext get() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("RequestContext not set");
        }
        return context;
    }

    public static Optional<RequestContext> getOptional() {
        return Optional.ofNullable(CONTEXT.get());
    }

    public static String getTenantId() {
        return get().getTenantId();
    }

    public static String getUserId() {
        return get().getUserId();
    }

    public static String getCorrelationId() {
        return get().getCorrelationId();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
