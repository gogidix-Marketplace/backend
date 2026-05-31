package com.gogidix.shared.infrastructure.services.security.auth.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a user logs out.
 */
public class UserLoggedOutEvent {

    private final String userId;
    private final String ipAddress;
    private final Instant occurredAt;

    public UserLoggedOutEvent(String userId, String ipAddress) {
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.occurredAt = Instant.now();
    }

    public String getUserId() {
        return userId;
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
        if (!(o instanceof UserLoggedOutEvent)) return false;
        UserLoggedOutEvent that = (UserLoggedOutEvent) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
