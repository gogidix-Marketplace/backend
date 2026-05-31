package com.gogidix.digitalmarketing.shared.exception;

/**
 * Exception thrown when a conflict occurs (e.g., duplicate resource).
 */
public class ConflictException extends DomainException {

    public ConflictException(String message) {
        super("CONFLICT", message);
    }

    public ConflictException(String resource, String id) {
        super("CONFLICT", String.format("%s already exists: %s", resource, id));
    }

    public ConflictException(String message, Throwable cause) {
        super("CONFLICT", message, cause);
    }
}
