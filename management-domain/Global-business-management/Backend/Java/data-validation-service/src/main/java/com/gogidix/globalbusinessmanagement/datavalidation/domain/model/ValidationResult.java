package com.gogidix.globalbusinessmanagement.datavalidation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing the result of a validation operation.
 * Contains details about passed/failed validations and error messages.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "validation_results")
public class ValidationResult {

    @Id
    private String id;

    @Indexed
    private String validationId;

    @Indexed
    private String entityType;

    @Indexed
    private String entityId;

    private String entityData;

    @Indexed
    private String ruleCode;

    private String ruleName;

    @Indexed
    @Builder.Default
    private boolean passed = false;

    @Indexed
    private ValidationRule.SeverityLevel severity;

    @Indexed
    @Builder.Default
    private ValidationStatus status = ValidationStatus.PENDING;

    private String fieldName;

    private String actualValue;

    private String expectedValue;

    private String errorMessage;

    private Map<String, String> localizedErrorMessages;

    private String errorCode;

    private List<ValidationIssue> issues;

    private Map<String, Object> context;

    @Indexed
    private String validatedBy;

    private LocalDateTime validatedAt;

    @Indexed
    private LocalDateTime createdAt;

    @Builder.Default
    private long executionTimeMs = 0;

    private String batchId;

    @Indexed
    private String tenantId;

    private Map<String, Object> metadata;

    /**
     * Status of the validation
     */
    public enum ValidationStatus {
        PENDING,
        IN_PROGRESS,
        PASSED,
        FAILED,
        SKIPPED,
        ERROR
    }

    /**
     * Represents a specific validation issue found during validation
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationIssue {
        private String code;
        private String field;
        private String message;
        private ValidationRule.SeverityLevel severity;
        private Object actualValue;
        private Object expectedValue;
        private Map<String, Object> details;
    }

    /**
     * Checks if the validation was successful
     */
    public boolean isSuccessful() {
        return passed && status == ValidationStatus.PASSED;
    }

    /**
     * Checks if the validation has critical issues
     */
    public boolean hasCriticalIssues() {
        if (issues == null || issues.isEmpty()) {
            return false;
        }
        return issues.stream()
                .anyMatch(issue -> issue.getSeverity() == ValidationRule.SeverityLevel.CRITICAL);
    }

    /**
     * Gets the count of issues by severity
     */
    public long getIssueCount(ValidationRule.SeverityLevel severity) {
        if (issues == null || issues.isEmpty()) {
            return 0;
        }
        return issues.stream()
                .filter(issue -> issue.getSeverity() == severity)
                .count();
    }

    /**
     * Adds an issue to the validation result
     */
    public void addIssue(ValidationIssue issue) {
        if (this.issues == null) {
            this.issues = new java.util.ArrayList<>();
        }
        this.issues.add(issue);
    }

    /**
     * Creates a successful result
     */
    public static ValidationResult success(String validationId, String entityType, String entityId, String ruleCode) {
        return ValidationResult.builder()
                .validationId(validationId)
                .entityType(entityType)
                .entityId(entityId)
                .ruleCode(ruleCode)
                .passed(true)
                .status(ValidationStatus.PASSED)
                .validatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a failed result
     */
    public static ValidationResult failure(String validationId, String entityType, String entityId,
                                          String ruleCode, String errorMessage, ValidationRule.SeverityLevel severity) {
        return ValidationResult.builder()
                .validationId(validationId)
                .entityType(entityType)
                .entityId(entityId)
                .ruleCode(ruleCode)
                .passed(false)
                .status(ValidationStatus.FAILED)
                .errorMessage(errorMessage)
                .severity(severity)
                .validatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
