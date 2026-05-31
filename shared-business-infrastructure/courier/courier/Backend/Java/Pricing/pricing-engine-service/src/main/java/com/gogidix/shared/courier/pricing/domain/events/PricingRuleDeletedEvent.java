package com.gogidix.shared.courier.pricing.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain Event: Pricing rule has been deleted
 * Published when a pricing rule is removed
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleDeletedEvent {

    private String tenantId;

    private String ruleId;

    private String ruleName;

    private LocalDateTime deletedAt;
}
