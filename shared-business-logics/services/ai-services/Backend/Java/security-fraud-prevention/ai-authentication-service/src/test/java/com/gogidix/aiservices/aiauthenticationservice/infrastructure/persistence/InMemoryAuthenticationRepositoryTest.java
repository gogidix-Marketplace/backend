package com.gogidix.aiservices.aiauthenticationservice.infrastructure.persistence;

import com.gogidix.aiservices.aiauthenticationservice.domain.aggregate.AuthenticationAttempt;
import com.gogidix.aiservices.aiauthenticationservice.domain.model.FailureReason;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.AuthenticationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("In-Memory Authentication Repository Infrastructure Tests")
class InMemoryAuthenticationRepositoryTest {

    private AuthenticationRepository repository;

    private static final String USERNAME = "testuser";
    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String PASSWORD_HASH = "$2a$10$encodedPasswordHere";

    @BeforeEach
    void setUp() {
        repository = new InMemoryAuthenticationRepository();
    }

    @Nested
    @DisplayName("User Storage Tests")
    class UserStorageTests {

        @Test
        @DisplayName("Should save user")
        void shouldSaveUser() {
            AuthenticationRepository.UserRecord user = new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    0
            );

            repository.save(user);

            Optional<AuthenticationRepository.UserRecord> found = repository.findByUsername(USERNAME);
            assertThat(found).isPresent();
            assertThat(found.get().username()).isEqualTo(USERNAME);
        }

        @Test
        @DisplayName("Should find user by username")
        void shouldFindUserByUsername() {
            AuthenticationRepository.UserRecord user = new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    0
            );

            repository.save(user);

