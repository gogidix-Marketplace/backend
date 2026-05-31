package com.gogidix.management.shared.exception;

public class ConflictException extends DomainException {
    public ConflictException(String message) {
        super(message, "CONFLICT");
    }

    public ConflictException(String type, String message) {
        super(type + ": " + message, "CONFLICT");
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
