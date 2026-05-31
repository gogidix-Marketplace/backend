package com.gogidix.ecommerce.sms.shared.constants;

public final class SmsConstants {
    private SmsConstants() {}
    public static final String SERVICE_NAME = "sms-service";
    public static final String API_BASE_PATH = "/api/v1/smss";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_sms";
}
