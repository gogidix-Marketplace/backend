package com.gogidix.aiservices.aiauthenticationservice.shared.exception;

public class AccountLockedException extends AuthenticationException {
    public AccountLockedException(String message) {
        super(message);
    }
}
