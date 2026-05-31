package com.gogidix.courier.availabilityservice.shared.context;

/**
 * Holder for the current request context using ThreadLocal.
 */
public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    /**
     * Set the current request context.
     *
     * @param context the request context
     */
    public static void setContext(RequestContext context) {
        CONTEXT.set(context);
    }

    /**
     * Get the current request context.
     *
     * @return the request context, or empty if not set
     */
    public static RequestContext getContext() {
        RequestContext context = CONTEXT.get();
        return context != null ? context : RequestContext.empty();
    }

    /**
     * Clear the current request context.
     */
    public static void clearContext() {
        CONTEXT.remove();
    }

    /**
     * Get the tenant ID from the current context.
     *
     * @return the tenant ID
     */
    public static String getTenantId() {
        return getContext().tenantId();
    }

    /**
     * Get the user ID from the current context.
     *
     * @return the user ID
     */
    public static String getUserId() {
        return getContext().userId();
    }

    /**
     * Get the correlation ID from the current context.
     *
     * @return the correlation ID
     */
    public static String getCorrelationId() {
        return getContext().correlationId();
    }
}
