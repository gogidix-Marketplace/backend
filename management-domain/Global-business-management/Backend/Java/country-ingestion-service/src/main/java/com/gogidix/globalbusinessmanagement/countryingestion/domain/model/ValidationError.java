package com.gogidix.globalbusinessmanagement.countryingestion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Domain model representing a validation error found during data ingestion.
 * Tracks details about validation failures for reporting and correction.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "validation_errors")
public class ValidationError {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Error ID is required")
    private String errorId;

    @Indexed
    @NotBlank(message = "Batch ID is required")
    private String batchId;

    @Indexed
    private String recordId;

    @Indexed
    private String countryCode;

    @Indexed
    @NotNull(message = "Error level is required")
    private ErrorLevel errorLevel;

    @Indexed
    @NotBlank(message = "Error code is required")
    private String errorCode;

    @NotBlank(message = "Error message is required")
    private String errorMessage;

    private String detailedMessage;

    @Indexed
    @NotBlank(message = "Field name is required")
    private String fieldName;

    private Object fieldValue;

    private String fieldType;

    @Indexed
    @NotNull(message = "Error type is required")
    private ErrorType errorType;

    private Integer rowNumber;

    private Integer columnNumber;

    @Indexed
    @NotNull(message = "Error timestamp is required")
    private LocalDateTime errorTimestamp;

    @Indexed
    @NotNull(message = "Status is required")
    @Builder.Default
    private ErrorStatus status = ErrorStatus.OPEN;

    private LocalDateTime resolvedDate;

    private String resolvedBy;

    private String resolutionNotes;

    @Indexed
    private String correctedValue;

    @Indexed
    private Boolean autoCorrected;

    private String correctionRuleApplied;

    @Indexed
    @Builder.Default
    private Boolean suppress = false;

    private String suppressedBy;

    private LocalDateTime suppressedDate;

    private String suppressionReason;

    @Indexed
    private String assignedTo;

    @Indexed
    private Integer priority;

    @Builder.Default
    private Integer occurrences = 1;

    @Indexed
    private String organizationId;

    private String tenantId;

    @Builder.Default
    private Map<String, Object> context = new java.util.HashMap<>();

    private String suggestion;

    private String validationRule;

    /**
     * Error level enumeration.
     */
    public enum ErrorLevel {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        INFO
    }

    /**
     * Error type enumeration.
     */
    public enum ErrorType {
        MISSING_REQUIRED_FIELD,
        INVALID_FORMAT,
        VALUE_OUT_OF_RANGE,
        INVALID_REFERENCE,
        DUPLICATE_VALUE,
        PATTERN_MISMATCH,
        TYPE_MISMATCH,
        BUSINESS_RULE_VIOLATION,
        DATA_QUALITY,
        SCHEMA_VIOLATION,
        CONSTRAINT_VIOLATION,
        INTEGRITY_ERROR,
        TRANSFORMATION_ERROR,
        UNKNOWN
    }

    /**
     * Error status enumeration.
     */
    public enum ErrorStatus {
        OPEN,
        IN_PROGRESS,
        RESOLVED,
        IGNORED,
        SUPPRESSED,
        AUTO_CORRECTED
    }

    /**
     * Creates a critical validation error.
     */
    public static ValidationError createCritical(String batchId, String fieldName,
                                                 String errorCode, String message) {
        return ValidationError.builder()
                .errorId(UUID.randomUUID().toString())
                .batchId(batchId)
                .fieldName(fieldName)
                .errorCode(errorCode)
                .errorMessage(message)
                .errorLevel(ErrorLevel.CRITICAL)
                .errorType(ErrorType.MISSING_REQUIRED_FIELD)
                .errorTimestamp(LocalDateTime.now())
                .status(ErrorStatus.OPEN)
                .priority(1)
                .build();
    }

    /**
     * Creates a high priority validation error.
     */
    public static ValidationError createHigh(String batchId, String fieldName,
                                             String errorCode, String message) {
        return ValidationError.builder()
                .errorId(UUID.randomUUID().toString())
                .batchId(batchId)
                .fieldName(fieldName)
                .errorCode(errorCode)
                .errorMessage(message)
                .errorLevel(ErrorLevel.HIGH)
                .errorType(ErrorType.INVALID_FORMAT)
                .errorTimestamp(LocalDateTime.now())
                .status(ErrorStatus.OPEN)
                .priority(2)
                .build();
    }

    /**
     * Creates a medium priority validation error.
     */
    public static ValidationError createMedium(String batchId, String fieldName,
                                               String errorCode, String message) {
        return ValidationError.builder()
                .errorId(UUID.randomUUID().toString())
                .batchId(batchId)
                .fieldName(fieldName)
                .errorCode(errorCode)
                .errorMessage(message)
                .errorLevel(ErrorLevel.MEDIUM)
                .errorType(ErrorType.DATA_QUALITY)
                .errorTimestamp(LocalDateTime.now())
                .status(ErrorStatus.OPEN)
                .priority(3)
                .build();
    }

    /**
     * Resolves the error.
     */
    public void resolve(String resolvedBy, String correctedValue, String notes) {
        this.status = ErrorStatus.RESOLVED;
        this.resolvedDate = LocalDateTime.now();
        this.resolvedBy = resolvedBy;
        this.correctedValue = correctedValue;
        this.resolutionNotes = notes;
    }

    /**
     * Marks the error as auto-corrected.
     */
    public void markAutoCorrected(String correctedValue, String rule) {
        this.status = ErrorStatus.AUTO_CORRECTED;
        this.resolvedDate = LocalDateTime.now();
        this.correctedValue = correctedValue;
        this.autoCorrected = true;
        this.correctionRuleApplied = rule;
    }

    /**
     * Suppresses the error.
     */
    public void suppress(String suppressedBy, String reason) {
        this.status = ErrorStatus.SUPPRESSED;
        this.suppressedDate = LocalDateTime.now();
        this.suppressedBy = suppressedBy;
        this.suppressionReason = reason;
        this.suppress = true;
    }

    /**
     * Increments the occurrence count.
     */
    public void incrementOccurrences() {
        this.occurrences++;
    }

    /**
     * Checks if the error is in a terminal state.
     */
    public boolean isTerminalState() {
        return status == ErrorStatus.RESOLVED ||
                status == ErrorStatus.SUPPRESSED ||
                status == ErrorStatus.IGNORED ||
                status == ErrorStatus.AUTO_CORRECTED;
    }

    /**
     * Gets the display name for the error type.
     */
    public String getErrorTypeDisplayName() {
        return errorType != null ? errorType.name().replace("_", " ") : "UNKNOWN";
    }

    /**
     * Gets the display name for the error level.
     */
    public String getErrorLevelDisplayName() {
        return errorLevel != null ? errorLevel.name() : "INFO";
    }

    /**
     * Adds context information.
     */
    public void addContext(String key, Object value) {
        this.context.put(key, value);
    }

    /**
     * Gets context value by key.
     */
    public Object getContextValue(String key) {
        return context.get(key);
    }
}
