package com.gogidix.ecommerce.paymentgateway.shared.exception;

public class PaymentGatewayNotFoundException extends RuntimeException {
    private final String id;
    public PaymentGatewayNotFoundException(String id) { super("PaymentGateway not found: " + id); this.id = id; }
    public String getId() { return id; }
}
