package com.gogidix.ecommerce.sms.shared.exception;

public class SmsNotFoundException extends RuntimeException {

    public SmsNotFoundException(String message) {
        super(message);
    }
}