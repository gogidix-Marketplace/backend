package com.gogidix.shared.servicediscovery.config.context;

import com.gogidix.shared.servicediscovery.config.TenantContext;
import com.gogidix.shared.servicediscovery.config.model.TenantId;

/**
 * Tenant Context Holder
 * <p>
 * Wrapper around TenantContext for backwards compatibility.
 * Provides type-safe TenantId access to tenant context.
 * </p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
public final class TenantContextHolder {

    /**
     * Get current tenant ID as TenantId value object
     */
    public static TenantId getTenantId() {
        String tenantId = TenantContext.getTenantId();
        return tenantId != null ? TenantId.of(tenantId) : null;
    }

    /**
     * Set tenant ID
     */
    public static void setTenantId(TenantId tenantId) {
        TenantContext.setTenantId(tenantId != null ? tenantId.stringValue() : null);
    }

    /**
     * Set tenant ID from string
     */
    public static void setTenantId(String tenantId) {
        TenantContext.setTenantId(tenantId);
    }

    /**
     * Clear all tenant context
     */
    public static void clear() {
        TenantContext.clear();
    }

    /**
     * Get domain
     */
    public static String getDomain() {
        return TenantContext.getDomain();
    }

    /**
     * Set domain
     */
    public static void setDomain(String domain) {
        TenantContext.setDomain(domain);
    }

    /**
     * Get correlation ID
     */
    public static String getCorrelationId() {
        return TenantContext.getCorrelationId();
    }

    /**
     * Set correlation ID
     */
    public static void setCorrelationId(String correlationId) {
        TenantContext.setCorrelationId(correlationId);
    }

    /**
     * Get user ID
     */
    public static String getUserId() {
        return TenantContext.getUserId();
    }

    /**
     * Set user ID
     */
    public static void setUserId(String userId) {
        TenantContext.setUserId(userId);
    }

    /**
     * Check if tenant context is set
     */
    public static boolean hasTenant() {
        return TenantContext.getTenantId() != null;
    }
}
