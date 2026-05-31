package com.gogidix.aiservices.aimarketbasketanalysisservice.shared.context;

/**
 * Holder for the request context in a ThreadLocal.
 */
public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    private RequestContextHolder() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Set the request context for the current thread.
     */
    public static void setContext(RequestContext context) {
        CONTEXT.set(context);
    }

    /**
     * Get the request context for the current thread.
     */
    public static RequestContext getContext() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("RequestContext not set. Use RequestContext.create() to set it.");
        }
        return context;
    }

    /**
     * Get the request context or return empty if not set.
     */
    public static java.util.Optional<RequestContext> getOptionalContext() {
        return java.util.Optional.ofNullable(CONTEXT.get());
    }

    /**
     * Clear the request context for the current thread.
     */
    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * Get the tenant ID from the current context.
     */
    public static String getTenantId() {
        return getContext().tenantId();
    }

    /**
     * Get the user ID from the current context.
     */
    public static String getUserId() {
        return getContext().userId();
    }

    /**
     * Get the correlation ID from the current context.
     */
    public static String getCorrelationId() {
        return getContext().correlationId();
    }
}
