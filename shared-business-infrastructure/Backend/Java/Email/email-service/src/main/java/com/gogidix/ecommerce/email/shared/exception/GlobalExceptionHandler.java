package com.gogidix.ecommerce.email.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EmailNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("timestamp", LocalDateTime.now(), "status", 404, "error", "Not Found", "message", ex.getMessage()));
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(EmailDuplicateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("timestamp", LocalDateTime.now(), "status", 409, "error", "Conflict", "message", ex.getMessage()));
    }

    @ExceptionHandler(EmailInvalidException.class)
    public ResponseEntity<Map<String, Object>> handleInvalid(EmailInvalidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("timestamp", LocalDateTime.now(), "status", 400, "error", "Bad Request", "message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("timestamp", LocalDateTime.now(), "status", 400, "error", "Bad Request", "message", ex.getMessage()));
    }
}