package com.gogidix.finance.globalfinancedashboard.application.dto.response;

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
 * Standard error response format for the API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

    private String correlationId;

    private String tenantId;

    private String userId;

    private List<FieldError> fieldErrors;

    /**
     * Field error details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;
        private String code;
    }

    /**
     * Creates a basic error response
     */
    public static ErrorResponseDto of(int status, String error, String message) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(status)
                .error(error)
                .message(message)
                .build();
    }

    /**
     * Creates an error response with field errors
     */
    public static ErrorResponseDto withFieldErrors(int status, String error, String message,
                                                    List<FieldError> fieldErrors) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(status)
                .error(error)
                .message(message)
                .fieldErrors(fieldErrors)
                .build();
    }

    /**
     * Creates an error response with request context
     */
    public static ErrorResponseDto withContext(int status, String error, String message,
                                               String path, String correlationId, String tenantId) {
        return ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .correlationId(correlationId)
                .tenantId(tenantId)
                .build();
    }
}
