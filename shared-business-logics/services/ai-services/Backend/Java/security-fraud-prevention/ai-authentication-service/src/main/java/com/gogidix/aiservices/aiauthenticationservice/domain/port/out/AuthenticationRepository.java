package com.gogidix.aiservices.aiauthenticationservice.domain.port.out;

import com.gogidix.aiservices.aiauthenticationservice.domain.aggregate.AuthenticationAttempt;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AuthenticationRepository {

    record UserRecord(
            UUID userId,
            String username,
            String passwordHash,
            Set<String> roles,
            boolean isLocked,
            Instant lockUntil,
            int failedAttempts
    ) {}

    record ActiveSession(
            String sessionId,
            UUID userId,
            Instant expiresAt
    ) {}

    record PendingMfaSession(
            String sessionId,
            String username,
            String mfaCode,
            Instant expiresAt
    ) {}

    // User operations
    void save(UserRecord user);
    Optional<UserRecord> findByUsername(String username);
    Optional<UserRecord> findById(UUID userId);
    List<UserRecord> findAll();
    void delete(String username);
    void updatePassword(String username, String passwordHash);

    // Attempt operations
    void saveAttempt(AuthenticationAttempt attempt);
    Optional<AuthenticationAttempt> findAttemptById(UUID attemptId);
    List<AuthenticationAttempt> findRecentAttempts(String username, int limit);
    int countFailedAttempts(String username, Instant since);

    // Session operations
    void saveSession(ActiveSession session);
    Optional<ActiveSession> findSession(String sessionId);
    void invalidateSession(String sessionId);
    List<ActiveSession> findSessionsByUserId(UUID userId);
    void cleanupExpiredSessions();
    int countActiveSessions(UUID userId);

    // MFA operations
    void saveMfaSession(PendingMfaSession session);
    Optional<PendingMfaSession> findPendingMfaSession(String sessionId);
    void consumeMfaSession(String sessionId);
    void cleanupExpiredMfaSessions();

    // Lockout operations
    void lockAccount(String username, Instant lockUntil);
    void unlockAccount(String username);
    void resetFailedAttempts(String username);

    // Password history
    boolean isPasswordInHistory(String username, String passwordHash);

    // Token blacklist
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
}
