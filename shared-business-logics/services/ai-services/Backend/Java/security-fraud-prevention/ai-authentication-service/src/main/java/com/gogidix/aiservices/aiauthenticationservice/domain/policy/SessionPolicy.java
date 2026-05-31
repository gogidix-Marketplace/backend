package com.gogidix.aiservices.aiauthenticationservice.domain.policy;

import com.gogidix.aiservices.aiauthenticationservice.domain.model.AuthenticationResult;

import java.time.Instant;
import java.util.Set;

public class SessionPolicy {
    private static final int DEFAULT_TIMEOUT_SECONDS = 3600; // 1 hour
    private static final int REMEMBER_ME_TIMEOUT_SECONDS = 2592000; // 30 days
    private static final int ADMIN_TIMEOUT_SECONDS = 1800; // 30 minutes
    private static final int MAX_CONCURRENT_SESSIONS = 3;

    public int getDefaultTimeout() {
        return DEFAULT_TIMEOUT_SECONDS;
    }

    public int getTimeoutForUser(Set<String> roles) {
        if (roles != null && roles.contains("ADMIN")) {
            return ADMIN_TIMEOUT_SECONDS;
        }
        return DEFAULT_TIMEOUT_SECONDS;
    }

    public int getRememberMeTimeout() {
        return REMEMBER_ME_TIMEOUT_SECONDS;
    }

    public boolean isSessionValid(AuthenticationResult result) {
        return result.getSessionExpiresAt() != null &&
               result.getSessionExpiresAt().isAfter(Instant.now());
    }

    public boolean allowNewSession(int currentSessionCount) {
        return currentSessionCount < MAX_CONCURRENT_SESSIONS;
    }

    public int getMaxConcurrentSessions() {
        return MAX_CONCURRENT_SESSIONS;
    }
}
