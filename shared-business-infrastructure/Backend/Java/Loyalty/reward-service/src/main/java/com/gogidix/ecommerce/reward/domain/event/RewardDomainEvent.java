package com.gogidix.ecommerce.reward.domain.event;

import java.time.Instant;

public interface RewardDomainEvent {
    String eventId();
    String tenantId();
    String eventType();
    Instant timestamp();
}
