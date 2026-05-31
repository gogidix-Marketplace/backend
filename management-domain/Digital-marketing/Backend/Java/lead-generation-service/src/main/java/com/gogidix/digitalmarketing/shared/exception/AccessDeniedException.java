package com.gogidix.digitalmarketing.shared.exception;

/**
 * Exception thrown when access is denied to a resource.
 */
public class AccessDeniedException extends DomainException {

    public AccessDeniedException(String message) {
        super("ACCESS_DENIED", message);
    }

    public AccessDeniedException(String resource, String action) {
        super("ACCESS_DENIED", String.format("Access denied to %s for action: %s", resource, action));
    }
}
