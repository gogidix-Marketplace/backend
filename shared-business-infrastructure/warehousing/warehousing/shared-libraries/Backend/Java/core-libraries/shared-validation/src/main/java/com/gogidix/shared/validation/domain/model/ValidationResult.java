package com.gogidix.shared.validation.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Domain entity representing the result of a validation execution.
 */
@Data
@With
@Builder
public class ValidationResult {
    
    private final UUID id;
    private final ValidationRule rule;
    private final Object validatedValue;
    private final ValidationStatus status;
    private final String message;
    private final String errorCode;
    private final Exception error;
    private final LocalDateTime validatedAt;
    private final long executionTimeMs;
    private final Map<String, Object> metadata;
    private final ValidationContext context;
    
    /**
     * Status of validation execution.
     */
    public enum ValidationStatus {
        PASSED("Passed", "Validation passed successfully"),
        FAILED("Failed", "Validation failed"),
        ERROR("Error", "Validation encountered an error"),
        SKIPPED("Skipped", "Validation was skipped"),
        WARNING("Warning", "Validation passed with warnings");
        
        private final String displayName;
        private final String description;
        
        ValidationStatus(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        
        public boolean isPassed() { return this == PASSED || this == WARNING; }
        public boolean isFailed() { return this == FAILED || this == ERROR; }
        public boolean isBlocking() { return this == FAILED || this == ERROR; }
    }
    
    /**
     * Gets the severity of this validation result.
     */
    public ValidationSeverity getSeverity() {
        if (rule == null) return ValidationSeverity.INFO;
        
        return switch (status) {
            case PASSED -> ValidationSeverity.INFO;
            case WARNING -> ValidationSeverity.WARNING;
            case FAILED -> rule.getSeverity();
            case ERROR -> ValidationSeverity.CRITICAL;
            case SKIPPED -> ValidationSeverity.INFO;
        };
    }
    
    /**
     * Checks if this result represents a successful validation.
     */
    public boolean isSuccess() {
        return status == ValidationStatus.PASSED || status == ValidationStatus.WARNING;
    }
    
    /**
     * Checks if this result represents a failed validation.
     */
    public boolean isFailure() {
        return status == ValidationStatus.FAILED || status == ValidationStatus.ERROR;
    }
    
    /**
     * Checks if this result should block further processing.
     */
    public boolean isBlocking() {
        return status.isBlocking() && getSeverity().isBlocking();
    }
    
    /**
     * Gets a user-friendly description of the validation result.
     */
    public String getDescription() {
        if (rule == null) return message != null ? message : "Unknown validation result";
        
        String ruleDescription = rule.getName() + " validation";
        return switch (status) {
            case PASSED -> ruleDescription + " passed";
            case WARNING -> ruleDescription + " passed with warning: " + message;
            case FAILED -> ruleDescription + " failed: " + message;
            case ERROR -> ruleDescription + " encountered error: " + message;
            case SKIPPED -> ruleDescription + " was skipped: " + message;
        };
    }
    
    /**
     * Creates a successful validation result.
     */
    public static ValidationResult passed(ValidationRule rule, Object value) {
        return ValidationResult.builder()
                .id(UUID.randomUUID())
                .rule(rule)
                .validatedValue(value)
                .status(ValidationStatus.PASSED)
                .message("Validation passed")
                .validatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a failed validation result.
     */
    public static ValidationResult failed(ValidationRule rule, Object value, String message) {
        return ValidationResult.builder()
                .id(UUID.randomUUID())
                .rule(rule)
                .validatedValue(value)
                .status(ValidationStatus.FAILED)
                .message(message)
                .errorCode(rule != null ? rule.getErrorCode() : "VALIDATION_FAILED")
                .validatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates an error validation result.
     */
    public static ValidationResult error(ValidationRule rule, Object value, String message, Exception error) {
        return ValidationResult.builder()
                .id(UUID.randomUUID())
                .rule(rule)
                .validatedValue(value)
                .status(ValidationStatus.ERROR)
                .message(message)
                .errorCode("VALIDATION_ERROR")
                .error(error)
                .validatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a skipped validation result.
     */
    public static ValidationResult skipped(ValidationRule rule, Object value, String reason) {
        return ValidationResult.builder()
                .id(UUID.randomUUID())
                .rule(rule)
                .validatedValue(value)
                .status(ValidationStatus.SKIPPED)
                .message(reason)
                .validatedAt(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a warning validation result.
     */
    public static ValidationResult warning(ValidationRule rule, Object value, String message) {
        return ValidationResult.builder()
                .id(UUID.randomUUID())
                .rule(rule)
                .validatedValue(value)
                .status(ValidationStatus.WARNING)
                .message(message)
                .validatedAt(LocalDateTime.now())
                .build();
    }
}