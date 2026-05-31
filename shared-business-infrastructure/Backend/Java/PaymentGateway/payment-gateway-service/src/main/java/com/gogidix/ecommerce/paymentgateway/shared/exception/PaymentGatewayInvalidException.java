package com.gogidix.ecommerce.paymentgateway.shared.exception;

public class PaymentGatewayInvalidException extends RuntimeException {

    public PaymentGatewayInvalidException(String message) {
        super(message);
    }
}