package com.gogidix.ecommerce.category.shared.requestcontext;

public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    public static void set(RequestContext context) {
        CONTEXT.set(context);
    }

    public static RequestContext get() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("RequestContext not initialized. Use set() before get().");
        }
        return context;
    }

    public static boolean isInitialized() {
        return CONTEXT.get() != null;
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static String getTenantId() {
        return get().tenantId();
    }

    public static String getUserId() {
        return get().userId();
    }

    public static String getCorrelationId() {
        return get().correlationId();
    }
    public static String getCustomerId() { return get().customerId(); }
}
