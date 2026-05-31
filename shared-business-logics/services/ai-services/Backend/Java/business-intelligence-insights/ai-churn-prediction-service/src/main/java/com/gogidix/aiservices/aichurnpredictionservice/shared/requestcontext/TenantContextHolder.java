package com.gogidix.aiservices.aichurnpredictionservice.shared.requestcontext;

/**
 * Thread-local holder for tenant context in ai-churn-predictionation-service.
 */
public class TenantContextHolder {

    private static final ThreadLocal<TenantContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(TenantContext context) {
        CONTEXT.set(context);
    }

    public static TenantContext getContext() {
        return CONTEXT.get();
    }

    public static void clearContext() {
        CONTEXT.remove();
    }

    public static String getTenantId() {
        TenantContext context = getContext();
        return context != null ? context.getTenantId() : null;
    }
}
