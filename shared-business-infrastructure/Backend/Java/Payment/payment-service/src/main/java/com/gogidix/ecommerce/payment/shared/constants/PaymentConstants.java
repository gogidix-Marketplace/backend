package com.gogidix.ecommerce.payment.shared.constants;

public final class PaymentConstants {
    private PaymentConstants() {}
    public static final String SERVICE_NAME = "payment-service";
    public static final String API_BASE_PATH = "/api/v1/payments";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_payment";
}
