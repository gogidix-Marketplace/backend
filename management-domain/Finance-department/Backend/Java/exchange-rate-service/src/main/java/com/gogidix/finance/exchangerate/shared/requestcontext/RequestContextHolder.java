package com.gogidix.finance.exchangerate.shared.requestcontext;

public class RequestContextHolder {
    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(RequestContext context) {
        CONTEXT.set(context);
    }

    public static void set(RequestContext context) {
        CONTEXT.set(context);
    }

    public static RequestContext getContext() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
