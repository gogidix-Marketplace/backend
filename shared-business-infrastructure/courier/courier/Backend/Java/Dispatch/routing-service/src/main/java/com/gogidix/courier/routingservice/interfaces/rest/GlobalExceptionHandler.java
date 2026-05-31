package com.gogidix.courier.routingservice.interfaces.rest;

import com.gogidix.courier.routingservice.shared.exception.BaseDomainException;
import com.gogidix.courier.routingservice.shared.exception.ConflictException;
import com.gogidix.courier.routingservice.shared.exception.NotFoundException;
import com.gogidix.courier.routingservice.shared.exception.ValidationException;
import io.swagger.v3.oas.annotations.media.Schema;
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

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, WebRequest request) {
        log.warn("Not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        Instant.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "NOT_FOUND",
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, WebRequest request) {
        log.warn("Conflict: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        Instant.now(),
                        HttpStatus.CONFLICT.value(),
                        "CONFLICT",
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex, WebRequest request) {
        log.warn("Validation error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "VALIDATION_ERROR",
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation failed: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "VALIDATION_ERROR",
                        "Validation failed",
                        request.getDescription(false).replace("uri=", ""),
                        errors
                ));
    }

    @ExceptionHandler(BaseDomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(BaseDomainException ex, WebRequest request) {
        log.error("Domain error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getErrorCode(),
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        Instant.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "INTERNAL_ERROR",
                        "An unexpected error occurred",
                        request.getDescription(false).replace("uri=", "")
                ));
    }

    @Schema(description = "Error response structure")
    public record ErrorResponse(
            @Schema(description = "Timestamp of the error")
            Instant timestamp,

            @Schema(description = "HTTP status code")
            int status,

            @Schema(description = "Error code")
            String error,

            @Schema(description = "Error message")
            String message,

            @Schema(description = "Request path")
            String path,

            @Schema(description = "Field validation errors")
            Map<String, String> fieldErrors
    ) {
        public ErrorResponse {
            if (fieldErrors == null) {
                fieldErrors = new HashMap<>();
            }
        }

        // Constructor for errors without field validation errors
        public ErrorResponse(Instant timestamp, int status, String error, String message, String path) {
            this(timestamp, status, error, message, path, new HashMap<>());
        }
    }
}
