package com.gogidix.ecommerce.sms.shared.exception;

public class SmsDuplicateException extends RuntimeException {

    public SmsDuplicateException(String message) {
        super(message);
    }
}