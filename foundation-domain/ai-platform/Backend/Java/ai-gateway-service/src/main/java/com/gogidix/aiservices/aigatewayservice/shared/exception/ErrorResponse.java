package com.gogidix.aiservices.aigatewayservice.shared.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Standard error response structure.
 */
public record ErrorResponse(
        String errorCode,
        String message,
        int status,
        Instant timestamp,
        String path,
        Map<String, Object> details
) {
    public static ErrorResponse of(String errorCode, String message, int status, String path) {
        return new ErrorResponse(errorCode, message, status, Instant.now(), path, null);
    }

    public static ErrorResponse of(String errorCode, String message, int status, String path, Map<String, Object> details) {
        return new ErrorResponse(errorCode, message, status, Instant.now(), path, details);
    }
}
