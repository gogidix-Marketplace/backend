package com.gogidix.courier.gpstrackingservice.shared.context;

/**
 * Holder for the current request context.
 * Uses ThreadLocal to store context per request thread.
 */
public class RequestContextHolder {

    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    /**
     * Set the request context for the current thread.
     *
     * @param context the context to set
     */
    public static void setContext(RequestContext context) {
        CONTEXT.set(context);
    }

    /**
     * Get the request context for the current thread.
     *
     * @return the context, or anonymous context if not set
     */
    public static RequestContext getContext() {
        RequestContext context = CONTEXT.get();
        if (context == null) {
            context = RequestContext.anonymous();
            CONTEXT.set(context);
        }
        return context;
    }

    /**
     * Clear the request context for the current thread.
     */
    public static void clearContext() {
        CONTEXT.remove();
    }
}
