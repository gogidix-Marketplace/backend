package com.gogidix.ecommerce.paymentgateway.shared.constants;

public final class PaymentGatewayConstants {
    private PaymentGatewayConstants() {}
    public static final String SERVICE_NAME = "payment-gateway-service";
    public static final String API_BASE_PATH = "/api/v1/payment-gateways";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_paymentgateway";
}
