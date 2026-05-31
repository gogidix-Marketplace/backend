package com.gogidix.shared.infrastructure.services.security.auth.domain.exception;

/**
 * Exception thrown when authentication credentials are invalid.
 */
public class InvalidCredentialsException extends AuthenticationException {

    public InvalidCredentialsException() {
        super("Invalid username or password");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
