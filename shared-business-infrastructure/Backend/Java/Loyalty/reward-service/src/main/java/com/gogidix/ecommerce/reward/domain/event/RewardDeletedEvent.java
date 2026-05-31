package com.gogidix.ecommerce.reward.domain.event;

import java.time.Instant;

public record RewardDeletedEvent(
    String eventId,
    String tenantId,
    String entityId,
    Instant timestamp
) implements RewardDomainEvent {
    public RewardDeletedEvent(String tenantId, String entityId) {
        this(java.util.UUID.randomUUID().toString(), tenantId, entityId, Instant.now());
    }
    public String eventType() { return "Reward_DELETED"; }
}
