package com.gogidix.courier.gpstrackingservice.shared.exception;

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

    @Override
    protected String deriveErrorCode() {
        return "CONFLICT";
    }
}
