package com.gogidix.shared.infrastructure.services.billing.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Domain event published when a subscription is updated.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionUpdatedEvent {
    private String subscriptionId;
    private String tenantId;
    private String changeType; // PLAN_CHANGE, STATUS_CHANGE, CANCELLATION, etc.
    String oldValue;
    String newValue;
    private Instant occurredAt;
}
