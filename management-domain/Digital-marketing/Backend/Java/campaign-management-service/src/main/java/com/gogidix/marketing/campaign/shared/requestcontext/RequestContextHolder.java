package com.gogidix.marketing.campaign.shared.requestcontext;

import java.util.Optional;
import java.util.UUID;

/**
 * RequestContextHolder - ThreadLocal holder for RequestContext
 *
 * <p>Context is set by TenantContextFilter and cleared after request completes.
 * This class provides convenient methods for accessing the current request context.</p>
 *
 * <p>Usage in service code:</p>
 * <pre>
 * // Get tenant ID (convenience method)
 * String tenantId = RequestContextHolder.getTenantId();
 *
 * // Get user ID (may be empty)
 * Optional&lt;String&gt; userId = RequestContextHolder.getUserId();
 *
 * // Get full context
 * RequestContext context = RequestContextHolder.require();
 * </pre>
 *
 * @see RequestContext
 */
public final class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    private RequestContextHolder() {}

    /**
     * Set the context for the current thread.
     * Called by TenantContextFilter at the start of each request.
     *
     * @param context the request context to set
     * @throws IllegalArgumentException if context is null
     */
    public static void set(RequestContext context) {
        if (context == null) {
            throw new IllegalArgumentException("RequestContext cannot be null");
        }
        CONTEXT.set(context);
    }

    /**
     * Get the context for the current thread.
     * Returns Optional.empty() if not set (should NOT happen in normal flow).
     *
     * @return Optional containing the RequestContext, or empty if not set
     */
    public static Optional<RequestContext> get() {
        return Optional.ofNullable(CONTEXT.get());
    }

    /**
     * Get the context or throw if not set.
     * Use this when context is required.
     *
     * @return the current RequestContext
     * @throws IllegalStateException if context is not set
     */
    public static RequestContext require() {
        return get().orElseThrow(() ->
            new IllegalStateException("RequestContext not set. TenantContextFilter must run first.")
        );
    }

    /**
     * Clear the context for the current thread.
     * Called after request completes to prevent memory leaks.
     */
    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * Get tenantId from current context.
     * Convenience method that throws if context not set.
     *
     * @return the current tenant ID
     * @throws IllegalStateException if context is not set
     */
    public static String getTenantId() {
        return require().tenantId();
    }

    /**
     * Get userId from current context.
     * Convenience method that returns Optional.
     *
     * @return Optional containing the user ID, or empty if not set
     */
    public static Optional<String> getUserId() {
        return get().map(RequestContext::userId);
    }

    /**
     * Get correlationId from current context.
     * Convenience method that throws if context not set.
     *
     * @return the current correlation ID
     * @throws IllegalStateException if context is not set
     */
    public static String getCorrelationId() {
        return require().correlationId();
    }

    /**
     * Get region from current context.
     *
     * @return Optional containing the region, or empty if not set
     */
    public static Optional<String> getRegion() {
        return get().map(RequestContext::region);
    }

    /**
     * Get country from current context.
     *
     * @return Optional containing the country, or empty if not set
     */
    public static Optional<String> getCountry() {
        return get().map(RequestContext::country);
    }

    /**
     * Get traceId from current context.
     *
     * @return Optional containing the trace ID, or empty if not set
     */
    public static Optional<String> getTraceId() {
        return get().map(RequestContext::traceId);
    }

    /**
     * Generate a new correlation ID if one doesn't exist in the current context.
     *
     * @return a new UUID string
     */
    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Check if context is set for the current thread.
     *
     * @return true if context is set, false otherwise
     */
    public static boolean isSet() {
        return CONTEXT.get() != null;
    }
}
