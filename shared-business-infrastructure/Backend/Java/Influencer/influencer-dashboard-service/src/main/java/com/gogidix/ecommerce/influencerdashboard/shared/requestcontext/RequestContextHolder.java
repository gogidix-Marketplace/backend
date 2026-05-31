package com.gogidix.ecommerce.influencerdashboard.shared.requestcontext;
public final class RequestContextHolder {
    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();
    private RequestContextHolder() {}
    public static void set(RequestContext context) { CONTEXT.set(context); }
    public static RequestContext get() {
        RequestContext context = CONTEXT.get();
        if (context == null) throw new IllegalStateException("RequestContext not set.");
        return context;
    }
    public static String getTenantId() { return get().tenantId(); }
    public static String getCustomerId() { return get().customerId(); }
    public static String getUserId() { return get().userId(); }
    public static String getCorrelationId() { return get().correlationId(); }
    public static void clear() { CONTEXT.remove(); }
    public static boolean isSet() { return CONTEXT.get() != null; }
}