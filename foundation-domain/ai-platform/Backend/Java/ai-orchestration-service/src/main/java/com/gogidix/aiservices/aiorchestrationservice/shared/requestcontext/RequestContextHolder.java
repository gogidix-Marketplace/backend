package com.gogidix.aiservices.aiorchestrationservice.shared.requestcontext;

public class RequestContextHolder {
    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(RequestContext context) { CONTEXT.set(context); }
    public static RequestContext getContext() {
        RequestContext context = CONTEXT.get();
        return context != null ? context : RequestContext.anonymous();
    }
    public static void clearContext() { CONTEXT.remove(); }
}
