package com.gogidix.aiservices.aicustomersegmentationservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

/**
 * DTO for error response.
 */
@Schema(description = "Error response DTO")
public record ErrorResponseDto(

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "Error type", example = "Bad Request")
        String error,

        @Schema(description = "Error message", example = "Validation failed for field 'name'")
        String message,

        @Schema(description = "Timestamp when the error occurred", example = "2024-01-20T14:45:00Z")
        Instant timestamp,

        @Schema(description = "Request path", example = "/api/v1/segments")
        String path,

        @Schema(description = "Detailed validation errors")
        List<ValidationError> fieldErrors,

        @Schema(description = "Correlation ID for tracking", example = "a1b2c3d4e5f6")
        String correlationId

) {
    /**
     * Creates a basic error response without field errors.
     */
    public static ErrorResponseDto of(int status, String error, String message, String path, String correlationId) {
        return new ErrorResponseDto(status, error, message, Instant.now(), path, null, correlationId);
    }

    /**
     * Creates a validation error response with field errors.
     */
    public static ErrorResponseDto validation(String message, String path, String correlationId, List<ValidationError> fieldErrors) {
        return new ErrorResponseDto(400, "Bad Request", message, Instant.now(), path, fieldErrors, correlationId);
    }

    /**
     * Represents a single field validation error.
     */
    @Schema(description = "Field validation error")
    public record ValidationError(
            @Schema(description = "Field name", example = "name")
            String field,

            @Schema(description = "Error message", example = "must not be blank")
            String message,

            @Schema(description = "Rejected value", example = "")
            Object rejectedValue
    ) {
        public static ValidationError of(String field, String message, Object rejectedValue) {
            return new ValidationError(field, message, rejectedValue);
        }
    }
}
