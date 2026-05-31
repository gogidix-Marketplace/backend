package com.gogidix.ecommerce.pricing.domain.policy;

import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PricingCalculationPolicy Tests")
class PricingCalculationPolicyTest {

    private PricingCalculationPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new PricingCalculationPolicy();
    }

    private PricingRule createFixedRule(BigDecimal discountAmount) {
        PricingRule rule = new PricingRule("tenant-1");
        rule.setType(PricingRule.PricingType.FIXED);
        rule.setDiscountAmount(discountAmount);
        return rule;
    }

    private PricingRule createPercentageRule(BigDecimal discountPercentage) {
        PricingRule rule = new PricingRule("tenant-1");
        rule.setType(PricingRule.PricingType.PERCENTAGE);
        rule.setDiscountPercentage(discountPercentage);
        return rule;
    }

    private PricingRule createTieredRule() {
        PricingRule rule = new PricingRule("tenant-1");
        rule.setType(PricingRule.PricingType.TIERED);
        return rule;
    }

    private PricingRule createVolumeRule(BigDecimal discountPercentage) {
        PricingRule rule = new PricingRule("tenant-1");
        rule.setType(PricingRule.PricingType.VOLUME);
        rule.setDiscountPercentage(discountPercentage);
        return rule;
    }

    private PricingRule createDynamicRule(BigDecimal salePrice, BigDecimal discountPercentage) {
        PricingRule rule = new PricingRule("tenant-1");
        rule.setType(PricingRule.PricingType.DYNAMIC);
        rule.setSalePrice(salePrice);
        rule.setDiscountPercentage(discountPercentage);
        return rule;
    }

    @Test
    @DisplayName("Fixed: subtracts discount amount from base price")
    void fixedDiscount() {
        PricingRule rule = createFixedRule(new BigDecimal("10.00"));
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 1);
        assertThat(result).isEqualByComparingTo("90.00");
    }

    @Test
    @DisplayName("Fixed: uses salePrice when no discount amount")
    void fixedWithSalePrice() {
        PricingRule rule = createFixedRule(null);
        rule.setSalePrice(new BigDecimal("79.99"));
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 1);
        assertThat(result).isEqualByComparingTo("79.99");
    }

    @Test
    @DisplayName("Percentage: applies percentage discount")
    void percentageDiscount() {
        PricingRule rule = createPercentageRule(new BigDecimal("20"));
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 1);
        assertThat(result).isEqualByComparingTo("80.00");
    }

    @Test
    @DisplayName("Percentage: returns base price when no percentage set")
    void percentageNoDiscount() {
        PricingRule rule = createPercentageRule(null);
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 1);
        assertThat(result).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Tiered: 10% discount for 10+ items")
    void tieredDiscount10Items() {
        PricingRule rule = createTieredRule();
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 10);
        assertThat(result).isEqualByComparingTo("90.00");
    }

    @Test
    @DisplayName("Tiered: 15% discount for 50+ items")
    void tieredDiscount50Items() {
        PricingRule rule = createTieredRule();
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 50);
        assertThat(result).isEqualByComparingTo("85.00");
    }

    @Test
    @DisplayName("Tiered: 20% discount for 100+ items")
    void tieredDiscount100Items() {
        PricingRule rule = createTieredRule();
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 100);
        assertThat(result).isEqualByComparingTo("80.00");
    }

    @Test
    @DisplayName("Tiered: no discount for less than 10 items")
    void tieredNoDiscount() {
        PricingRule rule = createTieredRule();
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 5);
        assertThat(result).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Volume: extra 1.5x discount for 1000+ items")
    void volumeDiscount1000Items() {
        PricingRule rule = createVolumeRule(new BigDecimal("10"));
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 1000);
        assertThat(result).isEqualByComparingTo("85.00");
    }

    @Test
    @DisplayName("Dynamic: sale price with additional percentage off")
    void dynamicPriceWithDiscount() {
        PricingRule rule = createDynamicRule(new BigDecimal("90.00"), new BigDecimal("10"));
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 1);
        assertThat(result).isEqualByComparingTo("81.00");
    }

    @Test
    @DisplayName("Enforces minimum price")
    void enforcesMinimumPrice() {
        PricingRule rule = createFixedRule(new BigDecimal("90.00"));
        rule.setMinimumPrice(new BigDecimal("50.00"));
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 1);
        assertThat(result).isEqualByComparingTo("50.00");
    }

    @Test
    @DisplayName("Enforces maximum price")
    void enforcesMaximumPrice() {
        PricingRule rule = createFixedRule(null);
        rule.setSalePrice(new BigDecimal("150.00"));
        rule.setMaximumPrice(new BigDecimal("120.00"));
        BigDecimal result = policy.calculateFinalPrice(rule, new BigDecimal("100.00"), 1);
        assertThat(result).isEqualByComparingTo("120.00");
    }

    @Test
    @DisplayName("Returns base price when rule is null")
    void nullRuleReturnsBase() {
        BigDecimal result = policy.calculateFinalPrice(null, new BigDecimal("100.00"), 1);
        assertThat(result).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Returns base price when base price is null")
    void nullBasePriceReturnsNull() {
        PricingRule rule = createFixedRule(new BigDecimal("10.00"));
        BigDecimal result = policy.calculateFinalPrice(rule, null, 1);
        assertThat(result).isNull();
    }
}
