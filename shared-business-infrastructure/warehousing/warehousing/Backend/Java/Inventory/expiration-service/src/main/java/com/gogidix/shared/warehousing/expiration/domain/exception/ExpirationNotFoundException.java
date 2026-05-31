package com.gogidix.shared.warehousing.expiration.domain.exception;

/**
 * Exception thrown when an expiration record is not found
 */
public class ExpirationNotFoundException extends RuntimeException {

    public ExpirationNotFoundException(String message) {
        super(message);
    }

    public ExpirationNotFoundException(String entityType, String id) {
        super(String.format("%s not found with id: %s", entityType, id));
    }
}
