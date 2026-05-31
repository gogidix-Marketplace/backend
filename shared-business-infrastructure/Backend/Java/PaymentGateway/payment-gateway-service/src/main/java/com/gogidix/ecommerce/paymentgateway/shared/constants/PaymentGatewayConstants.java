package com.gogidix.ecommerce.paymentgateway.shared.constants;

public final class PaymentGatewayConstants {

    public static final String SERVICE_NAME = "PaymentGateway";
    public static final String API_BASE_PATH = "/api/v1/payment-gateways";
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final String MAX_PAGE_SIZE = "100";
    public static final String TENANT_HEADER = "X-Tenant-ID";

    private PaymentGatewayConstants() {
    }
}