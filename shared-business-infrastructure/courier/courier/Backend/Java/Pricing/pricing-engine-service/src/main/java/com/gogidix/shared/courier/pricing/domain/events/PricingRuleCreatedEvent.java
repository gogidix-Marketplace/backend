package com.gogidix.shared.courier.pricing.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain Event: Pricing rule has been created
 * Published when a new pricing rule is added
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleCreatedEvent {

    private String tenantId;

    private String ruleId;

    private String ruleName;

    private String ruleType;

    private LocalDateTime createdAt;
}
