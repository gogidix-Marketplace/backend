package com.gogidix.sales.dealmanagement.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Error Response DTO
 * Standard format for error responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private int status;
    private String error;
    private String message;
    private String path;
    private Instant timestamp;
    private Map<String, Object> details;
    private String correlationId;

    public static ErrorResponseDto of(int status, String error, String message, String path) {
        return ErrorResponseDto.builder()
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }

    public static ErrorResponseDto withDetails(int status, String error, String message,
                                                String path, Map<String, Object> details) {
        return ErrorResponseDto.builder()
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .details(details)
                .build();
    }
}
