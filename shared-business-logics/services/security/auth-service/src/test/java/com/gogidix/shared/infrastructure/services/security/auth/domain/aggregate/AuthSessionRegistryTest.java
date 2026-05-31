package com.gogidix.shared.infrastructure.services.security.auth.domain.aggregate;

import com.gogidix.shared.infrastructure.services.security.auth.domain.event.UserLoggedInEvent;
import com.gogidix.shared.infrastructure.services.security.auth.domain.event.UserLoggedOutEvent;
import com.gogidix.shared.infrastructure.services.security.auth.domain.event.UserRegisteredEvent;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuthSessionRegistry Tests")
class AuthSessionRegistryTest {

    private AuthSessionRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new AuthSessionRegistry();
    }

    @Nested
    @DisplayName("User Registration")
    class RegistrationTests {

        @Test
        void shouldRegisterUser() {
            User user = registry.registerUser("user-1", "tenant-1", "john", "john@test.com", "hash123");

            assertNotNull(user);
            assertEquals("user-1", user.getId());
            assertEquals("john", user.getUsername());
            assertEquals("john@test.com", user.getEmail());
        }

        @Test
        void shouldPublishRegisteredEvent() {
            String[] capturedUserId = {null};
            registry.onUserRegistered(e -> capturedUserId[0] = e.getUserId());

            registry.registerUser("user-2", "tenant-1", "jane", "jane@test.com", "hash456");

            assertEquals("user-2", capturedUserId[0]);
        }

        @Test
        void shouldGetRegisteredUser() {
            registry.registerUser("user-1", "tenant-1", "john", "john@test.com", "hash123");

            Optional<User> found = registry.getUser("user-1");
            assertTrue(found.isPresent());
            assertEquals("john", found.get().getUsername());
        }

        @Test
        void shouldReturnEmptyForUnknownUser() {
            Optional<User> found = registry.getUser("unknown");
            assertTrue(found.isEmpty());
        }

        @Test
        void shouldFindUserByUsername() {
            registry.registerUser("user-1", "tenant-1", "john", "john@test.com", "hash123");

            User found = registry.findUserByUsername("john");
            assertNotNull(found);
            assertEquals("user-1", found.getId());
        }

        @Test
        void shouldReturnNullForUnknownUsername() {
            User found = registry.findUserByUsername("unknown");
            assertNull(found);
        }

        @Test
        void shouldFindUserByEmail() {
            registry.registerUser("user-1", "tenant-1", "john", "john@test.com", "hash123");

            User found = registry.findUserByEmail("john@test.com");
            assertNotNull(found);
            assertEquals("john", found.getUsername());
        }

        @Test
        void shouldReturnNullForUnknownEmail() {
            User found = registry.findUserByEmail("unknown@test.com");
            assertNull(found);
        }
    }

    @Nested
    @DisplayName("Authentication")
    class AuthTests {

        @BeforeEach
        void registerUser() {
            registry.registerUser("user-1", "tenant-1", "john", "john@test.com", "hash123");
        }

        @Test
        void shouldAuthenticateWithValidCredentials() {
            Optional<AuthToken> result = registry.authenticate("john", "hash123", "192.168.1.1");

            assertTrue(result.isPresent());
            assertEquals("user-1", result.get().getUserId());
            assertNotNull(result.get().getAccessToken());
            assertNotNull(result.get().getRefreshToken());
        }

        @Test
        void shouldFailForUnknownUser() {
            Optional<AuthToken> result = registry.authenticate("unknown", "hash123", "10.0.0.1");
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldFailForWrongPassword() {
            Optional<AuthToken> result = registry.authenticate("john", "wronghash", "10.0.0.1");
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldPublishLoggedInEvent() {
            String[] capturedUserId = {null};
            registry.onUserLoggedIn(e -> capturedUserId[0] = e.getUserId());

            registry.authenticate("john", "hash123", "192.168.1.1");

            assertEquals("user-1", capturedUserId[0]);
        }
    }

    @Nested
    @DisplayName("Token Management")
    class TokenTests {

        private AuthToken token;

        @BeforeEach
        void setUp() {
            registry.registerUser("user-1", "tenant-1", "john", "john@test.com", "hash123");
            token = registry.authenticate("john", "hash123", "10.0.0.1").orElseThrow();
            token.setExpiresAt(LocalDateTime.now().plusHours(1));
            token.setRefreshExpiresAt(LocalDateTime.now().plusDays(7));
        }

        @Test
        void shouldValidateValidToken() {
            assertTrue(registry.validateToken(token.getTokenId()));
        }

        @Test
        void shouldInvalidateUnknownToken() {
            assertFalse(registry.validateToken("unknown-token"));
        }

        @Test
        void shouldRefreshToken() {
            Optional<AuthToken> refreshed = registry.refreshToken(token.getRefreshToken());
            assertTrue(refreshed.isPresent());
            assertNotEquals(token.getTokenId(), refreshed.get().getTokenId());
        }

        @Test
        void shouldFailRefreshForUnknownToken() {
            Optional<AuthToken> refreshed = registry.refreshToken("unknown-refresh");
            assertTrue(refreshed.isEmpty());
        }

        @Test
        void shouldLogout() {
            boolean result = registry.logout(token.getTokenId(), "10.0.0.1");
            assertTrue(result);
            assertFalse(registry.validateToken(token.getTokenId()));
        }

        @Test
        void shouldFailLogoutForUnknownToken() {
            boolean result = registry.logout("unknown", "10.0.0.1");
            assertFalse(result);
        }

        @Test
        void shouldPublishLoggedOutEventOnLogout() {
            String[] capturedUserId = {null};
            registry.onUserLoggedOut(e -> capturedUserId[0] = e.getUserId());

            registry.logout(token.getTokenId(), "10.0.0.1");

            assertEquals("user-1", capturedUserId[0]);
        }

        @Test
        void shouldLogoutAllSessions() {
            registry.logoutAll("user-1", "10.0.0.1");
            assertFalse(registry.validateToken(token.getTokenId()));
        }

        @Test
        void shouldGetUserTokens() {
            List<AuthToken> tokens = registry.getUserTokens("user-1");
            assertEquals(1, tokens.size());
            assertEquals(token.getTokenId(), tokens.get(0).getTokenId());
        }

        @Test
        void shouldReturnEmptyTokensForUnknownUser() {
            List<AuthToken> tokens = registry.getUserTokens("unknown");
            assertTrue(tokens.isEmpty());
        }
    }

    @Nested
    @DisplayName("Query Operations")
    class QueryTests {

        @BeforeEach
        void registerUsers() {
            registry.registerUser("u1", "tenant-1", "alice", "alice@test.com", "h1");
            registry.registerUser("u2", "tenant-1", "bob", "bob@test.com", "h2");
            registry.registerUser("u3", "tenant-2", "charlie", "charlie@test.com", "h3");
        }

        @Test
        void shouldGetAllUsers() {
            List<User> users = registry.getAllUsers();
            assertEquals(3, users.size());
        }

        @Test
        void shouldGetUsersByTenant() {
            List<User> t1Users = registry.getUsersByTenant("tenant-1");
            assertEquals(2, t1Users.size());

            List<User> t2Users = registry.getUsersByTenant("tenant-2");
            assertEquals(1, t2Users.size());
        }

        @Test
        void shouldReturnEmptyForUnknownTenant() {
            List<User> users = registry.getUsersByTenant("unknown");
            assertTrue(users.isEmpty());
        }
    }

    @Nested
    @DisplayName("Statistics")
    class StatsTests {

        @Test
        void shouldReturnEmptyStats() {
            AuthSessionRegistry.SessionStats stats = registry.getStats();
            assertEquals(0, stats.totalUsers());
            assertEquals(0, stats.totalTokens());
            assertEquals(0, stats.activeTokens());
            assertEquals(0, stats.revokedTokens());
        }

        @Test
        void shouldCountStatsAfterOperations() {
            registry.registerUser("u1", "t1", "alice", "a@t.com", "h1");
            Optional<AuthToken> opt = registry.authenticate("alice", "h1", "10.0.0.1");
            assertTrue(opt.isPresent());
            opt.get().setExpiresAt(LocalDateTime.now().plusHours(1));

            AuthSessionRegistry.SessionStats stats = registry.getStats();
            assertEquals(1, stats.totalUsers());
            assertEquals(1, stats.totalTokens());
            assertEquals(1, stats.activeTokens());
            assertEquals(0, stats.revokedTokens());
        }
    }
}
