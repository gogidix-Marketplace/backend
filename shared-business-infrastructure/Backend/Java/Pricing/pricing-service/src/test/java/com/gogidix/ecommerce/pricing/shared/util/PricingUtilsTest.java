package com.gogidix.ecommerce.pricing.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PricingUtils Tests")
class PricingUtilsTest {

    @Test
    @DisplayName("generateId returns non-null non-empty string")
    void generateId() {
        String id = PricingUtils.generateId();
        assertThat(id).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("scalePrice scales to 2 decimal places")
    void scalePrice() {
        BigDecimal result = PricingUtils.scalePrice(new BigDecimal("10.12345"));
        assertThat(result).isEqualByComparingTo("10.12");
    }

    @Test
    @DisplayName("scalePrice returns ZERO for null")
    void scalePriceNull() {
        assertThat(PricingUtils.scalePrice(null)).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("calculatePercentage computes percentage correctly")
    void calculatePercentage() {
        BigDecimal result = PricingUtils.calculatePercentage(new BigDecimal("200"), new BigDecimal("15"));
        assertThat(result).isEqualByComparingTo("30.00");
    }

    @Test
    @DisplayName("calculatePercentage returns ZERO for null inputs")
    void calculatePercentageNull() {
        assertThat(PricingUtils.calculatePercentage(null, new BigDecimal("10"))).isEqualByComparingTo("0");
        assertThat(PricingUtils.calculatePercentage(new BigDecimal("100"), null)).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("isWithinRange returns true when value is within bounds")
    void withinRange() {
        assertThat(PricingUtils.isWithinRange(new BigDecimal("50"), new BigDecimal("10"), new BigDecimal("100"))).isTrue();
    }

    @Test
    @DisplayName("isWithinRange returns false when value is below minimum")
    void belowMinimum() {
        assertThat(PricingUtils.isWithinRange(new BigDecimal("5"), new BigDecimal("10"), new BigDecimal("100"))).isFalse();
    }

    @Test
    @DisplayName("isWithinRange returns false when value is above maximum")
    void aboveMaximum() {
        assertThat(PricingUtils.isWithinRange(new BigDecimal("150"), new BigDecimal("10"), new BigDecimal("100"))).isFalse();
    }

    @Test
    @DisplayName("isWithinRange handles null min/max as unbounded")
    void unboundedRange() {
        assertThat(PricingUtils.isWithinRange(new BigDecimal("50"), null, null)).isTrue();
        assertThat(PricingUtils.isWithinRange(new BigDecimal("50"), null, new BigDecimal("100"))).isTrue();
        assertThat(PricingUtils.isWithinRange(new BigDecimal("50"), new BigDecimal("10"), null)).isTrue();
    }

    @Test
    @DisplayName("isWithinRange returns false for null value")
    void nullValue() {
        assertThat(PricingUtils.isWithinRange(null, null, null)).isFalse();
    }

    @Test
    @DisplayName("isEffectiveNow returns true for current time")
    void effectiveNow() {
        Instant from = Instant.now().minusSeconds(3600);
        Instant to = Instant.now().plusSeconds(3600);
        assertThat(PricingUtils.isEffectiveNow(from, to)).isTrue();
    }

    @Test
    @DisplayName("isEffectiveNow returns true when dates are null")
    void effectiveNowNullDates() {
        assertThat(PricingUtils.isEffectiveNow(null, null)).isTrue();
    }

    @Test
    @DisplayName("isEffectiveNow returns false for past period")
    void effectiveNowPast() {
        Instant from = Instant.now().minusSeconds(7200);
        Instant to = Instant.now().minusSeconds(3600);
        assertThat(PricingUtils.isEffectiveNow(from, to)).isFalse();
    }

    @Test
    @DisplayName("defaultExpiry returns future instant")
    void defaultExpiry() {
        assertThat(PricingUtils.defaultExpiry()).isAfter(Instant.now());
    }

    @Test
    @DisplayName("isPositive returns true for positive values")
    void isPositive() {
        assertThat(PricingUtils.isPositive(new BigDecimal("1"))).isTrue();
        assertThat(PricingUtils.isPositive(new BigDecimal("0"))).isFalse();
        assertThat(PricingUtils.isPositive(new BigDecimal("-1"))).isFalse();
        assertThat(PricingUtils.isPositive(null)).isFalse();
    }
}
