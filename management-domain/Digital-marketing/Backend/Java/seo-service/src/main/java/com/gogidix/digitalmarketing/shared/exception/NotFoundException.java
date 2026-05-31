package com.gogidix.digitalmarketing.shared.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class NotFoundException extends DomainException {

    public NotFoundException(String resource, String id) {
        super("NOT_FOUND", String.format("%s not found: %s", resource, id));
    }

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }

    public NotFoundException(String resource, String id, Throwable cause) {
        super("NOT_FOUND", String.format("%s not found: %s", resource, id), cause);
    }
}
