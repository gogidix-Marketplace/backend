package com.gogidix.shared.warehousing.warehouse.analytics.infrastructure.security;

/**
 * Tenant Context
 *
 * Holds the current tenant ID for the request context
 */
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    /**
     * Set the current tenant ID
     */
    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Get the current tenant ID
     */
    public static String getCurrentTenantId() {
        String tenantId = CURRENT_TENANT.get();
        if (tenantId == null) {
            return "public"; // Default tenant
        }
        return tenantId;
    }

    /**
     * Clear the current tenant ID
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
