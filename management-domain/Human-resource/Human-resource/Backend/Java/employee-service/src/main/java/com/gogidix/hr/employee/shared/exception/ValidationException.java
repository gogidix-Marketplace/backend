package com.gogidix.hr.employee.shared.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) { super(message); }
    public ValidationException(String field, String reason) {
        super("Validation failed for '" + field + "': " + reason);
    }
}
