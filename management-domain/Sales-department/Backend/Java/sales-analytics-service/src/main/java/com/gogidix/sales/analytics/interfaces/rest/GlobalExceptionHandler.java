package com.gogidix.sales.analytics.interfaces.rest;

import com.gogidix.sales.analytics.shared.exception.ConflictException;
import com.gogidix.sales.analytics.shared.exception.NotFoundException;
import com.gogidix.sales.analytics.shared.exception.ValidationException;
import com.gogidix.sales.analytics.shared.requestcontext.RequestContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for REST API
 * Handles all exceptions and returns standardized error responses
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(
            NotFoundException ex,
            WebRequest request) {

        log.warn("Not found: {} - {}", ex.getMessage(), request.getContextPath());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getContextPath())
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(
            ValidationException ex,
            WebRequest request) {

        log.warn("Validation failed: {}", ex.getMessage());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message(ex.getMessage())
                .path(request.getContextPath())
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflict(
            ConflictException ex,
            WebRequest request) {

        log.warn("Conflict: {}", ex.getMessage());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .path(request.getContextPath())
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request) {

        log.warn("Illegal argument: {}", ex.getMessage());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getContextPath())
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalState(
            IllegalStateException ex,
            WebRequest request) {

        log.warn("Illegal state: {}", ex.getMessage());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Invalid State")
                .message(ex.getMessage())
                .path(request.getContextPath())
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(
            Exception ex,
            WebRequest request) {

        log.error("Unexpected error: ", ex);

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path(request.getContextPath())
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Gets correlation ID from request context
     */
    private String getCorrelationId() {
        try {
            return RequestContextHolder.getCorrelationId();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /**
     * Gets tenant ID from request context
     */
    private String getTenantId() {
        try {
            return RequestContextHolder.getTenantId();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /**
     * Error Response DTO
     */
    public record ErrorResponseDto(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path,
            String correlationId,
            String tenantId
    ) {
        public static ErrorResponseDtoBuilder builder() {
            return new ErrorResponseDtoBuilder();
        }

        public static class ErrorResponseDtoBuilder {
            private Instant timestamp;
            private int status;
            private String error;
            private String message;
            private String path;
            private String correlationId;
            private String tenantId;

            public ErrorResponseDtoBuilder timestamp(Instant timestamp) {
                this.timestamp = timestamp;
                return this;
            }

            public ErrorResponseDtoBuilder status(int status) {
                this.status = status;
                return this;
            }

            public ErrorResponseDtoBuilder error(String error) {
                this.error = error;
                return this;
            }

            public ErrorResponseDtoBuilder message(String message) {
                this.message = message;
                return this;
            }

            public ErrorResponseDtoBuilder path(String path) {
                this.path = path;
                return this;
            }

            public ErrorResponseDtoBuilder correlationId(String correlationId) {
                this.correlationId = correlationId;
                return this;
            }

            public ErrorResponseDtoBuilder tenantId(String tenantId) {
                this.tenantId = tenantId;
                return this;
            }

            public ErrorResponseDto build() {
                return new ErrorResponseDto(timestamp, status, error, message, path, correlationId, tenantId);
            }
        }
    }
}
