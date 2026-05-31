package com.gogidix.shared.infrastructure.services.billing.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Domain event published when a new subscription is created.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCreatedEvent {
    private String subscriptionId;
    private String tenantId;
    private String plan;
    private String status;
    private String billingPeriod;
    private BigDecimal amount;
    private Instant occurredAt;
}
