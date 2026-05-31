package com.gogidix.shared.servicediscovery.config;

/**
 * Tenant Context Holder
 *
 * <p>Thread-local storage for tenant context throughout the request lifecycle.
 * This context is propagated to all downstream service calls via interceptors.</p>
 *
 * @author Foundation Team
 * @version 1.0.0
 */
public class TenantContext {

    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> DOMAIN = new ThreadLocal<>();
    private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ROLES = new ThreadLocal<>();

    /**
     * Set tenant ID for current request
     */
    public static void setTenantId(String tenantId) {
        TENANT_ID.set(tenantId);
    }

    /**
     * Get tenant ID for current request
     */
    public static String getTenantId() {
        return TENANT_ID.get();
    }

    /**
     * Set domain for current request
     */
    public static void setDomain(String domain) {
        DOMAIN.set(domain);
    }

    /**
     * Get domain for current request
     */
    public static String getDomain() {
        return DOMAIN.get();
    }

    /**
     * Set correlation ID for current request
     */
    public static void setCorrelationId(String correlationId) {
        CORRELATION_ID.set(correlationId);
    }

    /**
     * Get correlation ID for current request
     */
    public static String getCorrelationId() {
        return CORRELATION_ID.get();
    }

    /**
     * Set user ID for current request
     */
    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    /**
     * Get user ID for current request
     */
    public static String getUserId() {
        return USER_ID.get();
    }

    /**
     * Set user roles for current request
     */
    public static void setUserRoles(String roles) {
        USER_ROLES.set(roles);
    }

    /**
     * Get user roles for current request
     */
    public static String getUserRoles() {
        return USER_ROLES.get();
    }

    /**
     * Clear all context (typically at end of request)
     */
    public static void clear() {
        TENANT_ID.remove();
        DOMAIN.remove();
        CORRELATION_ID.remove();
        USER_ID.remove();
        USER_ROLES.remove();
    }

    /**
     * Get a snapshot of current context as a map
     */
    public static java.util.Map<String, String> getSnapshot() {
        java.util.Map<String, String> snapshot = new java.util.HashMap<>();
        if (TENANT_ID.get() != null) snapshot.put("tenantId", TENANT_ID.get());
        if (DOMAIN.get() != null) snapshot.put("domain", DOMAIN.get());
        if (CORRELATION_ID.get() != null) snapshot.put("correlationId", CORRELATION_ID.get());
        if (USER_ID.get() != null) snapshot.put("userId", USER_ID.get());
        if (USER_ROLES.get() != null) snapshot.put("userRoles", USER_ROLES.get());
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
