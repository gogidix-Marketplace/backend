package com.gogidix.aiservices.aifrauddetectionservice.shared.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Standard API error response structure.
 * <p>
 * This class provides a consistent format for error responses across all APIs,
 * including error codes, messages, timestamps, and optional additional details.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Unique identifier for this error instance.
     */
    private String errorId;

    /**
     * The error code (e.g., FRAUD-2000).
     */
    private String code;

    /**
     * Human-readable error description.
     */
    private String message;

    /**
     * The specific error code enum value.
     */
    private String errorType;

    /**
     * Timestamp when the error occurred.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp;

    /**
     * The HTTP status code.
     */
    private Integer status;

    /**
     * The request path that caused the error.
     */
    private String path;

    /**
     * Additional error details.
     */
    private Map<String, Object> details;

    /**
     * List of validation errors (for validation exceptions).
     */
    private List<ValidationErrorItem> validationErrors;

    /**
     * Stack trace in development mode (optional).
     */
    private String stackTrace;

    /**
     * Creates an ErrorResponse from a FraudDetectionException.
     *
     * @param exception the exception
     * @param status    the HTTP status code
     * @param path      the request path
     * @return a new ErrorResponse instance
     */
    public static ErrorResponse fromFraudDetectionException(FraudDetectionException exception, int status, String path) {
        ErrorResponseBuilder builder = ErrorResponse.builder()
                .errorId(exception.getErrorId())
                .code(exception.getErrorCode().getCode())
                .message(exception.getMessage())
                .errorType(exception.getErrorCode().name())
                .timestamp(exception.getTimestamp())
                .status(status)
                .path(path);

        // Add policy violation specific details
        if (exception instanceof PolicyViolationException) {
            PolicyViolationException pve = (PolicyViolationException) exception;
            builder = builder.details(Map.of(
                    "policyId", pve.getPolicyId() != null ? pve.getPolicyId() : "N/A",
                    "ruleId", pve.getRuleId() != null ? pve.getRuleId() : "N/A",
                    "fraudScore", pve.getFraudScore() != null ? pve.getFraudScore() : "N/A",
                    "threshold", pve.getThreshold() != null ? pve.getThreshold() : "N/A"
            ));
        }

        // Add validation errors for ValidationException
        if (exception instanceof ValidationException) {
            ValidationException ve = (ValidationException) exception;
            if (ve.hasValidationErrors()) {
                List<ValidationErrorItem> items = ve.getValidationErrors().stream()
                        .map(veError -> new ValidationErrorItem(veError.getFieldName(), veError.getMessage(), veError.getRejectedValue()))
                        .toList();
                builder = builder.validationErrors(items);
            } else if (ve.getFieldName() != null) {
                builder = builder.validationErrors(List.of(
                        new ValidationErrorItem(ve.getFieldName(), ve.getMessage(), ve.getRejectedValue())
                ));
            }
        }

        return builder.build();
    }

    /**
     * Creates a generic ErrorResponse.
     *
     * @param code     the error code
     * @param message  the error message
     * @param status   the HTTP status code
     * @param path     the request path
     * @return a new ErrorResponse instance
     */
    public static ErrorResponse create(String code, String message, int status, String path) {
        return ErrorResponse.builder()
                .errorId(java.util.UUID.randomUUID().toString())
                .code(code)
                .message(message)
                .timestamp(Instant.now())
                .status(status)
                .path(path)
                .build();
    }

    /**
     * Creates an ErrorResponse with additional details.
     *
     * @param code     the error code
     * @param message  the error message
     * @param status   the HTTP status code
     * @param path     the request path
     * @param details  additional details
     * @return a new ErrorResponse instance
     */
    public static ErrorResponse createWithDetails(String code, String message, int status, String path, Map<String, Object> details) {
        return ErrorResponse.builder()
                .errorId(java.util.UUID.randomUUID().toString())
                .code(code)
                .message(message)
                .timestamp(Instant.now())
                .status(status)
                .path(path)
                .details(details)
                .build();
    }

    /**
     * Represents a single validation error item in the response.
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ValidationErrorItem {
        private final String field;
        private final String message;
        private final Object rejectedValue;

        public ValidationErrorItem(String field, String message, Object rejectedValue) {
            this.field = field;
            this.message = message;
            this.rejectedValue = rejectedValue;
        }
    }
}
