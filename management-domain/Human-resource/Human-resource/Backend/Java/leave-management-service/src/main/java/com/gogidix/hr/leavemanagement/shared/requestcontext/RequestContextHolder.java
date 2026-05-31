package com.gogidix.hr.leavemanagement.shared.requestcontext;

import java.util.Optional;

public final class RequestContextHolder {
    private static final ThreadLocal<RequestContext> CONTEXT = new ThreadLocal<>();

    private RequestContextHolder() {}

    public static void set(RequestContext context) { CONTEXT.set(context); }
    public static Optional<RequestContext> get() { return Optional.ofNullable(CONTEXT.get()); }
    public static RequestContext require() { return get().orElseThrow(() -> new IllegalStateException("RequestContext not set")); }
    public static void clear() { CONTEXT.remove(); }
    public static String getTenantId() { return require().tenantId(); }
    public static String getUserId() { return get().map(RequestContext::userId).orElse(null); }
    public static String getCorrelationId() { return require().correlationId(); }
    public static String getCountryCode() { return get().map(RequestContext::countryCode).orElse(null); }
}
