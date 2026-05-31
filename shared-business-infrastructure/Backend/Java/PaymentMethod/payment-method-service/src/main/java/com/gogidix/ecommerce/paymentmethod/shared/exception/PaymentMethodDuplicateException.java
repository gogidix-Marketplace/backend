package com.gogidix.ecommerce.paymentmethod.shared.exception;

public class PaymentMethodDuplicateException extends RuntimeException {

    public PaymentMethodDuplicateException(String message) {
        super(message);
    }
}