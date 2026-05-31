package com.gogidix.aiservices.intelligenceanalysisservice.shared.requestcontext;

import java.util.Optional;

/**
 * ThreadLocal holder for RequestContext.
 * Context is set by TenantInterceptor and cleared after request completes.
 * Provides access to tenant context throughout all application layers.
 */
public final class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    private RequestContextHolder() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Set the context for the current thread.
     * Called by TenantInterceptor before request processing.
     *
     * @param context the request context to set
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
     * Use this when context is required for business logic.
     *
     * @return the current RequestContext
     * @throws IllegalStateException if context is not set
     */
    public static RequestContext require() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException(
                    "RequestContext not set. TenantInterceptor must run first."
            );
        }
        return context;
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
     * Convenience method for common use case.
     *
     * @return the current tenant ID
     * @throws IllegalStateException if context is not set
     */
    public static String getTenantId() {
        return require().tenantId();
    }

    /**
     * Get userId from current context.
     * Convenience method for common use case.
     *
     * @return Optional containing the current user ID, or empty if not set
     */
    public static Optional<String> getUserId() {
        return get().map(RequestContext::userId);
    }

    /**
     * Get correlationId from current context.
     * Convenience method for common use case.
     *
     * @return the current correlation ID
     * @throws IllegalStateException if context is not set
     */
    public static String getCorrelationId() {
        return require().correlationId();
    }

    /**
     * Check if context is set.
     *
     * @return true if context is set, false otherwise
     */
    public static boolean isSet() {
        return CONTEXT.get() != null;
    }
}
