package com.gogidix.shared.exceptions.domain.exception;

/**
 * Database exception for data access failures
 */
public class DatabaseException extends TechnicalException {

    public DatabaseException(String message) {
        super(message, "DATABASE_ERROR", 500, "DATABASE");
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause, "DATABASE_ERROR", 500, "DATABASE");
    }

    @Override
    public boolean isRetryable() {
        return false; // Database errors are not automatically retryable
    }
}
