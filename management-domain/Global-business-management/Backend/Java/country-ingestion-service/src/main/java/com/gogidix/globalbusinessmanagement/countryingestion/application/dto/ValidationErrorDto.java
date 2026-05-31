package com.gogidix.globalbusinessmanagement.countryingestion.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for ValidationError entity.
 * Used for transferring validation error information between layers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Validation Error DTO for tracking data validation issues")
public class ValidationErrorDto {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "Error identifier", example = "ERR-2024-001")
    @NotBlank(message = "Error ID is required")
    private String errorId;

    @Schema(description = "Associated batch ID")
    @NotBlank(message = "Batch ID is required")
    private String batchId;

    @Schema(description = "Associated record ID")
    private String recordId;

    @Schema(description = "Country code for the error")
    private String countryCode;

    @Schema(description = "Error severity level")
    @NotNull(message = "Error level is required")
    private ErrorLevelDto errorLevel;

    @Schema(description = "Error code", example = "MISSING_REQUIRED_FIELD")
    @NotBlank(message = "Error code is required")
    private String errorCode;

    @Schema(description = "Error message")
    @NotBlank(message = "Error message is required")
    private String errorMessage;

    @Schema(description = "Detailed error message")
    private String detailedMessage;

    @Schema(description = "Field name with error")
    @NotBlank(message = "Field name is required")
    private String fieldName;

    @Schema(description = "Invalid field value")
    private Object fieldValue;

    @Schema(description = "Field type")
    private String fieldType;

    @Schema(description = "Error type category")
    @NotNull(message = "Error type is required")
    private ErrorTypeDto errorType;

    @Schema(description = "Row number in source file")
    private Integer rowNumber;

    @Schema(description = "Column number in source file")
    private Integer columnNumber;

    @Schema(description = "Error timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime errorTimestamp;

    @Schema(description = "Error status")
    @NotNull(message = "Status is required")
    private ErrorStatusDto status;

    @Schema(description = "Resolution timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime resolvedDate;

    @Schema(description = "User who resolved the error")
    private String resolvedBy;

    @Schema(description = "Resolution notes")
    private String resolutionNotes;

    @Schema(description = "Corrected value")
    private String correctedValue;

    @Schema(description = "Was auto-corrected")
    private Boolean autoCorrected;

    @Schema(description = "Correction rule applied")
    private String correctionRuleApplied;

    @Schema(description = "Is error suppressed")
    private Boolean suppress;

    @Schema(description = "User who suppressed the error")
    private String suppressedBy;

    @Schema(description = "Suppression timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime suppressedDate;

    @Schema(description = "Suppression reason")
    private String suppressionReason;

    @Schema(description = "Assigned to user")
    private String assignedTo;

    @Schema(description = "Priority level (1=highest)")
    private Integer priority;

    @Schema(description = "Number of occurrences")
    private Integer occurrences;

    @Schema(description = "Organization ID")
    private String organizationId;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Additional context information")
    private Map<String, Object> context;

    @Schema(description = "Suggested correction")
    private String suggestion;

    @Schema(description = "Validation rule that failed")
    private String validationRule;

    /**
     * Error level DTO enum.
     */
    @Schema(description = "Error level enumeration")
    public enum ErrorLevelDto {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        INFO
    }

    /**
     * Error type DTO enum.
     */
    @Schema(description = "Error type enumeration")
    public enum ErrorTypeDto {
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
     * Error status DTO enum.
     */
    @Schema(description = "Error status enumeration")
    public enum ErrorStatusDto {
        OPEN,
        IN_PROGRESS,
        RESOLVED,
        IGNORED,
        SUPPRESSED,
        AUTO_CORRECTED
    }

    /**
     * Creates a critical error DTO.
     */
    public static ValidationErrorDto createCritical(String batchId, String fieldName,
                                                     String errorCode, String message) {
        return ValidationErrorDto.builder()
                .errorId(java.util.UUID.randomUUID().toString())
                .batchId(batchId)
                .fieldName(fieldName)
                .errorCode(errorCode)
                .errorMessage(message)
                .errorLevel(ErrorLevelDto.CRITICAL)
                .errorType(ErrorTypeDto.MISSING_REQUIRED_FIELD)
                .errorTimestamp(LocalDateTime.now())
                .status(ErrorStatusDto.OPEN)
                .priority(1)
                .occurrences(1)
                .build();
    }

    /**
     * Creates a high priority error DTO.
     */
    public static ValidationErrorDto createHigh(String batchId, String fieldName,
                                                 String errorCode, String message) {
        return ValidationErrorDto.builder()
                .errorId(java.util.UUID.randomUUID().toString())
                .batchId(batchId)
                .fieldName(fieldName)
                .errorCode(errorCode)
                .errorMessage(message)
                .errorLevel(ErrorLevelDto.HIGH)
                .errorType(ErrorTypeDto.INVALID_FORMAT)
                .errorTimestamp(LocalDateTime.now())
                .status(ErrorStatusDto.OPEN)
                .priority(2)
                .occurrences(1)
                .build();
    }

    /**
     * Checks if error is in a terminal state.
     */
    public boolean isTerminalState() {
        return status == ErrorStatusDto.RESOLVED ||
                status == ErrorStatusDto.SUPPRESSED ||
                status == ErrorStatusDto.IGNORED ||
                status == ErrorStatusDto.AUTO_CORRECTED;
    }

    /**
     * Checks if error needs attention.
     */
    public boolean needsAttention() {
        return !isTerminalState() &&
                (errorLevel == ErrorLevelDto.CRITICAL ||
                        errorLevel == ErrorLevelDto.HIGH);
    }

    /**
     * Gets display name for error type.
     */
    public String getErrorTypeDisplayName() {
        return errorType != null ? errorType.name().replace("_", " ") : "UNKNOWN";
    }
}
