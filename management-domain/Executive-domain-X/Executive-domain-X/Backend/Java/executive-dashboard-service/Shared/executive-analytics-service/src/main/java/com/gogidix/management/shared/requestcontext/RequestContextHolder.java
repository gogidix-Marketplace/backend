package com.gogidix.management.shared.requestcontext;

public class RequestContextHolder {
    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    public static void set(RequestContext context) {
        CONTEXT.set(context);
    }

    public static RequestContext get() {
        return CONTEXT.get();
    }

    public static String getTenantId() {
        RequestContext ctx = CONTEXT.get();
        return ctx != null ? ctx.getTenantId() : null;
    }

    public static String getCorrelationId() {
        RequestContext ctx = CONTEXT.get();
        return ctx != null ? ctx.getCorrelationId() : null;
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
