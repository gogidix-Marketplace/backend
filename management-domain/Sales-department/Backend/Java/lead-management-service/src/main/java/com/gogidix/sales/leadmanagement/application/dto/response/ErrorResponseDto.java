package com.gogidix.sales.leadmanagement.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Error Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private int status;
    private String error;
    private String message;
    private String path;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp;

    private String correlationId;
    private Map<String, Object> details;

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

    public static ErrorResponseDto badRequest(String message, String path) {
        return of(400, "Bad Request", message, path);
    }

    public static ErrorResponseDto notFound(String message, String path) {
        return of(404, "Not Found", message, path);
    }

    public static ErrorResponseDto conflict(String message, String path) {
        return of(409, "Conflict", message, path);
    }

    public static ErrorResponseDto internalError(String message, String path) {
        return of(500, "Internal Server Error", message, path);
    }
}
