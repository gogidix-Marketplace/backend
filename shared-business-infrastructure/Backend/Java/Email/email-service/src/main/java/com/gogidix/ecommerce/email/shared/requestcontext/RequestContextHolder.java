package com.gogidix.ecommerce.email.shared.requestcontext;

public final class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(RequestContext context) {
        CONTEXT.set(context);
    }

    public static RequestContext getContext() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            context = RequestContext.builder().build();
            CONTEXT.set(context);
        }
        return context;
    }

    public static void clear() {
        CONTEXT.remove();
    }

    private RequestContextHolder() {
    }
    public static String getCustomerId() { return get().customerId(); }
}