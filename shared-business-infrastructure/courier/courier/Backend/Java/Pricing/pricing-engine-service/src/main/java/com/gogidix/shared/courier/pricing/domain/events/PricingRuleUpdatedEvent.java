package com.gogidix.shared.courier.pricing.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain Event: Pricing rule has been updated
 * Published when an existing pricing rule is modified
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleUpdatedEvent {

    private String tenantId;

    private String ruleId;

    private String ruleName;

    private LocalDateTime updatedAt;
}
