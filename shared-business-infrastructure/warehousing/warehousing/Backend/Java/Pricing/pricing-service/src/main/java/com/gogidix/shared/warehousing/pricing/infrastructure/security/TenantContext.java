package com.gogidix.shared.warehousing.pricing.infrastructure.security;

/**
 * Tenant Context
 *
 * Thread-local storage for tenant ID in multi-tenant architecture
 */
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    private static final String DEFAULT_TENANT = "public";

    /**
     * Set current tenant ID for this thread
     */
    public static void setTenantId(String tenantId) {
        if (tenantId == null || tenantId.isEmpty()) {
            CURRENT_TENANT.set(DEFAULT_TENANT);
        } else {
            CURRENT_TENANT.set(tenantId);
        }
    }

    /**
     * Get current tenant ID for this thread
     */
    public static String getCurrentTenantId() {
        String tenantId = CURRENT_TENANT.get();
        return tenantId != null ? tenantId : DEFAULT_TENANT;
    }

    /**
     * Clear tenant context for this thread
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
