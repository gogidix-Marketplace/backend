package com.gogidix.cargo.eventdriven.shared.context;

public class EventContext {
    private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> SOURCE = new ThreadLocal<>();

    public static void setCorrelationId(String correlationId) { CORRELATION_ID.set(correlationId); }
    public static String getCorrelationId() { return CORRELATION_ID.get(); }
    public static void clearCorrelationId() { CORRELATION_ID.remove(); }
    public static void setTenantId(String tenantId) { TENANT_ID.set(tenantId); }
    public static String getTenantId() { return TENANT_ID.get(); }
    public static void clearTenantId() { TENANT_ID.remove(); }
    public static void setSource(String source) { SOURCE.set(source); }
    public static String getSource() { return SOURCE.get(); }
    public static void clearSource() { SOURCE.remove(); }
    public static void clearAll() { CORRELATION_ID.remove(); TENANT_ID.remove(); SOURCE.remove(); }
}
