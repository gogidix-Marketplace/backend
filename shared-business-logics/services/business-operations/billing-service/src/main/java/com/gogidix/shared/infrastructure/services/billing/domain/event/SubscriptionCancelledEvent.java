package com.gogidix.shared.infrastructure.services.billing.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Domain event published when a subscription is cancelled.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCancelledEvent {
    private String subscriptionId;
    private String tenantId;
    private String reason;
    private boolean effectiveImmediately;
    private Instant cancellationDate;
    private Instant occurredAt;
}
