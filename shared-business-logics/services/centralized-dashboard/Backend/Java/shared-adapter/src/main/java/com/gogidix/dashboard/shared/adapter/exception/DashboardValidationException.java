package com.gogidix.dashboard.shared.adapter.exception;

/**
 * Exception thrown when validation fails for business rules.
 * 
 * <p>This exception bridges between dashboard services and the Foundation
 * shared-exceptions library, providing consistent exception handling.
 * 
 * @author Gogidix Dashboard Team
 * @since 1.0.0
 */
public class DashboardValidationException extends RuntimeException {

    private final String field;
    private final String code;

    public DashboardValidationException(String message) {
        super(message);
        this.field = null;
        this.code = "VALIDATION_ERROR";
    }

    public DashboardValidationException(String message, String field) {
        super(message);
        this.field = field;
        this.code = "VALIDATION_ERROR";
    }

    public DashboardValidationException(String message, String field, String code) {
        super(message);
        this.field = field;
        this.code = code;
    }

    public DashboardValidationException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
        this.code = "VALIDATION_ERROR";
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }
}
