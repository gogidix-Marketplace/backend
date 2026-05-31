package com.gogidix.aiservices.aimonitoringservice.shared.exception;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends BaseDomainException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String message) {
        super(message);
    }

    @Override
    protected String deriveErrorCode() {
        return "VALIDATION_ERROR";
    }
}
