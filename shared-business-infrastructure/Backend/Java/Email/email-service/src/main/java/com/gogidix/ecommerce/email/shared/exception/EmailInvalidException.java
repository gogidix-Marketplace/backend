package com.gogidix.ecommerce.email.shared.exception;

public class EmailInvalidException extends RuntimeException {

    public EmailInvalidException(String message) {
        super(message);
    }
}