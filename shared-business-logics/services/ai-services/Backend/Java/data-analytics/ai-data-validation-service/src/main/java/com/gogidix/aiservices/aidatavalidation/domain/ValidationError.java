package com.gogidix.aiservices.aidatavalidation.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.AllArgsConstructor;

/**
 * Value object representing a validation error.
 */
@Getter
@AllArgsConstructor
public class ValidationError {

    @NonNull
    private final String field;

    @NonNull
    private final String code;

    @NonNull
    private final String message;

    private final ErrorSeverity severity;

    /**
     * Constructor with default severity MEDIUM.
     */
    public ValidationError(@NonNull String field, @NonNull String code, @NonNull String message) {
        this.field = field;
        this.code = code;
        this.message = message;
        this.severity = ErrorSeverity.MEDIUM;
    }

    public enum ErrorSeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
