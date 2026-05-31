package com.gogidix.aiservices.leadgenerationaiservice.interfaces.rest;

import com.gogidix.aiservices.leadgenerationaiservice.shared.exception.LeadGenerationException;
import com.gogidix.aiservices.leadgenerationaiservice.shared.exception.LeadNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LeadNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLeadNotFound(LeadNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(LeadGenerationException.class)
    public ResponseEntity<Map<String, Object>> handleLeadGenerationException(LeadGenerationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("message", message);
        error.put("timestamp", Instant.now());
        return error;
    }
}
