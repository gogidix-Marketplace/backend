package com.gogidix.aiservices.aisalesforecastingservice.interfaces.rest;

import com.gogidix.aiservices.aisalesforecastingservice.application.dto.ErrorResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.shared.context.RequestContextHolder;
import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        log.debug("Resource not found: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                getRequestPath(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException ex) {
        log.debug("Validation error: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                getRequestPath(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.debug("Method argument validation failed: {}", ex.getMessage());

        List<ErrorResponseDto.ValidationError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorResponseDto.ValidationError.of(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .collect(Collectors.toList());

        ErrorResponseDto response = ErrorResponseDto.validation(
                "Validation failed for one or more fields",
                getRequestPath(),
                getCorrelationId(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponseDto> handleMessageNotReadable(Exception ex) {
        log.debug("Message not readable: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Invalid request format or type mismatch",
                getRequestPath(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException ex) {
        log.debug("Access denied: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "You do not have permission to access this resource",
                getRequestPath(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException ex) {
        log.debug("Bad credentials: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Invalid or missing authentication credentials",
                getRequestPath(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoHandlerFound(NoHandlerFoundException ex) {
        log.debug("No handler found: {}", ex.getRequestURL());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "The requested endpoint does not exist: " + ex.getRequestURL(),
                getRequestPath(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        log.debug("Illegal argument: {}", ex.getMessage());

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                getRequestPath(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalState(IllegalStateException ex) {
        log.error("Illegal state: {}", ex.getMessage(), ex);

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                getRequestPath(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnhandledException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);

        ErrorResponseDto response = ErrorResponseDto.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                getRequestPath(),
                getCorrelationId()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private String getRequestPath() {
        return "";
    }

    private String getCorrelationId() {
        try {
            return RequestContextHolder.getCorrelationId();
        } catch (Exception e) {
            return java.util.UUID.randomUUID().toString();
        }
    }
}
