package com.gogidix.shared.exceptions.domain.exception;

/**
 * Authentication exception (401)
 * Security-related exception for authentication failures
 */
public class AuthenticationException extends BaseException {

    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_ERROR", 401, "SECURITY");
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause, "AUTHENTICATION_ERROR", 401, "SECURITY");
    }

    @Override
    public boolean isCritical() {
        return false; // Authentication failures are not critical (expected operation)
    }

    @Override
    public boolean isRetryable() {
        return false; // Authentication failures are not retryable
    }
}
