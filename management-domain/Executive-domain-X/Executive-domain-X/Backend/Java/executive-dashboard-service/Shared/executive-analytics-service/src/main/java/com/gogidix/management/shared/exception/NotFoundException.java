package com.gogidix.management.shared.exception;

public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(message, "NOT_FOUND");
    }

    public NotFoundException(String type, String id) {
        super(type + " not found: " + id, "NOT_FOUND");
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
