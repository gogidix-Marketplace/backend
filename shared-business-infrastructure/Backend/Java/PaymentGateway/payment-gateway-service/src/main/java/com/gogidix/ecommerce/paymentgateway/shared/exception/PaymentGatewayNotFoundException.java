package com.gogidix.ecommerce.paymentgateway.shared.exception;

public class PaymentGatewayNotFoundException extends RuntimeException {

    public PaymentGatewayNotFoundException(String message) {
        super(message);
    }
}