package com.gogidix.aiservices.aiauthenticationservice.domain.policy;

import com.gogidix.aiservices.aiauthenticationservice.domain.aggregate.AuthenticationAttempt;

import java.time.Instant;

public class AccountLockoutPolicy {
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int DEFAULT_LOCKOUT_MINUTES = 30;

    public boolean isLockedOut(AuthenticationAttempt attempt) {
        return attempt.getFailedAttemptCount() >= MAX_FAILED_ATTEMPTS ||
               (attempt.getLockoutUntil() != null && attempt.getLockoutUntil().isAfter(Instant.now()));
    }

    public Instant getLockoutUntil(AuthenticationAttempt attempt) {
        if (attempt.getLockoutUntil() != null) {
            return attempt.getLockoutUntil();
        }
        if (attempt.getFailedAttemptCount() >= MAX_FAILED_ATTEMPTS) {
            return Instant.now().plusSeconds(DEFAULT_LOCKOUT_MINUTES * 60);
        }
        return null;
    }

    public Instant calculateLockoutDuration(int lockoutCount) {
        int minutes = switch (lockoutCount) {
            case 1 -> 30;
            case 2 -> 60;
            case 3 -> 120;
            default -> 180;
        };
        return Instant.now().plusSeconds(minutes * 60);
    }
}
