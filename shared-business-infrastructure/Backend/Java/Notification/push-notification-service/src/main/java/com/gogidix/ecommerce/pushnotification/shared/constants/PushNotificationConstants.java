package com.gogidix.ecommerce.pushnotification.shared.constants;

public final class PushNotificationConstants {
    private PushNotificationConstants() {}
    public static final String SERVICE_NAME = "push-notification-service";
    public static final String API_BASE_PATH = "/api/v1/push-notifications";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_pushnotification";
}
