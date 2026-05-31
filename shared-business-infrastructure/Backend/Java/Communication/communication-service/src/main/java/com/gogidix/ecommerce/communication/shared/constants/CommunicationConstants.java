package com.gogidix.ecommerce.communication.shared.constants;

public final class CommunicationConstants {
    private CommunicationConstants() {}
    public static final String SERVICE_NAME = "communication-service";
    public static final String API_BASE_PATH = "/api/v1/communications";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_communication";
}
