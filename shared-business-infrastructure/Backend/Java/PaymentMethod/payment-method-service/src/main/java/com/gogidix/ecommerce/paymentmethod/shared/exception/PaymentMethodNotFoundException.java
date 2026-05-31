package com.gogidix.ecommerce.paymentmethod.shared.exception;

public class PaymentMethodNotFoundException extends RuntimeException {

    public PaymentMethodNotFoundException(String message) {
        super(message);
    }
}