package com.gogidix.ecommerce.customer.shared.constants;

public final class CustomerConstants {
    private CustomerConstants() {}
    public static final String SERVICE_NAME = "customer-service";
    public static final String API_BASE_PATH = "/api/v1/customers";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_customer";
}
