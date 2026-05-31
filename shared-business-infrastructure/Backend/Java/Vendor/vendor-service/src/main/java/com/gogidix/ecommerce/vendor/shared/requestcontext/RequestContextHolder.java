package com.gogidix.ecommerce.vendor.shared.requestcontext;

public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    public static void set(RequestContext context) { CONTEXT.set(context); }
    public static RequestContext get() {
        RequestContext ctx = CONTEXT.get();
        if (ctx == null) throw new IllegalStateException("RequestContext not initialized");
        return ctx;
    }
    public static boolean isInitialized() { return CONTEXT.get() != null; }
    public static void clear() { CONTEXT.remove(); }
    public static String getTenantId() { return get().tenantId(); }
    public static String getUserId() { return get().userId(); }
    public static String getCorrelationId() { return get().correlationId(); }
    public static String getCustomerId() { return get().customerId(); }
}