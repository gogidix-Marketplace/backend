package com.gogidix.shared.infrastructure.core.tenancy.context;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Thread-local holder for tenant context.
 * <p>
 * Maintains tenant ID throughout request processing using ThreadLocal storage.
 * Each request thread gets its own isolated tenant context.
 * <p>
 * Usage:
 * <pre>
 * // Set tenant ID (typically done by TenantInterceptor)
 * tenantContextHolder.setTenantId("tenant-123");
 *
 * // Get tenant ID
 * String tenantId = tenantContextHolder.getRequiredTenantId();
 *
 * // Clear tenant context (typically done after request completes)
 * tenantContextHolder.clear();
 * </pre>
 */
@Component
public class TenantContextHolder {

    private static final ThreadLocal<String> TENANT_CONTEXT = new ThreadLocal<>();

    /**
     * Sets the tenant ID for the current request thread.
     *
     * @param tenantId the tenant identifier, must not be null or empty
     * @throws IllegalArgumentException if tenantId is null or empty
     */
    public void setTenantId(String tenantId) {
        if (tenantId == null || tenantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant ID cannot be null or empty");
        }
        TENANT_CONTEXT.set(tenantId.trim());
    }

    /**
     * Gets the tenant ID for the current request thread.
     *
     * @return Optional containing the tenant ID if present, empty otherwise
     */
    public Optional<String> getTenantId() {
        return Optional.ofNullable(TENANT_CONTEXT.get());
    }

    /**
     * Gets the tenant ID for the current request thread.
     *
     * @return the tenant ID
     * @throws IllegalStateException if tenant ID is not set in context
     */
    public String getRequiredTenantId() {
        return getTenantId()
                .orElseThrow(() -> new IllegalStateException("Tenant ID not set in context"));
    }

    /**
     * Clears the tenant context for the current thread.
     * Should be called after request processing completes to prevent memory leaks.
     */
    public void clear() {
        TENANT_CONTEXT.remove();
    }

    /**
     * Checks if a tenant ID is set in the current context.
     *
     * @return true if tenant ID is present, false otherwise
     */
    public boolean hasTenantId() {
        return TENANT_CONTEXT.get() != null;
    }
}
