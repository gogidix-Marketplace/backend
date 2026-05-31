package com.gogidix.aiservices.aigatewayservice.interfaces.rest;

import com.gogidix.aiservices.aigatewayservice.shared.exception.BaseDomainException;
import com.gogidix.aiservices.aigatewayservice.shared.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(BaseDomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            BaseDomainException ex,
            WebRequest request
    ) {
        log.error("Domain exception: {}", ex.getMessage());

        HttpStatus status = determineStatus(ex);
        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                status.value(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        log.error("Illegal argument: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                "INVALID_ARGUMENT",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
            IllegalStateException ex,
            WebRequest request
    ) {
        log.error("Illegal state: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                "INVALID_STATE",
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request
    ) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        Map<String, Object> details = new HashMap<>();
        details.put("type", ex.getClass().getSimpleName());

        ErrorResponse errorResponse = ErrorResponse.of(
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false).replace("uri=", ""),
                details
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus determineStatus(BaseDomainException ex) {
        String errorCode = ex.getErrorCode();
        if (errorCode.contains("NOT_FOUND")) {
            return HttpStatus.NOT_FOUND;
        } else if (errorCode.contains("VALIDATION")) {
            return HttpStatus.BAD_REQUEST;
        } else if (errorCode.contains("CONFLICT")) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
