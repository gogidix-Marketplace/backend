package com.gogidix.shared.infrastructure.services.security.auth.domain.aggregate;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.auth.domain.event.UserLoggedInEvent;
import com.gogidix.shared.infrastructure.services.security.auth.domain.event.UserLoggedOutEvent;
import com.gogidix.shared.infrastructure.services.security.auth.domain.event.UserRegisteredEvent;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Aggregate root for authentication session management.
 * Manages user authentication sessions and tokens.
 */
public class AuthSessionRegistry {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, AuthToken> tokens = new ConcurrentHashMap<>();
    private final Map<String, List<String>> userTokens = new ConcurrentHashMap<>();

    private final List<Consumer<UserLoggedInEvent>> loggedInEventHandlers = new ArrayList<>();
    private final List<Consumer<UserLoggedOutEvent>> loggedOutEventHandlers = new ArrayList<>();
    private final List<Consumer<UserRegisteredEvent>> registeredEventHandlers = new ArrayList<>();

    /**
     * Register a new user.
     */
    public User registerUser(String userId, String tenantId, String username, String email, String passwordHash) {
        User user = User.builder()
                .id(userId)
                .tenantId(TenantId.of(tenantId))
                .username(username)
                .email(email)
                .password(passwordHash)
                .enabled(true)
                .build();

        users.put(userId, user);

        publishEvent(new UserRegisteredEvent(userId, username, email, tenantId));

        return user;
    }

    /**
     * Authenticate a user and create a token.
     */
    public Optional<AuthToken> authenticate(String username, String passwordHash, String ipAddress) {
        User user = findUserByUsername(username);

        if (user == null) {
            return Optional.empty();
        }

        if (!user.isEnabled()) {
            return Optional.empty();
        }

        if (!user.getPassword().equals(passwordHash)) {
            // Increment failed login attempts
            return Optional.empty();
        }

        // Create token
        AuthToken token = AuthToken.builder()
                .tokenId(generateTokenId())
                .userId(user.getId())
                .tenantId(user.getTenantId().toString())
                .accessToken(generateAccessToken(user))
                .refreshToken(generateRefreshToken(user))
                .build();

        tokens.put(token.getTokenId(), token);
        userTokens.computeIfAbsent(user.getId(), k -> new ArrayList<>()).add(token.getTokenId());

        publishEvent(new UserLoggedInEvent(user.getId(), username, ipAddress));

        return Optional.of(token);
    }

    /**
     * Validate a token.
     */
    public boolean validateToken(String tokenId) {
        AuthToken token = tokens.get(tokenId);
        return token != null && token.isValid();
    }

    /**
     * Refresh a token.
     */
    public Optional<AuthToken> refreshToken(String refreshToken) {
        AuthToken existingToken = tokens.values().stream()
                .filter(t -> refreshToken.equals(t.getRefreshToken()))
                .filter(AuthToken::isRefreshable)
                .findFirst()
                .orElse(null);

        if (existingToken == null) {
            return Optional.empty();
        }

        // Revoke old token
        existingToken.revoke();

        // Create new token
        User user = users.get(existingToken.getUserId());
        if (user == null) {
            return Optional.empty();
        }

        AuthToken newToken = AuthToken.builder()
                .tokenId(generateTokenId())
                .userId(user.getId())
                .tenantId(user.getTenantId().toString())
                .accessToken(generateAccessToken(user))
                .refreshToken(generateRefreshToken(user))
                .build();

        tokens.put(newToken.getTokenId(), newToken);
        userTokens.get(user.getId()).remove(existingToken.getTokenId());
        userTokens.get(user.getId()).add(newToken.getTokenId());

        return Optional.of(newToken);
    }

    /**
     * Logout user and revoke token.
     */
    public boolean logout(String tokenId, String ipAddress) {
        AuthToken token = tokens.get(tokenId);
        if (token == null) {
            return false;
        }

        token.revoke();

        publishEvent(new UserLoggedOutEvent(token.getUserId(), ipAddress));

        return true;
    }

    /**
     * Logout all sessions for a user.
     */
    public void logoutAll(String userId, String ipAddress) {
        List<String> tokenIds = userTokens.get(userId);
        if (tokenIds != null) {
            tokenIds.forEach(tid -> {
                AuthToken token = tokens.get(tid);
                if (token != null) {
                    token.revoke();
                }
            });
            userTokens.remove(userId);
        }

        publishEvent(new UserLoggedOutEvent(userId, ipAddress));
    }

    /**
     * Get user by ID.
     */
    public Optional<User> getUser(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    /**
     * Find user by username.
     */
    public User findUserByUsername(String username) {
        return users.values().stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find user by email.
     */
    public User findUserByEmail(String email) {
        return users.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all users.
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Get users by tenant.
     */
    public List<User> getUsersByTenant(String tenantId) {
        return users.values().stream()
                .filter(u -> tenantId.equals(u.getTenantId().toString()))
                .collect(Collectors.toList());
    }

    /**
     * Get tokens for a user.
     */
    public List<AuthToken> getUserTokens(String userId) {
        List<String> tokenIds = userTokens.get(userId);
        if (tokenIds == null) {
            return List.of();
        }
        return tokenIds.stream()
                .map(tokens::get)
                .filter(t -> t != null)
                .collect(Collectors.toList());
    }

    /**
     * Get session statistics.
     */
    public SessionStats getStats() {
        int totalUsers = users.size();
        int totalTokens = tokens.size();
        long activeTokens = tokens.values().stream().filter(AuthToken::isValid).count();
        long revokedTokens = tokens.values().stream().filter(AuthToken::isRevoked).count();

        return new SessionStats(totalUsers, totalTokens, activeTokens, revokedTokens);
    }

    private String generateTokenId() {
        return java.util.UUID.randomUUID().toString();
    }

    private String generateAccessToken(User user) {
        // This would be implemented by JWT provider
        return "jwt_" + generateTokenId();
    }

    private String generateRefreshToken(User user) {
        // This would be implemented by JWT provider
        return "refresh_" + generateTokenId();
    }

    private void publishEvent(UserLoggedInEvent event) {
        loggedInEventHandlers.forEach(h -> h.accept(event));
    }

    private void publishEvent(UserLoggedOutEvent event) {
        loggedOutEventHandlers.forEach(h -> h.accept(event));
    }

    private void publishEvent(UserRegisteredEvent event) {
        registeredEventHandlers.forEach(h -> h.accept(event));
    }

    public void onUserLoggedIn(Consumer<UserLoggedInEvent> handler) {
        loggedInEventHandlers.add(handler);
    }

    public void onUserLoggedOut(Consumer<UserLoggedOutEvent> handler) {
        loggedOutEventHandlers.add(handler);
    }

    public void onUserRegistered(Consumer<UserRegisteredEvent> handler) {
        registeredEventHandlers.add(handler);
    }

    /**
     * Value object for session statistics.
     */
    public record SessionStats(int totalUsers, int totalTokens, long activeTokens, long revokedTokens) {}
}
