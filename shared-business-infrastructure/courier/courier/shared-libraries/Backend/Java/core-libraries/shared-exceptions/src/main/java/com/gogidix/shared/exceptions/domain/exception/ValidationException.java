package com.gogidix.shared.exceptions.domain.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation exceptions for input validation failures
 */
public class ValidationException extends BusinessException {

    private final List<String> validationErrors;

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR", 400);
        this.validationErrors = new ArrayList<>();
    }

    public ValidationException(String message, List<String> validationErrors) {
        super(message, "VALIDATION_ERROR", 400);
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    public void addValidationError(String error) {
        this.validationErrors.add(error);
    }

    public List<String> getValidationErrors() {
        return new ArrayList<>(validationErrors);
    }

    public boolean hasValidationErrors() {
        return !validationErrors.isEmpty();
    }
}
