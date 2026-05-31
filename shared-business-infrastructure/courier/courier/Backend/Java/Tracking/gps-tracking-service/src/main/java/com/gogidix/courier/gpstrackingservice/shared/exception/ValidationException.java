package com.gogidix.courier.gpstrackingservice.shared.exception;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends BaseDomainException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    protected String deriveErrorCode() {
        return "VALIDATION_ERROR";
    }
}
