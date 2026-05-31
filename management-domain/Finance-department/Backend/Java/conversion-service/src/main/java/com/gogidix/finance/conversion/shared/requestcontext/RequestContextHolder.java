package com.gogidix.finance.conversion.shared.requestcontext;

/**
 * Request Context Holder
 * Thread-local storage for request context in multi-tenant environment
 */
public final class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    private RequestContextHolder() {
    }

    /**
     * Sets the request context for the current thread
     */
    public static void set(RequestContext context) {
        CONTEXT.set(context);
    }

    /**
     * Gets the request context for the current thread
     */
    public static RequestContext get() {
        return CONTEXT.get();
    }

    /**
     * Gets the tenant ID from the current request context
     */
    public static String getTenantId() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("RequestContext not set");
        }
        return context.getTenantId();
    }

    /**
     * Gets the user ID from the current request context
     */
    public static String getUserId() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("RequestContext not set");
        }
        return context.getUserId();
    }

    /**
     * Gets the correlation ID from the current request context
     */
    public static String getCorrelationId() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("RequestContext not set");
        }
        return context.getCorrelationId();
    }

    /**
     * Clears the request context for the current thread
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
