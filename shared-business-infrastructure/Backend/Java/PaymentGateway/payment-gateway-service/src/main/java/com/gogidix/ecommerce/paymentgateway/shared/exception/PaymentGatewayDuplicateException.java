package com.gogidix.ecommerce.paymentgateway.shared.exception;

public class PaymentGatewayDuplicateException extends RuntimeException {

    public PaymentGatewayDuplicateException(String message) {
        super(message);
    }
}