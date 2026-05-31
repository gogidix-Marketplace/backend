package com.gogidix.finance.forecasting.shared.requestcontext;

import java.util.Optional;

/**
 * Request Context Holder
 * Thread-local holder for the current request context
 * Ensures tenant isolation throughout request processing
 */
public final class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    private RequestContextHolder() {
        // Utility class - prevent instantiation
    }

    /**
     * Set the request context for the current thread
     */
    public static void set(RequestContext context) {
        CONTEXT.set(context);
    }

    /**
     * Get the request context for the current thread
     */
    public static Optional<RequestContext> get() {
        return Optional.ofNullable(CONTEXT.get());
    }

    /**
     * Get the request context or throw if not set
     */
    public static RequestContext require() {
        return get().orElseThrow(() -> new IllegalStateException("RequestContext not set"));
    }

    /**
     * Clear the request context for the current thread
     */
    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * Get the tenant ID from the current context
     */
    public static String getTenantId() {
        return require().tenantId();
    }

    /**
     * Get the user ID from the current context
     */
    public static Optional<String> getUserId() {
        return get().map(RequestContext::userId);
    }

    /**
     * Get the correlation ID from the current context
     */
    public static String getCorrelationId() {
        return require().correlationId();
    }
}
