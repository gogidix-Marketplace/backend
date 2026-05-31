package com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Thread-local holder for the tenant context.
 * Maintains tenant information throughout the request lifecycle.
 */
@Slf4j
public class TenantContextHolder {

    private static final ThreadLocal<TenantContext> CONTEXT = new ThreadLocal<>();

    /**
     * Set the tenant context for the current thread.
     *
     * @param context the tenant context to set
     */
    public static void setContext(TenantContext context) {
        Assert.notNull(context, "TenantContext must not be null");
        if (context.getTenantId() != null) {
            log.debug("Setting tenant context: tenantId={}, userId={}, correlationId={}",
                context.getTenantId(), context.getUserId(), context.getCorrelationId());
        }
        CONTEXT.set(context);
    }

    /**
     * Get the tenant context for the current thread.
     *
     * @return the tenant context, or null if not set
     */
    public static TenantContext getContext() {
        return CONTEXT.get();
    }

    /**
     * Get the tenant context as an Optional.
     *
     * @return Optional containing the context, or empty if not set
     */
    public static Optional<TenantContext> getOptionalContext() {
        return Optional.ofNullable(CONTEXT.get());
    }

    /**
     * Get the current tenant ID.
     *
     * @return the tenant ID, or null if not set
     */
    public static String getTenantId() {
        TenantContext context = CONTEXT.get();
        return context != null ? context.getTenantId() : null;
    }

    /**
     * Get the current user ID.
     *
     * @return the user ID, or null if not set
     */
    public static String getUserId() {
        TenantContext context = CONTEXT.get();
        return context != null ? context.getUserId() : null;
    }

    /**
     * Get the current correlation ID.
     *
     * @return the correlation ID, or null if not set
     */
    public static String getCorrelationId() {
        TenantContext context = CONTEXT.get();
        return context != null ? context.getCorrelationId() : null;
    }

    /**
     * Check if a tenant context is available.
     *
     * @return true if context is set, false otherwise
     */
    public static boolean hasContext() {
        return CONTEXT.get() != null;
    }

    /**
     * Require a tenant context to be present.
     * Throws IllegalStateException if no context is set.
     *
     * @return the tenant context
     * @throws IllegalStateException if no context is set
     */
    public static TenantContext requireContext() {
        TenantContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("TenantContext not set. Make sure TenantContextRequestFilter is properly configured.");
        }
        return context;
    }

    /**
     * Clear the tenant context for the current thread.
     * Should be called at the end of request processing.
     */
    public static void clearContext() {
        TenantContext context = CONTEXT.get();
        if (context != null) {
            log.debug("Clearing tenant context: tenantId={}", context.getTenantId());
        }
        CONTEXT.remove();
    }
}
