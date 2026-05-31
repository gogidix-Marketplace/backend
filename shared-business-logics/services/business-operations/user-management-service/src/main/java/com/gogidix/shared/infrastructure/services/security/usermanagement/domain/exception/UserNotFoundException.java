package com.gogidix.shared.infrastructure.services.security.usermanagement.domain.exception;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
    }

    public UserNotFoundException(String field, String value) {
        super("User not found with " + field + ": " + value);
    }
}
