package com.gogidix.courier.availabilityservice.shared.exception;

/**
 * Exception thrown when a conflict occurs (e.g., duplicate resource).
 */
public class ConflictException extends BaseDomainException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
