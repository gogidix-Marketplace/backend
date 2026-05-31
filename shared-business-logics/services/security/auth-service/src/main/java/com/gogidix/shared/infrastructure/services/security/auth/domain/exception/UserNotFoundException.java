package com.gogidix.shared.infrastructure.services.security.auth.domain.exception;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends AuthenticationException {

    private final String userId;

    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
        this.userId = userId;
    }

    public UserNotFoundException(String field, String value) {
        super("User not found with " + field + ": " + value);
        this.userId = value;
    }

    public String getUserId() {
        return userId;
    }
}
