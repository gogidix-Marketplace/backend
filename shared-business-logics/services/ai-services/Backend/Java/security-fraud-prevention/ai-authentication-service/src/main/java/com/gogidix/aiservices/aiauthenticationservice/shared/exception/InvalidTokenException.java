package com.gogidix.aiservices.aiauthenticationservice.shared.exception;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
