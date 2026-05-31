package com.gogidix.ecommerce.paymentgateway.shared.exception;

public class DuplicatePaymentGatewayException extends RuntimeException {
    private final String name;
    public DuplicatePaymentGatewayException(String name) { super("Duplicate PaymentGateway: " + name); this.name = name; }
    public String getName() { return name; }
}
