package com.gogidix.ecommerce.pricing.shared.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Exception Tests")
class ExceptionTest {

    @Test
    @DisplayName("PricingRuleNotFoundException holds ruleId")
    void ruleNotFound() {
        PricingRuleNotFoundException ex = new PricingRuleNotFoundException("rule-123");
        assertThat(ex.getRuleId()).isEqualTo("rule-123");
        assertThat(ex.getMessage()).contains("rule-123");
    }

    @Test
    @DisplayName("DuplicatePricingRuleException holds ruleCode")
    void duplicateRule() {
        DuplicatePricingRuleException ex = new DuplicatePricingRuleException("CODE-1");
        assertThat(ex.getRuleCode()).isEqualTo("CODE-1");
        assertThat(ex.getMessage()).contains("CODE-1");
    }

    @Test
    @DisplayName("InvalidPricingRuleException holds message")
    void invalidRule() {
        InvalidPricingRuleException ex = new InvalidPricingRuleException("bad data");
        assertThat(ex.getMessage()).isEqualTo("bad data");
    }

    @Test
    @DisplayName("InvalidPricingRuleException holds cause")
    void invalidRuleWithCause() {
        RuntimeException cause = new RuntimeException("root cause");
        InvalidPricingRuleException ex = new InvalidPricingRuleException("bad", cause);
        assertThat(ex.getCause()).isEqualTo(cause);
    }
}
