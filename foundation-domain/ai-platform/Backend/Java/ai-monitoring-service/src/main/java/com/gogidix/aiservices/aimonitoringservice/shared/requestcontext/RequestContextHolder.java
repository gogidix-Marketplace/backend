package com.gogidix.aiservices.aimonitoringservice.shared.requestcontext;

/**
 * Holder for the current request context.
 */
public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(RequestContext context) {
        CONTEXT.set(context);
    }

    public static RequestContext getContext() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            return RequestContext.anonymous();
        }
        return context;
    }

    public static void clearContext() {
        CONTEXT.remove();
    }
}
