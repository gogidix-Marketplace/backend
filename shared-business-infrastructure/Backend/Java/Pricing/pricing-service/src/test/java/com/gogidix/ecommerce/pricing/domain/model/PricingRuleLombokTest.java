package com.gogidix.ecommerce.pricing.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class PricingRuleLombokTest {
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");
    private PricingRule createFull() {
        PricingRule r = new PricingRule("t1");
        r.setId("id1"); r.setRuleCode("RULE1"); r.setName("Test Rule");
        r.setDescription("desc"); r.setType(PricingRule.PricingType.FIXED);
        r.setStrategy(PricingRule.PricingStrategy.COST_PLUS); r.setBasePrice(BigDecimal.TEN);
        r.setSalePrice(BigDecimal.ONE); r.setCostPrice(BigDecimal.valueOf(5));
        r.setMinimumPrice(BigDecimal.ONE); r.setMaximumPrice(BigDecimal.valueOf(100));
        r.setDiscountPercentage(BigDecimal.TEN); r.setDiscountAmount(BigDecimal.ONE);
        r.setCurrency("USD"); r.setProductId("p1"); r.setCategoryId("c1");
        r.setApplicableProductIds(List.of("p1")); r.setApplicableCategoryIds(List.of("c1"));
        r.setEffectiveFrom(FIXED); r.setEffectiveTo(FIXED);
        r.setIsActive(true); r.setPriority(1);
        return r;
    }

    @Test void equals_same() { assertThat(createFull()).isEqualTo(createFull()); }
    @Test void equals_different() {
        PricingRule r1 = createFull(); PricingRule r2 = createFull(); r2.setName("Other");
        assertThat(r1).isNotEqualTo(r2);
    }
    @Test void equals_null() { assertThat(createFull()).isNotEqualTo(null); }
    @Test void hashCode_consistency() { PricingRule r = createFull(); assertThat(r.hashCode()).isEqualTo(r.hashCode()); }
    @Test void toString_notNull() { assertThat(createFull().toString()).contains("PricingRule"); }
    @Test void canEqual() { assertThat(createFull().canEqual(new PricingRule())).isTrue(); }
    @Test void canEqual_false() { assertThat(createFull().canEqual("str")).isFalse(); }
    @Test void equals_self() { PricingRule r = createFull(); assertThat(r).isEqualTo(r); }
    @Test void isEffectiveNow() {
        PricingRule r = createFull(); r.setEffectiveFrom(null); r.setEffectiveTo(null);
        assertThat(r.isEffectiveNow()).isTrue();
    }
    @Test void isEffectiveNow_pastEnd() {
        PricingRule r = createFull(); r.setEffectiveTo(Instant.now().minusSeconds(3600));
        assertThat(r.isEffectiveNow()).isFalse();
    }
    @Test void create() {
        PricingRule r = PricingRule.create("t1", "CODE", "Name", PricingRule.PricingType.FIXED);
        assertThat(r.getRuleCode()).isEqualTo("CODE");
        assertThat(r.getTenantId()).isEqualTo("t1");
    }
}