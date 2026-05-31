package com.gogidix.aiservices.intelligenceanalysisservice.shared.requestcontext;

/**
 * Thread-local holder for tenant context in ai-customer-segmentation-service.
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
