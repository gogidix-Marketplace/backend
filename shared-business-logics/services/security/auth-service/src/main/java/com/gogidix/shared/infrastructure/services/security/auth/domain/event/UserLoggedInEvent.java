package com.gogidix.shared.infrastructure.services.security.auth.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a user logs in.
 */
public class UserLoggedInEvent {

    private final String userId;
    private final String username;
    private final String ipAddress;
    private final Instant occurredAt;

    public UserLoggedInEvent(String userId, String username, String ipAddress) {
        this.userId = userId;
        this.username = username;
        this.ipAddress = ipAddress;
        this.occurredAt = Instant.now();
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLoggedInEvent)) return false;
        UserLoggedInEvent that = (UserLoggedInEvent) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
