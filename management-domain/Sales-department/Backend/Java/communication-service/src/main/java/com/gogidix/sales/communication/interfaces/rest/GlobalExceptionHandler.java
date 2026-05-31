package com.gogidix.sales.communication.interfaces.rest;

import com.gogidix.sales.communication.application.dto.response.ErrorResponseDto;
import com.gogidix.sales.communication.shared.exception.BusinessRuleException;
import com.gogidix.sales.communication.shared.exception.ConflictException;
import com.gogidix.sales.communication.shared.exception.NotFoundException;
import com.gogidix.sales.communication.shared.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global Exception Handler
 * Handles all exceptions and returns appropriate error responses
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(
            NotFoundException ex, WebRequest request) {
        log.warn("Not found exception: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            ValidationException ex, WebRequest request) {
        log.warn("Validation exception: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflictException(
            ConflictException ex, WebRequest request) {
        log.warn("Conflict exception: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessRuleException(
            BusinessRuleException ex, WebRequest request) {
        log.warn("Business rule exception: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Business Rule Violation",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.warn("Validation exception: {}", ex.getMessage());

        List<ErrorResponseDto.ValidationError> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = error instanceof FieldError ?
                            ((FieldError) error).getField() : error.getObjectName();
                    String errorMessage = error.getDefaultMessage();
                    Object rejectedValue = error instanceof FieldError ?
                            ((FieldError) error).getRejectedValue() : null;

                    return ErrorResponseDto.ValidationError.builder()
                            .field(fieldName)
                            .message(errorMessage)
                            .rejectedValue(rejectedValue)
                            .build();
                })
                .collect(Collectors.toList());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Invalid request parameters",
                request.getDescription(false).replace("uri=", ""),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        log.warn("Illegal argument exception: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        log.error("Illegal state exception: {}", ex.getMessage(), ex);

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.CONFLICT.value(),
                "Invalid State",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
            Exception ex, WebRequest request) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);

        ErrorResponseDto response = ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
