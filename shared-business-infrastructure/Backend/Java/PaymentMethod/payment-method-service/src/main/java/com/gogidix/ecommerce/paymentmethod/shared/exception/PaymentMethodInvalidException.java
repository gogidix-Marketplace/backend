package com.gogidix.ecommerce.paymentmethod.shared.exception;

public class PaymentMethodInvalidException extends RuntimeException {

    public PaymentMethodInvalidException(String message) {
        super(message);
    }
}