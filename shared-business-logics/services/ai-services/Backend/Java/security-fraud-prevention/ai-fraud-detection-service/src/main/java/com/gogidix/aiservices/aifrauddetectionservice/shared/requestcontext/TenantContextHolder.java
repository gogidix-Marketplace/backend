package com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext;

import java.util.UUID;

/**
 * Holder for tenant context in multi-tenant environments.
 */
public final class TenantContextHolder {
    
    private static final ThreadLocal<TenantContext> CONTEXT = new ThreadLocal<>();
    
    private TenantContextHolder() {
    }
    
    public static void setTenantId(String tenantId) {
        TenantContext context = getContext();
        if (context == null) {
            context = new TenantContext();
        }
        context.setTenantId(tenantId);
        CONTEXT.set(context);
    }
    
    public static String getTenantId() {
        TenantContext context = getContext();
        return context != null ? context.getTenantId() : null;
    }
    
    public static void setCorrelationId(String correlationId) {
        TenantContext context = getContext();
        if (context == null) {
            context = new TenantContext();
        }
        context.setCorrelationId(correlationId);
        CONTEXT.set(context);
    }
    
    public static String getCorrelationId() {
        TenantContext context = getContext();
        return context != null ? context.getCorrelationId() : null;
    }
    
    public static void setUserId(String userId) {
        TenantContext context = getContext();
        if (context == null) {
            context = new TenantContext();
        }
        context.setUserId(userId);
        CONTEXT.set(context);
    }
    
    public static String getUserId() {
        TenantContext context = getContext();
        return context != null ? context.getUserId() : null;
    }
    
    public static TenantContext getContext() {
        return CONTEXT.get();
    }
    
    public static boolean hasContext() {
        return CONTEXT.get() != null;
    }
    
    public static boolean hasTenant() {
        TenantContext context = getContext();
        return context != null && context.getTenantId() != null;
    }
    
    public static void clear() {
        CONTEXT.remove();
    }
    
    public static void clearContext() {
        CONTEXT.remove();
    }

    public static void init() {
        TenantContext context = new TenantContext();
        context.setCorrelationId(UUID.randomUUID().toString());
        CONTEXT.set(context);
    }

    public static void setContext(TenantContext context) {
        CONTEXT.set(context);
    }
}
