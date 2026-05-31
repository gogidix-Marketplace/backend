package com.gogidix.ecommerce.pricing.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Pricing Domain Event Tests")
class PricingDomainEventTest {

    @Test
    @DisplayName("PricingRuleCreatedEvent holds correct values")
    void createdEvent() {
        PricingRuleCreatedEvent event = new PricingRuleCreatedEvent("id-1", "t-1", "CODE-1", "Rule 1", "FIXED");
        assertThat(event.ruleId()).isEqualTo("id-1");
        assertThat(event.tenantId()).isEqualTo("t-1");
        assertThat(event.ruleCode()).isEqualTo("CODE-1");
        assertThat(event.name()).isEqualTo("Rule 1");
        assertThat(event.type()).isEqualTo("FIXED");
        assertThat(event.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("PricingRuleUpdatedEvent holds correct values")
    void updatedEvent() {
        PricingRuleUpdatedEvent event = new PricingRuleUpdatedEvent("id-1", "t-1", "CODE-1", "Rule 1", "PERCENTAGE");
        assertThat(event.ruleId()).isEqualTo("id-1");
        assertThat(event.type()).isEqualTo("PERCENTAGE");
        assertThat(event.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("PricingRuleDeletedEvent holds correct values")
    void deletedEvent() {
        PricingRuleDeletedEvent event = new PricingRuleDeletedEvent("id-1", "t-1", "CODE-1");
        assertThat(event.ruleId()).isEqualTo("id-1");
        assertThat(event.ruleCode()).isEqualTo("CODE-1");
        assertThat(event.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("PriceCalculatedEvent holds correct values")
    void calculatedEvent() {
        PriceCalculatedEvent event = new PriceCalculatedEvent("t-1", "prod-1",
                new java.math.BigDecimal("100.00"), new java.math.BigDecimal("80.00"), "CODE-1");
        assertThat(event.tenantId()).isEqualTo("t-1");
        assertThat(event.productId()).isEqualTo("prod-1");
        assertThat(event.originalPrice()).isEqualByComparingTo("100.00");
        assertThat(event.finalPrice()).isEqualByComparingTo("80.00");
        assertThat(event.appliedRuleCode()).isEqualTo("CODE-1");
        assertThat(event.timestamp()).isNotNull();
    }
}
