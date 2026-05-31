package com.gogidix.sales.notification.shared.exception;

import lombok.Getter;

/**
 * Validation Exception
 * Thrown when validation fails
 */
@Getter
public class ValidationException extends RuntimeException {

    private final String field;
    private final String message;

    public ValidationException(String field, String message) {
        super(String.format("Validation failed for field '%s': %s", field, message));
        this.field = field;
        this.message = message;
    }

    public ValidationException(String message) {
        super(message);
        this.field = null;
        this.message = message;
    }
}
