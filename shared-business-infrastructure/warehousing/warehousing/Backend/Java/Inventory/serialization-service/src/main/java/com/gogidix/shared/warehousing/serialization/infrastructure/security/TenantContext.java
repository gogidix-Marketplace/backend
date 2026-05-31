package com.gogidix.shared.warehousing.serialization.infrastructure.security;

/**
 * Thread-local storage for tenant context
 * Used for multi-tenant data isolation
 */
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    /**
     * Get the current tenant ID
     */
    public static String getCurrentTenantId() {
        return CURRENT_TENANT.get();
    }

    /**
     * Set the current tenant ID
     */
    public static void setCurrentTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Clear the current tenant ID
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
