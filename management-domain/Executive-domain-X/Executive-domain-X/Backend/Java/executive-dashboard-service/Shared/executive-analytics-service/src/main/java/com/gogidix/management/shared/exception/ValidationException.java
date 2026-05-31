package com.gogidix.management.shared.exception;

public class ValidationException extends DomainException {
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }

    public ValidationException(String field, String message) {
        super(field + ": " + message, "VALIDATION_ERROR");
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
