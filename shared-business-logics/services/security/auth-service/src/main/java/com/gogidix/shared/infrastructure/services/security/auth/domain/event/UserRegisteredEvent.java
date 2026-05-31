package com.gogidix.shared.infrastructure.services.security.auth.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a new user registers.
 */
public class UserRegisteredEvent {

    private final String userId;
    private final String username;
    private final String email;
    private final String tenantId;
    private final Instant occurredAt;

    public UserRegisteredEvent(String userId, String username, String email, String tenantId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.tenantId = tenantId;
        this.occurredAt = Instant.now();
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRegisteredEvent)) return false;
        UserRegisteredEvent that = (UserRegisteredEvent) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
