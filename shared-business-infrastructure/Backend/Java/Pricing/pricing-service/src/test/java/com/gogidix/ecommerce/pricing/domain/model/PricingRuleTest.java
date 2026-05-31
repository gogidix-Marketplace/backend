package com.gogidix.ecommerce.pricing.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PricingRule Domain Model Tests")
class PricingRuleTest {

    @Test
    @DisplayName("Should create pricing rule with tenant ID")
    void shouldCreatePricingRuleWithTenantId() {
        String tenantId = "tenant-123";
        PricingRule rule = new PricingRule(tenantId);

        assertThat(rule.getTenantId()).isEqualTo(tenantId);
        assertThat(rule.getId()).isNull();
        assertThat(rule.getIsActive()).isTrue();
        assertThat(rule.getCurrency()).isEqualTo("USD");
        assertThat(rule.getPriority()).isEqualTo(0);
        assertThat(rule.getCreatedAt()).isNotNull();
        assertThat(rule.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should create pricing rule with factory method")
    void shouldCreatePricingRuleWithFactoryMethod() {
        String tenantId = "tenant-123";
        PricingRule rule = PricingRule.create(tenantId, "PRICE-001", "Standard Pricing",
                PricingRule.PricingType.FIXED);

        assertThat(rule.getTenantId()).isEqualTo(tenantId);
        assertThat(rule.getRuleCode()).isEqualTo("PRICE-001");
        assertThat(rule.getName()).isEqualTo("Standard Pricing");
        assertThat(rule.getType()).isEqualTo(PricingRule.PricingType.FIXED);
    }

    @Test
    @DisplayName("Should initialize with default values")
    void shouldInitializeWithDefaults() {
        PricingRule rule = new PricingRule();

        assertThat(rule.getIsActive()).isTrue();
        assertThat(rule.getCurrency()).isEqualTo("USD");
        assertThat(rule.getPriority()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should allow setting all pricing properties")
    void shouldAllowSettingAllPricingProperties() {
        PricingRule rule = new PricingRule("tenant-123");

        rule.setRuleCode("PRICE-001");
        rule.setName("Holiday Sale");
        rule.setDescription("Holiday season discount");
        rule.setType(PricingRule.PricingType.PERCENTAGE);
        rule.setStrategy(PricingRule.PricingStrategy.COMPETITIVE);
        rule.setBasePrice(new BigDecimal("100.00"));
        rule.setSalePrice(new BigDecimal("80.00"));
        rule.setCostPrice(new BigDecimal("50.00"));
        rule.setMinimumPrice(new BigDecimal("40.00"));
        rule.setMaximumPrice(new BigDecimal("120.00"));
        rule.setDiscountPercentage(new BigDecimal("20.00"));
        rule.setDiscountAmount(new BigDecimal("20.00"));
        rule.setCurrency("EUR");
        rule.setProductId("prod-123");
        rule.setCategoryId("cat-456");
        rule.setApplicableProductIds(List.of("prod-1", "prod-2"));
        rule.setApplicableCategoryIds(List.of("cat-1", "cat-2"));
        rule.setPriority(10);
        rule.setIsActive(false);

        assertThat(rule.getRuleCode()).isEqualTo("PRICE-001");
        assertThat(rule.getName()).isEqualTo("Holiday Sale");
        assertThat(rule.getType()).isEqualTo(PricingRule.PricingType.PERCENTAGE);
        assertThat(rule.getStrategy()).isEqualTo(PricingRule.PricingStrategy.COMPETITIVE);
        assertThat(rule.getBasePrice()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(rule.getSalePrice()).isEqualByComparingTo(new BigDecimal("80.00"));
        assertThat(rule.getCostPrice()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(rule.getDiscountPercentage()).isEqualByComparingTo(new BigDecimal("20.00"));
        assertThat(rule.getCurrency()).isEqualTo("EUR");
        assertThat(rule.getProductId()).isEqualTo("prod-123");
        assertThat(rule.getCategoryId()).isEqualTo("cat-456");
        assertThat(rule.getApplicableProductIds()).hasSize(2);
        assertThat(rule.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("Should hold pricing condition")
    void shouldHoldPricingCondition() {
        PricingRule rule = new PricingRule("tenant-123");

        PricingRule.PricingCondition condition = new PricingRule.PricingCondition();
        condition.setCustomerTier("PREMIUM");
        condition.setRegion("US-WEST");
        condition.setMinimumOrderValue(new BigDecimal("100.00"));
        condition.setMinimumQuantity(5);
        condition.setChannel("ONLINE");

        rule.setCondition(condition);

        assertThat(rule.getCondition()).isNotNull();
        assertThat(rule.getCondition().getCustomerTier()).isEqualTo("PREMIUM");
        assertThat(rule.getCondition().getRegion()).isEqualTo("US-WEST");
        assertThat(rule.getCondition().getMinimumOrderValue()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(rule.getCondition().getMinimumQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should check effective date range correctly")
    void shouldCheckEffectiveDateRange() {
        PricingRule rule = new PricingRule("tenant-123");

        rule.setEffectiveFrom(Instant.now().minusSeconds(3600));
        rule.setEffectiveTo(Instant.now().plusSeconds(3600));
        assertThat(rule.isEffectiveNow()).isTrue();

        rule.setEffectiveFrom(Instant.now().plusSeconds(3600));
        assertThat(rule.isEffectiveNow()).isFalse();
    }

    @Test
    @DisplayName("Should be effective when no date constraints")
    void shouldBeEffectiveWhenNoDateConstraints() {
        PricingRule rule = new PricingRule("tenant-123");

        assertThat(rule.isEffectiveNow()).isTrue();
    }

    @Test
    @DisplayName("Should update timestamp")
    void shouldUpdateTimestamp() {
        PricingRule rule = new PricingRule("tenant-123");
        Instant beforeUpdate = rule.getUpdatedAt();

        rule.updateTimestamp();
        Instant afterUpdate = rule.getUpdatedAt();

        assertThat(afterUpdate).isAfterOrEqualTo(beforeUpdate);
    }

    @Test
    @DisplayName("Should support all pricing types")
    void shouldSupportAllPricingTypes() {
        assertThat(PricingRule.PricingType.values()).containsExactlyInAnyOrder(
                PricingRule.PricingType.FIXED,
                PricingRule.PricingType.PERCENTAGE,
                PricingRule.PricingType.TIERED,
                PricingRule.PricingType.VOLUME,
                PricingRule.PricingType.DYNAMIC
        );
    }

    @Test
    @DisplayName("Should support all pricing strategies")
    void shouldSupportAllPricingStrategies() {
        assertThat(PricingRule.PricingStrategy.values()).containsExactlyInAnyOrder(
                PricingRule.PricingStrategy.COST_PLUS,
                PricingRule.PricingStrategy.COMPETITIVE,
                PricingRule.PricingStrategy.VALUE_BASED,
                PricingRule.PricingStrategy.PENETRATION,
                PricingRule.PricingStrategy.SKIMMING,
                PricingRule.PricingStrategy.BUNDLE
        );
    }
}
