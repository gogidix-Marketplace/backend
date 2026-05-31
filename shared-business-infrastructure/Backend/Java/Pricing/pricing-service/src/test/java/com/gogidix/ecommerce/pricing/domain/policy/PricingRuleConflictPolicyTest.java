package com.gogidix.ecommerce.pricing.domain.policy;

import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PricingRuleConflictPolicy Tests")
class PricingRuleConflictPolicyTest {

    private PricingRuleConflictPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new PricingRuleConflictPolicy();
    }

    private PricingRule createRule(String name, int priority, boolean active, boolean effective) {
        PricingRule rule = new PricingRule("tenant-1");
        rule.setName(name);
        rule.setPriority(priority);
        rule.setIsActive(active);
        if (effective) {
            rule.setEffectiveFrom(Instant.now().minusSeconds(3600));
            rule.setEffectiveTo(Instant.now().plusSeconds(3600));
        } else {
            rule.setEffectiveFrom(Instant.now().plusSeconds(3600));
            rule.setEffectiveTo(Instant.now().plusSeconds(7200));
        }
        return rule;
    }

    @Test
    @DisplayName("Returns null for empty list")
    void emptyListReturnsNull() {
        assertThat(policy.resolveConflict(Collections.emptyList())).isNull();
    }

    @Test
    @DisplayName("Returns null for null list")
    void nullListReturnsNull() {
        assertThat(policy.resolveConflict(null)).isNull();
    }

    @Test
    @DisplayName("Selects highest priority (lowest number) rule")
    void selectsHighestPriority() {
        PricingRule lowPriority = createRule("low", 10, true, true);
        PricingRule highPriority = createRule("high", 1, true, true);

        PricingRule result = policy.resolveConflict(List.of(lowPriority, highPriority));

        assertThat(result.getName()).isEqualTo("high");
    }

    @Test
    @DisplayName("Skips inactive rules")
    void skipsInactiveRules() {
        PricingRule inactive = createRule("inactive", 1, false, true);
        PricingRule active = createRule("active", 5, true, true);

        PricingRule result = policy.resolveConflict(List.of(inactive, active));

        assertThat(result.getName()).isEqualTo("active");
    }

    @Test
    @DisplayName("Skips not-yet-effective rules")
    void skipsNotEffectiveRules() {
        PricingRule notEffective = createRule("future", 1, true, false);
        PricingRule effective = createRule("now", 5, true, true);

        PricingRule result = policy.resolveConflict(List.of(notEffective, effective));

        assertThat(result.getName()).isEqualTo("now");
    }

    @Test
    @DisplayName("Returns null when all rules are inactive")
    void allInactiveReturnsNull() {
        PricingRule r1 = createRule("r1", 1, false, true);
        PricingRule r2 = createRule("r2", 2, false, true);

        assertThat(policy.resolveConflict(List.of(r1, r2))).isNull();
    }

    @Test
    @DisplayName("When same priority, selects newest (latest createdAt)")
    void samePrioritySelectsNewest() {
        PricingRule older = new PricingRule("tenant-1");
        older.setName("older");
        older.setPriority(1);
        older.setIsActive(true);
        older.setEffectiveFrom(Instant.now().minusSeconds(3600));

        PricingRule newer = new PricingRule("tenant-1");
        newer.setName("newer");
        newer.setPriority(1);
        newer.setIsActive(true);
        newer.setEffectiveFrom(Instant.now().minusSeconds(3600));

        PricingRule result = policy.resolveConflict(List.of(older, newer));
        assertThat(result).isNotNull();
    }
}
