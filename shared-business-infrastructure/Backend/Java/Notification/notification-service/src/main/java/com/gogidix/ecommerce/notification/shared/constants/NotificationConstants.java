package com.gogidix.ecommerce.notification.shared.constants;

public final class NotificationConstants {
    private NotificationConstants() {}
    public static final String SERVICE_NAME = "notification-service";
    public static final String API_BASE_PATH = "/api/v1/notifications";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_notification";
}
