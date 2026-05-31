package com.gogidix.ecommerce.reward.domain.event;

import java.time.Instant;

public record RewardCreatedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements RewardDomainEvent {
    public RewardCreatedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Reward_CREATED"; }
}
