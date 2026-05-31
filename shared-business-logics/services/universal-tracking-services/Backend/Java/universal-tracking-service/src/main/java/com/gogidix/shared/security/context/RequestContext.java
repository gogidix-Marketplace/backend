package com.gogidix.shared.security.context;

import java.util.Optional;
import java.util.UUID;

/**
 * Request context holder for tenant and user information.
 */
public class RequestContext {

    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();

    /**
     * Set tenant ID for current request
     */
    public static void setTenantId(String tenantId) {
        TENANT_ID.set(tenantId);
    }

    /**
     * Get tenant ID for current request
     */
    public static Optional<String> getTenantId() {
        return Optional.ofNullable(TENANT_ID.get());
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
    public static Optional<String> getUserId() {
        return Optional.ofNullable(USER_ID.get());
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
    public static Optional<String> getCorrelationId() {
        return Optional.ofNullable(CORRELATION_ID.get());
    }

    /**
     * Generate and set a new correlation ID
     */
    public static String generateCorrelationId() {
        String correlationId = UUID.randomUUID().toString();
        setCorrelationId(correlationId);
        return correlationId;
    }

    /**
     * Clear all context data
     */
    public static void clear() {
        TENANT_ID.remove();
        USER_ID.remove();
        CORRELATION_ID.remove();
    }
}
