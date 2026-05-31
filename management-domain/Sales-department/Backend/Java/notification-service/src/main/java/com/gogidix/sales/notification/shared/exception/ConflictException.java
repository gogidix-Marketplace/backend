package com.gogidix.sales.notification.shared.exception;

/**
 * Conflict Exception
 * Thrown when a request conflicts with current state
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
