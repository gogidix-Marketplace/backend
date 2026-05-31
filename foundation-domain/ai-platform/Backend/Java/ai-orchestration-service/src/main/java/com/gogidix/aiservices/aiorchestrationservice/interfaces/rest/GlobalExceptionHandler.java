package com.gogidix.aiservices.aiorchestrationservice.interfaces.rest;

import com.gogidix.aiservices.aiorchestrationservice.shared.exception.BaseDomainException;
import com.gogidix.aiservices.aiorchestrationservice.shared.exception.ErrorResponse;
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
        HttpStatus status = ex.getErrorCode().contains("NOT_FOUND") ? HttpStatus.NOT_FOUND :
                          ex.getErrorCode().contains("VALIDATION") ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(ErrorResponse.of(ex.getErrorCode(), ex.getMessage(), status.value(),
                request.getDescription(false).replace("uri=", "")), status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        Map<String, Object> details = new HashMap<>();
        details.put("type", ex.getClass().getSimpleName());
        return new ResponseEntity<>(ErrorResponse.of("INTERNAL_ERROR", "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(), request.getDescription(false).replace("uri=", ""), details),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
