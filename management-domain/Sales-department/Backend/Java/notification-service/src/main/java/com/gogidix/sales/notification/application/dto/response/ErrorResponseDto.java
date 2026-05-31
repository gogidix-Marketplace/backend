package com.gogidix.sales.notification.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Error Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private int status;
    private String error;
    private String message;
    private List<String> details;
    private String path;
    private Instant timestamp;
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

    public static ErrorResponseDto of(int status, String error, String message, String path, List<String> details) {
        return ErrorResponseDto.builder()
                .status(status)
                .error(error)
                .message(message)
                .details(details)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }
}
