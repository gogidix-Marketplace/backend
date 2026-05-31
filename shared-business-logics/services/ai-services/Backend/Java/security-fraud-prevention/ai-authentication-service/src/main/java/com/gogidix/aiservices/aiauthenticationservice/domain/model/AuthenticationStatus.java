package com.gogidix.aiservices.aiauthenticationservice.domain.model;

public enum AuthenticationStatus {
    AUTHENTICATED,
    INVALID_CREDENTIALS,
    MFA_REQUIRED,
    ACCOUNT_LOCKED,
    HIGH_RISK_BLOCKED,
    PENDING,
    FAILED
}
