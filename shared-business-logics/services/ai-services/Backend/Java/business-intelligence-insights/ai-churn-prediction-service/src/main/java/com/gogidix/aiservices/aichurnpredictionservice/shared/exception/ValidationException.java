package com.gogidix.aiservices.aichurnpredictionservice.shared.exception;

import java.util.List;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends BaseDomainException {

    private final List<String> validationErrors;

    public ValidationException(String message) {
        super(message);
        this.validationErrors = List.of(message);
    }

    public ValidationException(String message, String errorCode) {
        super(message, errorCode);
        this.validationErrors = List.of(message);
    }

    public ValidationException(List<String> validationErrors) {
        super(String.join(", ", validationErrors));
        this.validationErrors = validationErrors;
    }

    public ValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.validationErrors = List.of(message);
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    @Override
    protected String deriveErrorCode() {
        return "VALIDATION_FAILED";
    }
}
