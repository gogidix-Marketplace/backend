package com.gogidix.shared.multitenancy.context;

import com.gogidix.shared.multitenancy.model.TenantId;

/**
 * Tenant Context Holder
 * <p>
 * Thread-local storage for tenant context throughout the request lifecycle.
 * Provides type-safe TenantId access to tenant context.
 * </p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
public final class TenantContextHolder {

    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> DOMAIN = new ThreadLocal<>();
    private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ROLES = new ThreadLocal<>();

    /**
     * Get current tenant ID as TenantId value object
     */
    public static TenantId getTenantId() {
        String tenantId = TENANT_ID.get();
        return tenantId != null ? TenantId.of(tenantId) : null;
    }

    /**
     * Get current tenant ID as raw string
     */
    public static String getTenantIdAsString() {
        return TENANT_ID.get();
    }

    /**
     * Set tenant ID from TenantId value object
     */
    public static void setTenantId(TenantId tenantId) {
        TENANT_ID.set(tenantId != null ? tenantId.stringValue() : null);
    }

    /**
     * Set tenant ID from string
     */
    public static void setTenantId(String tenantId) {
        TENANT_ID.set(tenantId);
    }

    /**
     * Get domain
     */
    public static String getDomain() {
        return DOMAIN.get();
    }

    /**
     * Set domain
     */
    public static void setDomain(String domain) {
        DOMAIN.set(domain);
    }

    /**
     * Get correlation ID
     */
    public static String getCorrelationId() {
        return CORRELATION_ID.get();
    }

    /**
     * Set correlation ID
     */
    public static void setCorrelationId(String correlationId) {
        CORRELATION_ID.set(correlationId);
    }

    /**
     * Get user ID
     */
    public static String getUserId() {
        return USER_ID.get();
    }

    /**
     * Set user ID
     */
    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    /**
     * Get user roles
     */
    public static String getUserRoles() {
        return USER_ROLES.get();
    }

    /**
     * Set user roles
     */
    public static void setUserRoles(String roles) {
        USER_ROLES.set(roles);
    }

    /**
     * Clear all tenant context
     */
    public static void clear() {
        TENANT_ID.remove();
        DOMAIN.remove();
        CORRELATION_ID.remove();
        USER_ID.remove();
        USER_ROLES.remove();
    }

    /**
     * Check if tenant context is set
     */
    public static boolean hasTenant() {
        return TENANT_ID.get() != null;
    }

    /**
     * Get a snapshot of current context as a map
     */
    public static java.util.Map<String, String> getSnapshot() {
        java.util.Map<String, String> snapshot = new java.util.HashMap<>();
        String tenantId = TENANT_ID.get();
        if (tenantId != null) snapshot.put("tenantId", tenantId);
        String domain = DOMAIN.get();
        if (domain != null) snapshot.put("domain", domain);
        String correlationId = CORRELATION_ID.get();
        if (correlationId != null) snapshot.put("correlationId", correlationId);
        String userId = USER_ID.get();
        if (userId != null) snapshot.put("userId", userId);
        String userRoles = USER_ROLES.get();
        if (userRoles != null) snapshot.put("userRoles", userRoles);
        return snapshot;
    }

    /**
     * Restore context from snapshot
     */
    public static void restoreFromSnapshot(java.util.Map<String, String> snapshot) {
        if (snapshot == null) return;
        if (snapshot.containsKey("tenantId")) TENANT_ID.set(snapshot.get("tenantId"));
        if (snapshot.containsKey("domain")) DOMAIN.set(snapshot.get("domain"));
        if (snapshot.containsKey("correlationId")) CORRELATION_ID.set(snapshot.get("correlationId"));
        if (snapshot.containsKey("userId")) USER_ID.set(snapshot.get("userId"));
        if (snapshot.containsKey("userRoles")) USER_ROLES.set(snapshot.get("userRoles"));
    }
}
