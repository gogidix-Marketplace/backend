package com.gogidix.shared.infrastructure.core.tenancy.context;

/**
 * TenantContextHolder - Thread-local context for storing current tenant.
 * Local implementation for sla-management-service independence.
 */
public class TenantContextHolder {
    private static final ThreadLocal<String> TENANT_CONTEXT = new ThreadLocal<>();

    /**
     * Set the current tenant ID in the context.
     *
     * @param tenantId the tenant ID
     */
    public static void setTenantId(String tenantId) {
        TENANT_CONTEXT.set(tenantId);
    }

    /**
     * Get the current tenant ID from the context.
     *
     * @return the tenant ID, or null if not set
     */
    public static String getTenantId() {
        return TENANT_CONTEXT.get();
    }

    /**
     * Clear the current tenant ID from the context.
     */
    public static void clear() {
        TENANT_CONTEXT.remove();
    }

    /**
     * Check if a tenant ID is set in the context.
     *
     * @return true if a tenant ID is set, false otherwise
     */
    public static boolean hasTenantId() {
        return TENANT_CONTEXT.get() != null;
    }

    public static String getRequiredTenantId() {
        String tenantId = TENANT_CONTEXT.get();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID not set in context");
        }
        return tenantId;
    }
}
