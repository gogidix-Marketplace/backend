package com.gogidix.digitalmarketing.shared.exception;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends DomainException {

    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
    }

    public ValidationException(String field, String reason) {
        super("VALIDATION_ERROR", String.format("Validation failed for field '%s': %s", field, reason));
    }

    public ValidationException(String message, Throwable cause) {
        super("VALIDATION_ERROR", message, cause);
    }
}
