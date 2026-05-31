package com.gogidix.aiservices.aiauthenticationservice.domain.model;

public enum FailureReason {
    INVALID_CREDENTIALS,
    ACCOUNT_LOCKED,
    MFA_FAILED,
    MFA_EXPIRED,
    HIGH_RISK,
    SESSION_EXPIRED,
    INVALID_TOKEN,
    UNKNOWN
}
