package com.gogidix.dashboard.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Tenant context holder for multi-tenant requests.
 * Provides tenant-specific information throughout the request lifecycle.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantContext {

    private String tenantId;

    private String tenantName;

    private String userId;

    private String username;

    private String userRole;

    private LocalDateTime requestTimestamp;

    @Builder.Default
    private String correlationId = java.util.UUID.randomUUID().toString();

    private Map<String, Object> metadata;

    /**
     * Thread-local storage for tenant context
     */
    private static final ThreadLocal<TenantContext> CONTEXT = new ThreadLocal<>();

    /**
     * Set the current tenant context
     */
    public static void setContext(TenantContext context) {
        CONTEXT.set(context);
    }

    /**
     * Get the current tenant context
     */
    public static TenantContext getContext() {
        return CONTEXT.get();
    }

    /**
     * Get the current tenant ID
     */
    public static String getCurrentTenantId() {
        TenantContext context = CONTEXT.get();
        return context != null ? context.getTenantId() : null;
    }

    public static String getCurrentUserId() {
        TenantContext context = CONTEXT.get();
        return context != null ? context.getUserId() : null;
    }

    /**
     * Clear the current tenant context
     */
    public static void clearContext() {
        CONTEXT.remove();
    }

    /**
     * Create a builder with tenant ID pre-filled
     */
    public static TenantContextBuilder builder(String tenantId) {
        return new TenantContextBuilder().tenantId(tenantId);
    }
}
