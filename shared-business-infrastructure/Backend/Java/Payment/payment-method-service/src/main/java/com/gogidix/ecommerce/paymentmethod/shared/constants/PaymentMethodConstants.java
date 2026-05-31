package com.gogidix.ecommerce.paymentmethod.shared.constants;

public final class PaymentMethodConstants {
    private PaymentMethodConstants() {}
    public static final String SERVICE_NAME = "payment-method-service";
    public static final String API_BASE_PATH = "/api/v1/payment-methods";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_paymentmethod";
}
