package com.gogidix.finance.forecasting.interfaces.rest;

import com.gogidix.finance.forecasting.application.dto.response.ErrorResponseDto;
import com.gogidix.finance.forecasting.shared.exception.ConflictException;
import com.gogidix.finance.forecasting.shared.exception.NotFoundException;
import com.gogidix.finance.forecasting.shared.exception.ValidationException;
import com.gogidix.finance.forecasting.shared.requestcontext.RequestContextHolder;
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
 * Handles all exceptions and returns standardized error responses
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles NotFoundException
     *
     * @param ex the exception
     * @param request the web request
     * @return error response
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(
            NotFoundException ex, WebRequest request) {

        log.warn("Not found: {}", ex.getMessage());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(getPath(request))
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .errorCode("NOT_FOUND")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles ValidationException
     *
     * @param ex the exception
     * @param request the web request
     * @return error response
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(
            ValidationException ex, WebRequest request) {

        log.warn("Validation failed: {}", ex.getMessage());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message(ex.getMessage())
                .path(getPath(request))
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .errorCode("VALIDATION_ERROR")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles ConflictException
     *
     * @param ex the exception
     * @param request the web request
     * @return error response
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflict(
            ConflictException ex, WebRequest request) {

        log.warn("Conflict: {}", ex.getMessage());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .path(getPath(request))
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .errorCode("CONFLICT")
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handles IllegalArgumentException
     *
     * @param ex the exception
     * @param request the web request
     * @return error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        log.warn("Illegal argument: {}", ex.getMessage());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(getPath(request))
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .errorCode("ILLEGAL_ARGUMENT")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles IllegalStateException
     *
     * @param ex the exception
     * @param request the web request
     * @return error response
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalState(
            IllegalStateException ex, WebRequest request) {

        log.error("Illegal state: {}", ex.getMessage());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Invalid State")
                .message(ex.getMessage())
                .path(getPath(request))
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .errorCode("INVALID_STATE")
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handles MethodArgumentNotValidException (Bean Validation)
     *
     * @param ex the exception
     * @param request the web request
     * @return error response with field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.warn("Validation failed: {}", ex.getMessage());

        List<ErrorResponseDto.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorResponseDto.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .rejectedValue(error.getRejectedValue())
                        .code(error.getCode())
                        .build())
                .collect(Collectors.toList());

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Request validation failed. Please check the field errors for details.")
                .path(getPath(request))
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .fieldErrors(fieldErrors)
                .errorCode("FIELD_VALIDATION_ERROR")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles all other exceptions
     *
     * @param ex the exception
     * @param request the web request
     * @return error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(
            Exception ex, WebRequest request) {

        log.error("Unexpected error: ", ex);

        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred. Please try again later.")
                .path(getPath(request))
                .correlationId(getCorrelationId())
                .tenantId(getTenantId())
                .errorCode("INTERNAL_ERROR")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Extracts the request path from WebRequest
     *
     * @param request the web request
     * @return the request path
     */
    private String getPath(WebRequest request) {
        String description = request.getDescription(false);
        if (description != null && description.startsWith("uri=")) {
            return description.substring(4);
        }
        return description;
    }

    /**
     * Gets the correlation ID from request context
     *
     * @return the correlation ID or null
     */
    private String getCorrelationId() {
        try {
            return RequestContextHolder.getCorrelationId();
        } catch (Exception e) {
            log.debug("Could not get correlation ID: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Gets the tenant ID from request context
     *
     * @return the tenant ID or null
     */
    private String getTenantId() {
        try {
            return RequestContextHolder.getTenantId();
        } catch (Exception e) {
            log.debug("Could not get tenant ID: {}", e.getMessage());
            return null;
        }
    }
}