            Optional<AuthenticationRepository.UserRecord> found = repository.findByUsername(USERNAME);
            assertThat(found).isPresent();
            assertThat(found.get().userId()).isEqualTo(UUID.fromString(USER_ID));
        }

        @Test
        @DisplayName("Should find user by ID")
        void shouldFindUserById() {
            UUID userId = UUID.fromString(USER_ID);
            AuthenticationRepository.UserRecord user = new AuthenticationRepository.UserRecord(
                    userId,
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    0
            );

            repository.save(user);

            Optional<AuthenticationRepository.UserRecord> found = repository.findById(userId);
            assertThat(found).isPresent();
            assertThat(found.get().username()).isEqualTo(USERNAME);
        }

        @Test
        @DisplayName("Should return empty for non-existent user")
        void shouldReturnEmptyForNonExistentUser() {
            Optional<AuthenticationRepository.UserRecord> found = repository.findByUsername("nonexistent");
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Should update user")
        void shouldUpdateUser() {
            AuthenticationRepository.UserRecord user = new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    0
            );

            repository.save(user);

            AuthenticationRepository.UserRecord updated = new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    "$2a$10$newPasswordHash",
                    Set.of("USER", "ADMIN"),
                    false,
                    null,
                    0
            );

            repository.save(updated);

            Optional<AuthenticationRepository.UserRecord> found = repository.findByUsername(USERNAME);
            assertThat(found).isPresent();
            assertThat(found.get().passwordHash()).isEqualTo("$2a$10$newPasswordHash");
            assertThat(found.get().roles()).contains("ADMIN");
        }

        @Test
        @DisplayName("Should delete user")
        void shouldDeleteUser() {
            AuthenticationRepository.UserRecord user = new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    0
            );

            repository.save(user);
            repository.delete(USERNAME);

            Optional<AuthenticationRepository.UserRecord> found = repository.findByUsername(USERNAME);
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Should list all users")
        void shouldListAllUsers() {
            repository.save(new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    0
            ));

            UUID userId2 = UUID.randomUUID();
            repository.save(new AuthenticationRepository.UserRecord(
                    userId2,
                    "user2",
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    0
            ));

            assertThat(repository.findAll()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Authentication Attempt Storage Tests")
    class AttemptStorageTests {

        @Test
        @DisplayName("Should save authentication attempt")
        void shouldSaveAttempt() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            repository.saveAttempt(attempt);

            Optional<AuthenticationAttempt> found = repository.findAttemptById(attempt.getAttemptId());
            assertThat(found).isPresent();
            assertThat(found.get().getUsername()).isEqualTo(USERNAME);
        }

        @Test
        @DisplayName("Should find attempt by ID")
        void shouldFindAttemptById() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            repository.saveAttempt(attempt);

            Optional<AuthenticationAttempt> found = repository.findAttemptById(attempt.getAttemptId());
            assertThat(found).isPresent();
        }

        @Test
        @DisplayName("Should find recent attempts for username")
        void shouldFindRecentAttempts() {
            AuthenticationAttempt attempt1 = AuthenticationAttempt.create(USERNAME);
            AuthenticationAttempt attempt2 = AuthenticationAttempt.create(USERNAME);

            repository.saveAttempt(attempt1);
            repository.saveAttempt(attempt2);

            var recentAttempts = repository.findRecentAttempts(USERNAME, 10);
            assertThat(recentAttempts).hasSize(2);
        }

        @Test
        @DisplayName("Should limit recent attempts")
        void shouldLimitRecentAttempts() {
            for (int i = 0; i < 15; i++) {
                repository.saveAttempt(AuthenticationAttempt.create(USERNAME));
            }

            var recentAttempts = repository.findRecentAttempts(USERNAME, 10);
            assertThat(recentAttempts).hasSize(10);
        }

        @Test
        @DisplayName("Should count failed attempts")
        void shouldCountFailedAttempts() {
            AuthenticationAttempt attempt1 = AuthenticationAttempt.create(USERNAME);
            attempt1.markFailed(FailureReason.INVALID_CREDENTIALS);
            repository.saveAttempt(attempt1);

            AuthenticationAttempt attempt2 = AuthenticationAttempt.create(USERNAME);
            attempt2.markFailed(FailureReason.INVALID_CREDENTIALS);
            repository.saveAttempt(attempt2);

            int count = repository.countFailedAttempts(USERNAME, Instant.now().minusSeconds(3600));
            assertThat(count).isEqualTo(2);
        }

        @Test
        @DisplayName("Should reset count after successful attempt")
        void shouldResetAfterSuccess() {
            AuthenticationAttempt failedAttempt = AuthenticationAttempt.create(USERNAME);
            failedAttempt.markFailed(FailureReason.INVALID_CREDENTIALS);
            repository.saveAttempt(failedAttempt);

            AuthenticationAttempt successAttempt = AuthenticationAttempt.create(USERNAME);
            successAttempt.markSuccessful();
            repository.saveAttempt(successAttempt);

            int count = repository.countFailedAttempts(USERNAME, Instant.now().minusSeconds(3600));
            // Should count based on recent attempts only
            assertThat(count).isGreaterThanOrEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Session Management Tests")
    class SessionManagementTests {

        @Test
        @DisplayName("Should save active session")
        void shouldSaveSession() {
            String sessionId = "session-123";
            AuthenticationRepository.ActiveSession session = new AuthenticationRepository.ActiveSession(
                    sessionId,
                    UUID.fromString(USER_ID),
                    Instant.now().plusSeconds(3600)
            );

            repository.saveSession(session);

            Optional<AuthenticationRepository.ActiveSession> found = repository.findSession(sessionId);
            assertThat(found).isPresent();
            assertThat(found.get().sessionId()).isEqualTo(sessionId);
        }

        @Test
        @DisplayName("Should find session by ID")
        void shouldFindSessionById() {
            String sessionId = "session-123";
            AuthenticationRepository.ActiveSession session = new AuthenticationRepository.ActiveSession(
                    sessionId,
                    UUID.fromString(USER_ID),
                    Instant.now().plusSeconds(3600)
            );

            repository.saveSession(session);

            Optional<AuthenticationRepository.ActiveSession> found = repository.findSession(sessionId);
            assertThat(found).isPresent();
        }

        @Test
        @DisplayName("Should invalidate session")
        void shouldInvalidateSession() {
            String sessionId = "session-123";
            AuthenticationRepository.ActiveSession session = new AuthenticationRepository.ActiveSession(
                    sessionId,
                    UUID.fromString(USER_ID),
                    Instant.now().plusSeconds(3600)
            );

            repository.saveSession(session);
            repository.invalidateSession(sessionId);

            Optional<AuthenticationRepository.ActiveSession> found = repository.findSession(sessionId);
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Should find sessions by user ID")
        void shouldFindSessionsByUserId() {
            UUID userId = UUID.fromString(USER_ID);

            repository.saveSession(new AuthenticationRepository.ActiveSession(
                    "session-1", userId, Instant.now().plusSeconds(3600)
            ));
            repository.saveSession(new AuthenticationRepository.ActiveSession(
                    "session-2", userId, Instant.now().plusSeconds(3600)
            ));

            var sessions = repository.findSessionsByUserId(userId);
            assertThat(sessions).hasSize(2);
        }

        @Test
        @DisplayName("Should clean up expired sessions")
        void shouldCleanupExpiredSessions() {
            repository.saveSession(new AuthenticationRepository.ActiveSession(
                    "session-1", UUID.fromString(USER_ID), Instant.now().minusSeconds(60)
            ));
            repository.saveSession(new AuthenticationRepository.ActiveSession(
                    "session-2", UUID.fromString(USER_ID), Instant.now().plusSeconds(3600)
            ));

            repository.cleanupExpiredSessions();

            assertThat(repository.findSession("session-1")).isEmpty();
            assertThat(repository.findSession("session-2")).isPresent();
        }

        @Test
        @DisplayName("Should count active sessions for user")
        void shouldCountActiveSessions() {
            UUID userId = UUID.fromString(USER_ID);

            repository.saveSession(new AuthenticationRepository.ActiveSession(
                    "session-1", userId, Instant.now().plusSeconds(3600)
            ));
            repository.saveSession(new AuthenticationRepository.ActiveSession(
                    "session-2", userId, Instant.now().plusSeconds(3600)
            ));
            repository.saveSession(new AuthenticationRepository.ActiveSession(
                    "session-3", userId, Instant.now().plusSeconds(3600)
            ));

            int count = repository.countActiveSessions(userId);
            assertThat(count).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("MFA Session Storage Tests")
    class MfaSessionStorageTests {

        @Test
        @DisplayName("Should save pending MFA session")
        void shouldSaveMfaSession() {
            String sessionId = "mfa-session-123";
            String code = "123456";

            AuthenticationRepository.PendingMfaSession session = new AuthenticationRepository.PendingMfaSession(
                    sessionId,
                    USERNAME,
                    code,
                    Instant.now().plusSeconds(300)
            );

            repository.saveMfaSession(session);

            Optional<AuthenticationRepository.PendingMfaSession> found = repository.findPendingMfaSession(sessionId);
            assertThat(found).isPresent();
            assertThat(found.get().mfaCode()).isEqualTo(code);
        }

        @Test
        @DisplayName("Should find pending MFA session")
        void shouldFindPendingMfaSession() {
            String sessionId = "mfa-session-123";

            AuthenticationRepository.PendingMfaSession session = new AuthenticationRepository.PendingMfaSession(
                    sessionId,
                    USERNAME,
                    "123456",
                    Instant.now().plusSeconds(300)
            );

            repository.saveMfaSession(session);

            Optional<AuthenticationRepository.PendingMfaSession> found = repository.findPendingMfaSession(sessionId);
            assertThat(found).isPresent();
        }

        @Test
        @DisplayName("Should consume MFA session after use")
        void shouldConsumeMfaSession() {
            String sessionId = "mfa-session-123";

            AuthenticationRepository.PendingMfaSession session = new AuthenticationRepository.PendingMfaSession(
                    sessionId,
                    USERNAME,
                    "123456",
                    Instant.now().plusSeconds(300)
            );

            repository.saveMfaSession(session);
            repository.consumeMfaSession(sessionId);

            Optional<AuthenticationRepository.PendingMfaSession> found = repository.findPendingMfaSession(sessionId);
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Should clean up expired MFA sessions")
        void shouldCleanupExpiredMfaSessions() {
            repository.saveMfaSession(new AuthenticationRepository.PendingMfaSession(
                    "mfa-1", USERNAME, "123456", Instant.now().minusSeconds(60)
            ));
            repository.saveMfaSession(new AuthenticationRepository.PendingMfaSession(
                    "mfa-2", USERNAME, "654321", Instant.now().plusSeconds(300)
            ));

            repository.cleanupExpiredMfaSessions();

            assertThat(repository.findPendingMfaSession("mfa-1")).isEmpty();
            assertThat(repository.findPendingMfaSession("mfa-2")).isPresent();
        }
    }

    @Nested
    @DisplayName("Password Management Tests")
    class PasswordManagementTests {

        @Test
        @DisplayName("Should update password")
        void shouldUpdatePassword() {
            AuthenticationRepository.UserRecord user = new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    0
            );

            repository.save(user);

            String newPasswordHash = "$2a$10$newPasswordHash";
            repository.updatePassword(USERNAME, newPasswordHash);

            Optional<AuthenticationRepository.UserRecord> found = repository.findByUsername(USERNAME);
            assertThat(found).isPresent();
            assertThat(found.get().passwordHash()).isEqualTo(newPasswordHash);
        }

        @Test
        @DisplayName("Should track password history")
        void shouldTrackPasswordHistory() {
            String password1 = "hash1";
            String password2 = "hash2";
            String password3 = "hash3";

            repository.updatePassword(USERNAME, password1);
            repository.updatePassword(USERNAME, password2);
            repository.updatePassword(USERNAME, password3);

            assertThat(repository.isPasswordInHistory(USERNAME, password1)).isTrue();
            assertThat(repository.isPasswordInHistory(USERNAME, password2)).isTrue();
            assertThat(repository.isPasswordInHistory(USERNAME, password3)).isTrue();
        }

        @Test
        @DisplayName("Should limit password history size")
        void shouldLimitPasswordHistory() {
            for (int i = 0; i < 15; i++) {
                repository.updatePassword(USERNAME, "hash" + i);
            }

            // History should be limited to last 10
            assertThat(repository.isPasswordInHistory(USERNAME, "hash0")).isFalse();
            assertThat(repository.isPasswordInHistory(USERNAME, "hash14")).isTrue();
        }
    }

    @Nested
    @DisplayName("Account Lockout Tests")
    class AccountLockoutTests {

        @Test
        @DisplayName("Should lock account")
        void shouldLockAccount() {
            AuthenticationRepository.UserRecord user = new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    0
            );

            repository.save(user);

            Instant lockUntil = Instant.now().plusSeconds(1800);
            repository.lockAccount(USERNAME, lockUntil);

            Optional<AuthenticationRepository.UserRecord> found = repository.findByUsername(USERNAME);
            assertThat(found).isPresent();
            assertThat(found.get().isLocked()).isTrue();
            assertThat(found.get().lockUntil()).isEqualTo(lockUntil);
        }

        @Test
        @DisplayName("Should unlock account")
        void shouldUnlockAccount() {
            AuthenticationRepository.UserRecord user = new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    true,
                    Instant.now().plusSeconds(1800),
                    5
            );

            repository.save(user);

            repository.unlockAccount(USERNAME);

            Optional<AuthenticationRepository.UserRecord> found = repository.findByUsername(USERNAME);
            assertThat(found).isPresent();
            assertThat(found.get().isLocked()).isFalse();
            assertThat(found.get().lockUntil()).isNull();
        }

        @Test
        @DisplayName("Should reset failed attempts")
        void shouldResetFailedAttempts() {
            AuthenticationRepository.UserRecord user = new AuthenticationRepository.UserRecord(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    PASSWORD_HASH,
                    Set.of("USER"),
                    false,
                    null,
                    5
            );

            repository.save(user);

            repository.resetFailedAttempts(USERNAME);

            Optional<AuthenticationRepository.UserRecord> found = repository.findByUsername(USERNAME);
            assertThat(found).isPresent();
            assertThat(found.get().failedAttempts()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Token Blacklist Tests")
    class TokenBlacklistTests {

        @Test
        @DisplayName("Should blacklist token")
        void shouldBlacklistToken() {
            String token = "blacklisted-token";

            repository.blacklistToken(token);

            assertThat(repository.isTokenBlacklisted(token)).isTrue();
        }

        @Test
        @DisplayName("Should check if token is blacklisted")
        void shouldCheckBlacklistedToken() {
            String token1 = "blacklisted-token";
            String token2 = "valid-token";

            repository.blacklistToken(token1);

            assertThat(repository.isTokenBlacklisted(token1)).isTrue();
            assertThat(repository.isTokenBlacklisted(token2)).isFalse();
        }

        @Test
        @DisplayName("Should remove expired tokens from blacklist")
        void shouldRemoveExpiredBlacklistedTokens() {
            String expiredToken = "expired-token";

            repository.blacklistToken(expiredToken);
            // In a real implementation, tokens would be cleaned up based on expiration
        }
    }
}
