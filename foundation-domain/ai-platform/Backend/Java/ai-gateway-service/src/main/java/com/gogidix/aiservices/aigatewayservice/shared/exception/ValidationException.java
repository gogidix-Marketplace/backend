package com.gogidix.aiservices.aigatewayservice.shared.exception;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends BaseDomainException {

    private final String field;

    public ValidationException(String message) {
        super(message);
        this.field = null;
    }

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
    }

    public String getField() {
        return field;
    }

    @Override
    protected String deriveErrorCode() {
        return "VALIDATION_ERROR";
    }
}
