package com.gogidix.courier.gpstrackingservice.interfaces.rest;

import com.gogidix.courier.gpstrackingservice.shared.exception.BaseDomainException;
import com.gogidix.courier.gpstrackingservice.shared.exception.ConflictException;
import com.gogidix.courier.gpstrackingservice.shared.exception.NotFoundException;
import com.gogidix.courier.gpstrackingservice.shared.exception.ValidationException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for GPS Tracking Service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, WebRequest request) {
        log.warn("Not found: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex, WebRequest request) {
        log.warn("Validation error: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, WebRequest request) {
        log.warn("Conflict: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        log.warn("Validation failed: {}", ex.getMessage());

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                        (existing, replacement) -> existing + "; " + replacement
                ));

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Request validation failed",
                request.getDescription(false).replace("uri=", ""),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        log.warn("Constraint violation: {}", ex.getMessage());

        Map<String, String> fieldErrors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> {
                            String path = violation.getPropertyPath().toString();
                            return path.substring(path.lastIndexOf('.') + 1);
                        },
                        ConstraintViolation::getMessage
                ));

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Request validation failed",
                request.getDescription(false).replace("uri=", ""),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BaseDomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(BaseDomainException ex, WebRequest request) {
        log.error("Domain exception: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getErrorCode(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, WebRequest request) {
        log.error("Unexpected error", ex);

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @Schema(description = "Error response wrapper")
    public record ErrorResponse(
            @Schema(description = "Error timestamp", example = "2025-02-20T10:30:00Z")
            Instant timestamp,

            @Schema(description = "HTTP status code", example = "400")
            int status,

            @Schema(description = "Error code", example = "VALIDATION_ERROR")
            String errorCode,

            @Schema(description = "Error message", example = "Validation failed")
            String message,

            @Schema(description = "Request path", example = "/api/v1/tracking/gps/driver-123")
            String path,

            @Schema(description = "Field-level validation errors")
            Map<String, String> fieldErrors
    ) {
        public ErrorResponse {
            if (fieldErrors == null) {
                fieldErrors = new HashMap<>();
            }
        }

        // Constructor for errors without field validation errors
        public ErrorResponse(Instant timestamp, int status, String errorCode, String message, String path) {
            this(timestamp, status, errorCode, message, path, new HashMap<>());
        }
    }
}
