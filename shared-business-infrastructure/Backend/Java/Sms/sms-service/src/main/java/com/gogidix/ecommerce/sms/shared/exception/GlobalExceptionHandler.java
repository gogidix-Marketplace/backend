package com.gogidix.ecommerce.sms.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SmsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(SmsNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("timestamp", LocalDateTime.now(), "status", 404, "error", "Not Found", "message", ex.getMessage()));
    }

    @ExceptionHandler(SmsDuplicateException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(SmsDuplicateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("timestamp", LocalDateTime.now(), "status", 409, "error", "Conflict", "message", ex.getMessage()));
    }

    @ExceptionHandler(SmsInvalidException.class)
    public ResponseEntity<Map<String, Object>> handleInvalid(SmsInvalidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("timestamp", LocalDateTime.now(), "status", 400, "error", "Bad Request", "message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("timestamp", LocalDateTime.now(), "status", 400, "error", "Bad Request", "message", ex.getMessage()));
    }
}