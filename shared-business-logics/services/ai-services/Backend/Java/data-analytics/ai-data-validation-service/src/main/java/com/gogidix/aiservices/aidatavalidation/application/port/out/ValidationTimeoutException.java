package com.gogidix.aiservices.aidatavalidation.application.port.out;

public class ValidationTimeoutException extends RuntimeException {
    public ValidationTimeoutException(String message) {
        super(message);
    }
}
