package com.gogidix.aiservices.aiauthenticationservice.infrastructure.persistence;

import com.gogidix.aiservices.aiauthenticationservice.domain.aggregate.AuthenticationAttempt;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.AuthenticationRepository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryAuthenticationRepository implements AuthenticationRepository {
    private final Map<String, UserRecord> usersByUsername = new ConcurrentHashMap<>();
    private final Map<UUID, UserRecord> usersById = new ConcurrentHashMap<>();
    private final Map<UUID, AuthenticationAttempt> attempts = new ConcurrentHashMap<>();
    private final Map<String, ActiveSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, PendingMfaSession> mfaSessions = new ConcurrentHashMap<>();
    private final Map<String, String> passwordHistory = new ConcurrentHashMap<>();
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    private static final int MAX_PASSWORD_HISTORY = 10;

    @Override
    public void save(UserRecord user) {
        usersByUsername.put(user.username(), user);
        usersById.put(user.userId(), user);
    }

    @Override
    public Optional<UserRecord> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    @Override
    public Optional<UserRecord> findById(UUID userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    @Override
    public List<UserRecord> findAll() {
        return new ArrayList<>(usersByUsername.values());
    }

    @Override
    public void delete(String username) {
        UserRecord user = usersByUsername.remove(username);
        if (user != null) {
            usersById.remove(user.userId());
        }
    }

    @Override
    public void updatePassword(String username, String passwordHash) {
        UserRecord existing = usersByUsername.get(username);
        if (existing != null) {
            UserRecord updated = new UserRecord(
                    existing.userId(),
                    existing.username(),
                    passwordHash,
                    existing.roles(),
                    existing.isLocked(),
                    existing.lockUntil(),
                    existing.failedAttempts()
            );
            usersByUsername.put(username, updated);
            usersById.put(existing.userId(), updated);

            // Track password history
            List<String> history = new ArrayList<>();
            for (int i = 0; i < MAX_PASSWORD_HISTORY; i++) {
                String hist = passwordHistory.get(username + "_" + i);
                if (hist != null) {
                    history.add(hist);
                }
            }
            history.add(passwordHash);
            for (int i = 0; i < history.size(); i++) {
                passwordHistory.put(username + "_" + i, history.get(i));
            }
        }
    }

    @Override
    public void saveAttempt(AuthenticationAttempt attempt) {
        attempts.put(attempt.getAttemptId(), attempt);
    }

    @Override
    public Optional<AuthenticationAttempt> findAttemptById(UUID attemptId) {
        return Optional.ofNullable(attempts.get(attemptId));
    }

    @Override
    public List<AuthenticationAttempt> findRecentAttempts(String username, int limit) {
        return attempts.values().stream()
                .filter(a -> username.equals(a.getUsername()))
                .sorted((a1, a2) -> a2.getTimestamp().compareTo(a1.getTimestamp()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countFailedAttempts(String username, Instant since) {
        return (int) attempts.values().stream()
                .filter(a -> username.equals(a.getUsername()))
                .filter(a -> a.getTimestamp().isAfter(since))
                .filter(a -> a.getStatus() == com.gogidix.aiservices.aiauthenticationservice.domain.model.AttemptStatus.FAILED)
                .count();
    }

    @Override
    public void saveSession(ActiveSession session) {
        sessions.put(session.sessionId(), session);
    }

    @Override
    public Optional<ActiveSession> findSession(String sessionId) {
        ActiveSession session = sessions.get(sessionId);
        if (session != null && session.expiresAt().isBefore(Instant.now())) {
            sessions.remove(sessionId);
            return Optional.empty();
        }
        return Optional.ofNullable(session);
    }

    @Override
    public void invalidateSession(String sessionId) {
        sessions.remove(sessionId);
    }

    @Override
    public List<ActiveSession> findSessionsByUserId(UUID userId) {
        return sessions.values().stream()
                .filter(s -> s.userId().equals(userId))
                .filter(s -> s.expiresAt().isAfter(Instant.now()))
                .collect(Collectors.toList());
    }

    @Override
    public void cleanupExpiredSessions() {
        sessions.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(Instant.now()));
    }

    @Override
    public int countActiveSessions(UUID userId) {
        return (int) sessions.values().stream()
                .filter(s -> s.userId().equals(userId))
                .filter(s -> s.expiresAt().isAfter(Instant.now()))
                .count();
    }

    @Override
    public void saveMfaSession(PendingMfaSession session) {
        mfaSessions.put(session.sessionId(), session);
    }

    @Override
    public Optional<PendingMfaSession> findPendingMfaSession(String sessionId) {
        PendingMfaSession session = mfaSessions.get(sessionId);
        if (session != null && session.expiresAt().isBefore(Instant.now())) {
            mfaSessions.remove(sessionId);
            return Optional.empty();
        }
        return Optional.ofNullable(session);
    }

    @Override
    public void consumeMfaSession(String sessionId) {
        mfaSessions.remove(sessionId);
    }

    @Override
    public void cleanupExpiredMfaSessions() {
        mfaSessions.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(Instant.now()));
    }

    @Override
    public void lockAccount(String username, Instant lockUntil) {
        UserRecord existing = usersByUsername.get(username);
        if (existing != null) {
            UserRecord updated = new UserRecord(
                    existing.userId(),
                    existing.username(),
                    existing.passwordHash(),
                    existing.roles(),
                    true,
                    lockUntil,
                    existing.failedAttempts()
            );
            usersByUsername.put(username, updated);
            usersById.put(existing.userId(), updated);
        }
    }

    @Override
    public void unlockAccount(String username) {
        UserRecord existing = usersByUsername.get(username);
        if (existing != null) {
            UserRecord updated = new UserRecord(
                    existing.userId(),
                    existing.username(),
                    existing.passwordHash(),
                    existing.roles(),
                    false,
                    null,
                    0
            );
            usersByUsername.put(username, updated);
            usersById.put(existing.userId(), updated);
        }
    }

    @Override
    public void resetFailedAttempts(String username) {
        UserRecord existing = usersByUsername.get(username);
        if (existing != null) {
            UserRecord updated = new UserRecord(
                    existing.userId(),
                    existing.username(),
                    existing.passwordHash(),
                    existing.roles(),
                    existing.isLocked(),
                    existing.lockUntil(),
                    0
            );
            usersByUsername.put(username, updated);
            usersById.put(existing.userId(), updated);
        }
    }

    @Override
    public boolean isPasswordInHistory(String username, String passwordHash) {
        for (int i = 0; i < MAX_PASSWORD_HISTORY; i++) {
            if (passwordHash.equals(passwordHistory.get(username + "_" + i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
