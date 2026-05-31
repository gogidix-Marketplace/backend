package com.gogidix.aiservices.aimonitoringservice.interfaces.rest;

import com.gogidix.aiservices.aimonitoringservice.shared.exception.BaseDomainException;
import com.gogidix.aiservices.aimonitoringservice.shared.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseDomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(BaseDomainException ex, WebRequest request) {
        HttpStatus status = determineStatus(ex);
        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getErrorCode(), ex.getMessage(), status.value(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("type", ex.getClass().getSimpleName());
        ErrorResponse errorResponse = ErrorResponse.of(
                "INTERNAL_ERROR", "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false).replace("uri=", ""), details
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus determineStatus(BaseDomainException ex) {
        if (ex.getErrorCode().contains("NOT_FOUND")) return HttpStatus.NOT_FOUND;
        if (ex.getErrorCode().contains("VALIDATION")) return HttpStatus.BAD_REQUEST;
        if (ex.getErrorCode().contains("CONFLICT")) return HttpStatus.CONFLICT;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
