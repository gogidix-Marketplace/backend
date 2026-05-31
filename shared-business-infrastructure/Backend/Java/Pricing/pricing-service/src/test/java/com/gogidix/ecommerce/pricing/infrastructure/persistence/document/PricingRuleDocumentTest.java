package com.gogidix.ecommerce.pricing.infrastructure.persistence.document;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PricingRuleDocumentTest {

    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void gettersAndSetters() {
        PricingRuleDocument doc = new PricingRuleDocument();
        doc.setId("id1");
        doc.setTenantId("t1");
        doc.setRuleCode("RULE1");
        doc.setName("Name");
        doc.setDescription("desc");
        doc.setType("FIXED");
        doc.setStrategy("COST_PLUS");
        doc.setBasePrice(BigDecimal.TEN);
        doc.setSalePrice(BigDecimal.ONE);
        doc.setCostPrice(BigDecimal.valueOf(5));
        doc.setMinimumPrice(BigDecimal.ONE);
        doc.setMaximumPrice(BigDecimal.valueOf(100));
        doc.setDiscountPercentage(BigDecimal.TEN);
        doc.setDiscountAmount(BigDecimal.ONE);
        doc.setCurrency("USD");
        doc.setProductId("p1");
        doc.setCategoryId("c1");
        doc.setApplicableProductIds(List.of("p1"));
        doc.setApplicableCategoryIds(List.of("c1"));
        doc.setEffectiveFrom(FIXED);
        doc.setEffectiveTo(FIXED);
        doc.setIsActive(true);
        doc.setPriority(1);
        doc.setCreatedAt(FIXED);
        doc.setUpdatedAt(FIXED);

        assertThat(doc.getId()).isEqualTo("id1");
        assertThat(doc.getTenantId()).isEqualTo("t1");
        assertThat(doc.getRuleCode()).isEqualTo("RULE1");
        assertThat(doc.getName()).isEqualTo("Name");
        assertThat(doc.getDescription()).isEqualTo("desc");
        assertThat(doc.getType()).isEqualTo("FIXED");
        assertThat(doc.getStrategy()).isEqualTo("COST_PLUS");
        assertThat(doc.getBasePrice()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(doc.getSalePrice()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(doc.getCostPrice()).isEqualByComparingTo(BigDecimal.valueOf(5));
        assertThat(doc.getMinimumPrice()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(doc.getMaximumPrice()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(doc.getDiscountPercentage()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(doc.getDiscountAmount()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(doc.getCurrency()).isEqualTo("USD");
        assertThat(doc.getProductId()).isEqualTo("p1");
        assertThat(doc.getCategoryId()).isEqualTo("c1");
        assertThat(doc.getApplicableProductIds()).containsExactly("p1");
        assertThat(doc.getApplicableCategoryIds()).containsExactly("c1");
        assertThat(doc.getEffectiveFrom()).isEqualTo(FIXED);
        assertThat(doc.getEffectiveTo()).isEqualTo(FIXED);
        assertThat(doc.getIsActive()).isTrue();
        assertThat(doc.getPriority()).isEqualTo(1);
        assertThat(doc.getCreatedAt()).isEqualTo(FIXED);
        assertThat(doc.getUpdatedAt()).isEqualTo(FIXED);
    }

    @Test
    void defaultConstructor() {
        PricingRuleDocument doc = new PricingRuleDocument();
        assertThat(doc.getId()).isNull();
        assertThat(doc.getIsActive()).isNull();
    }
}
