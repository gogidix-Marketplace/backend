package com.gogidix.management.shared.exception;

public class AccessDeniedException extends DomainException {
    public AccessDeniedException(String message) {
        super(message, "ACCESS_DENIED");
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
