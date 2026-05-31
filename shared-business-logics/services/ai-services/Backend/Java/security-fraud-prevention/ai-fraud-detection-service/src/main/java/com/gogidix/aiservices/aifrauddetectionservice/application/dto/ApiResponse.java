package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Standard API response wrapper for all REST endpoints.
 * Provides consistent response structure across the application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Indicates whether the request was successful.
     */
    private boolean success;

    /**
     * HTTP status code.
     */
    private int statusCode;

    /**
     * Response message describing the result.
     */
    private String message;

    /**
     * The response payload data.
     */
    private T data;

    /**
     * Error details if the request failed.
     */
    private ErrorDetail error;

    /**
     * Timestamp when the response was generated.
     */
    @Builder.Default
    private Instant timestamp = Instant.now();

    /**
     * Request ID for tracing.
     */
    private String requestId;

    /**
     * Creates a success response with data.
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(200)
                .message("Success")
                .data(data)
                .build();
    }

    /**
     * Creates a success response with custom message.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(200)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Creates a success response with custom status code.
     */
    public static <T> ApiResponse<T> success(int statusCode, String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Creates an error response.
     */
    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(statusCode)
                .message(message)
                .error(new ErrorDetail(message, null))
                .build();
    }

    /**
     * Creates an error response with details.
     */
    public static <T> ApiResponse<T> error(int statusCode, String message, String errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(statusCode)
                .message(message)
                .error(new ErrorDetail(message, errorCode))
                .build();
    }

    /**
     * Creates an error response with field errors.
     */
    public static <T> ApiResponse<T> validationError(String message, List<FieldError> fieldErrors) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(400)
                .message(message)
                .error(new ErrorDetail(message, "VALIDATION_ERROR", fieldErrors))
                .build();
    }

    /**
     * Error details class.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @lombok.AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetail {
        private String message;
        private String code;
        private List<FieldError> fieldErrors;

        public ErrorDetail(String message, String code) {
            this.message = message;
            this.code = code;
            this.fieldErrors = null;
        }
    }

    /**
     * Field error details for validation errors.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;
    }
}
