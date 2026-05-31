package com.gogidix.shared.exceptions.domain.exception;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Base exception for all Gogidix exceptions
 * Provides common exception handling functionality with rich context
 */
@Getter
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    private final int httpStatus;
    private final LocalDateTime timestamp;
    private final Map<String, Object> context;
    private final String category;
    
    protected BaseException(String message, String errorCode, int httpStatus, String category) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.category = category;
        this.timestamp = LocalDateTime.now();
        this.context = new HashMap<>();
    }
    
    protected BaseException(String message, Throwable cause, String errorCode, int httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.category = "GENERAL";
        this.timestamp = LocalDateTime.now();
        this.context = new HashMap<>();
    }
    
    protected BaseException(String message, Throwable cause, String errorCode, int httpStatus, String category) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.category = category;
        this.timestamp = LocalDateTime.now();
        this.context = new HashMap<>();
    }
    
    public void addContext(String key, Object value) {
        this.context.put(key, value);
    }
    
    public boolean isCritical() {
        return httpStatus >= 500;
    }
    
    public boolean isRetryable() {
        return httpStatus == 503 || httpStatus == 504;
    }
}
