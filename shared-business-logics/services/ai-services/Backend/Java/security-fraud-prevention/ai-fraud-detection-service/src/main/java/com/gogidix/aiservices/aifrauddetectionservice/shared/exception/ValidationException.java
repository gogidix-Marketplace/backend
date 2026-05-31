package com.gogidix.aiservices.aifrauddetectionservice.shared.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown when input validation fails.
 * <p>
 * This exception can contain multiple validation errors and is used
 * to provide detailed feedback about what failed validation.
 */
@Getter
public class ValidationException extends FraudDetectionException {

    private final String fieldName;
    private final Object rejectedValue;
    private final List<ValidationError> validationErrors;

    /**
     * Constructs a new ValidationException for a single field.
     *
     * @param fieldName     the name of the field that failed validation
     * @param rejectedValue the value that was rejected
     * @param message       the detail message
     */
    public ValidationException(String fieldName, Object rejectedValue, String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.validationErrors = new ArrayList<>();
    }

    /**
     * Constructs a new ValidationException with multiple validation errors.
     *
     * @param message          the detail message
     * @param validationErrors list of validation errors
     */
    public ValidationException(String message, List<ValidationError> validationErrors) {
        super(ErrorCode.VALIDATION_ERROR, message);
        this.fieldName = null;
        this.rejectedValue = null;
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    /**
     * Constructs a new ValidationException with a cause.
     *
     * @param fieldName     the name of the field that failed validation
     * @param rejectedValue the value that was rejected
     * @param message       the detail message
     * @param cause         the root cause
     */
    public ValidationException(String fieldName, Object rejectedValue, String message, Throwable cause) {
        super(ErrorCode.VALIDATION_ERROR, message, cause);
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.validationErrors = new ArrayList<>();
    }

    /**
     * Constructs a new ValidationException with full specification.
     *
     * @param errorCode       the specific error code
     * @param fieldName       the name of the field that failed validation
     * @param rejectedValue   the value that was rejected
     * @param message         the detail message
     * @param cause           the root cause
     * @param errorId         the unique error ID
     * @param timestamp       the timestamp when the error occurred
     * @param validationErrors list of validation errors
     */
    public ValidationException(ErrorCode errorCode, String fieldName, Object rejectedValue,
                              String message, Throwable cause, String errorId,
                              Instant timestamp, List<ValidationError> validationErrors) {
        super(errorCode, message, cause, errorId, timestamp);
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.validationErrors = validationErrors != null ? new ArrayList<>(validationErrors) : new ArrayList<>();
    }

    /**
     * Adds a validation error to the list.
     *
     * @param error the validation error to add
     */
    public void addValidationError(ValidationError error) {
        this.validationErrors.add(error);
    }

    /**
     * Checks if there are any validation errors.
     *
     * @return true if there are validation errors, false otherwise
     */
    public boolean hasValidationErrors() {
        return !validationErrors.isEmpty();
    }

    /**
     * Creates a ValidationException for a missing required field.
     *
     * @param fieldName the name of the missing field
     * @return a new ValidationException instance
     */
    public static ValidationException missingRequiredField(String fieldName) {
        String message = String.format("Required field '%s' is missing", fieldName);
        return new ValidationException(fieldName, null, message);
    }

    /**
     * Creates a ValidationException for an invalid format.
     *
     * @param fieldName the name of the field with invalid format
     * @param format    the expected format
     * @return a new ValidationException instance
     */
    public static ValidationException invalidFormat(String fieldName, String format) {
        String message = String.format("Field '%s' has invalid format. Expected: %s", fieldName, format);
        return new ValidationException(fieldName, null, message);
    }

    /**
     * Creates a ValidationException for an out of range value.
     *
     * @param fieldName    the name of the field
     * @param value        the out of range value
     * @param min          the minimum allowed value
     * @param max          the maximum allowed value
     * @return a new ValidationException instance
     */
    public static ValidationException outOfRange(String fieldName, Object value, Object min, Object max) {
        String message = String.format("Field '%s' value %s is out of range [%s, %s]", fieldName, value, min, max);
        return new ValidationException(fieldName, value, message);
    }

    /**
     * Creates a ValidationException for a generic invalid input.
     *
     * @param fieldName the name of the field
     * @param value     the invalid value
     * @param reason    the reason for invalidity
     * @return a new ValidationException instance
     */
    public static ValidationException invalidInput(String fieldName, Object value, String reason) {
        String message = String.format("Invalid value for field '%s': %s. Reason: %s", fieldName, value, reason);
        return new ValidationException(fieldName, value, message);
    }

    /**
     * Represents a single validation error.
     */
    @Getter
    public static class ValidationError {
        private final String fieldName;
        private final String message;
        private final Object rejectedValue;

        public ValidationError(String fieldName, String message, Object rejectedValue) {
            this.fieldName = fieldName;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }
    }
}
