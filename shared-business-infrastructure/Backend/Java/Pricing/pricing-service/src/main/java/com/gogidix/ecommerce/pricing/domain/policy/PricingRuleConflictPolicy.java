package com.gogidix.ecommerce.pricing.domain.policy;

import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PricingRuleConflictPolicy {

    public PricingRule resolveConflict(List<PricingRule> applicableRules) {
        if (applicableRules == null || applicableRules.isEmpty()) return null;

        return applicableRules.stream()
                .filter(rule -> rule.getIsActive() != null && rule.getIsActive())
                .filter(PricingRule::isEffectiveNow)
                .min((r1, r2) -> {
                    int priorityCompare = Integer.compare(
                            r1.getPriority() != null ? r1.getPriority() : 0,
                            r2.getPriority() != null ? r2.getPriority() : 0);
                    if (priorityCompare != 0) return priorityCompare;
                    return r2.getCreatedAt().compareTo(r1.getCreatedAt());
                })
                .orElse(null);
    }
}
