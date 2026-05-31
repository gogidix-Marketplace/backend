package com.gogidix.finance.forecasting.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Error Response DTO
 * Standard error response format for API errors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    /**
     * Timestamp when the error occurred
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp;

    /**
     * HTTP status code
     */
    private int status;

    /**
     * Error type/category
     */
    private String error;

    /**
     * Detailed error message
     */
    private String message;

    /**
     * Request path that caused the error
     */
    private String path;

    /**
     * Correlation ID for request tracing
     */
    private String correlationId;

    /**
     * Tenant ID for multi-tenancy context
     */
    private String tenantId;

    /**
     * List of field-specific validation errors
     */
    private List<FieldError> fieldErrors;

    /**
     * Additional error details
     */
    private Object details;

    /**
     * Error code for programmatic handling
     */
    private String errorCode;

    /**
     * Request ID for tracking
     */
    private String requestId;

    /**
     * Field Error DTO
     * Represents individual field validation errors
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldError {

        /**
         * Field name that has validation error
         */
        private String field;

        /**
         * Error message for the field
         */
        private String message;

        /**
         * The rejected value that caused the error
         */
        private Object rejectedValue;

        /**
         * Error code for the field error
         */
        private String code;

        /**
         * Path to the field (for nested objects)
         */
        private String path;
    }

    /**
     * Creates a not found error response
     *
     * @param resource the resource type
     * @param id the resource identifier
     * @param path the request path
     * @param correlationId the correlation ID
     * @param tenantId the tenant ID
     * @return error response DTO
     */
    public static ErrorResponseDto notFound(String resource, String id, String path,
                                             String correlationId, String tenantId) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(404)
                .error("Not Found")
                .message(resource + " with id '" + id + "' not found")
                .path(path)
                .correlationId(correlationId)
                .tenantId(tenantId)
                .errorCode("NOT_FOUND")
                .build();
    }

    /**
     * Creates a validation error response
     *
     * @param message the error message
     * @param path the request path
     * @param correlationId the correlation ID
     * @param tenantId the tenant ID
     * @return error response DTO
     */
    public static ErrorResponseDto validationError(String message, String path,
                                                    String correlationId, String tenantId) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(400)
                .error("Validation Failed")
                .message(message)
                .path(path)
                .correlationId(correlationId)
                .tenantId(tenantId)
                .errorCode("VALIDATION_ERROR")
                .build();
    }

    /**
     * Creates a conflict error response
     *
     * @param message the error message
     * @param path the request path
     * @param correlationId the correlation ID
     * @param tenantId the tenant ID
     * @return error response DTO
     */
    public static ErrorResponseDto conflict(String message, String path,
                                            String correlationId, String tenantId) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(409)
                .error("Conflict")
                .message(message)
                .path(path)
                .correlationId(correlationId)
                .tenantId(tenantId)
                .errorCode("CONFLICT")
                .build();
    }

    /**
     * Creates an internal server error response
     *
     * @param message the error message
     * @param path the request path
     * @param correlationId the correlation ID
     * @param tenantId the tenant ID
     * @return error response DTO
     */
    public static ErrorResponseDto internalError(String message, String path,
                                                  String correlationId, String tenantId) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(500)
                .error("Internal Server Error")
                .message(message)
                .path(path)
                .correlationId(correlationId)
                .tenantId(tenantId)
                .errorCode("INTERNAL_ERROR")
                .build();
    }

    /**
     * Creates a bad request error response
     *
     * @param message the error message
     * @param path the request path
     * @param correlationId the correlation ID
     * @param tenantId the tenant ID
     * @return error response DTO
     */
    public static ErrorResponseDto badRequest(String message, String path,
                                               String correlationId, String tenantId) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(400)
                .error("Bad Request")
                .message(message)
                .path(path)
                .correlationId(correlationId)
                .tenantId(tenantId)
                .errorCode("BAD_REQUEST")
                .build();
    }

    /**
     * Creates an unauthorized error response
     *
     * @param message the error message
     * @param path the request path
     * @param correlationId the correlation ID
     * @return error response DTO
     */
    public static ErrorResponseDto unauthorized(String message, String path,
                                                 String correlationId) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(401)
                .error("Unauthorized")
                .message(message)
                .path(path)
                .correlationId(correlationId)
                .errorCode("UNAUTHORIZED")
                .build();
    }

    /**
     * Creates a forbidden error response
     *
     * @param message the error message
     * @param path the request path
     * @param correlationId the correlation ID
     * @param tenantId the tenant ID
     * @return error response DTO
     */
    public static ErrorResponseDto forbidden(String message, String path,
                                              String correlationId, String tenantId) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(403)
                .error("Forbidden")
                .message(message)
                .path(path)
                .correlationId(correlationId)
                .tenantId(tenantId)
                .errorCode("FORBIDDEN")
                .build();
    }

    /**
     * Adds field errors to the error response
     *
     * @param fieldErrors the list of field errors
     * @return this error response with field errors
     */
    public ErrorResponseDto withFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
        return this;
    }

    /**
     * Creates field error from validation error
     *
     * @param field the field name
     * @param message the error message
     * @param rejectedValue the rejected value
     * @return field error DTO
     */
    public static FieldError createFieldError(String field, String message, Object rejectedValue) {
        return FieldError.builder()
                .field(field)
                .message(message)
                .rejectedValue(rejectedValue)
                .build();
    }
}
