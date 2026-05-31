package com.gogidix.ecommerce.pricing.application.mapper;

import com.gogidix.ecommerce.pricing.application.dto.CreatePricingRuleRequest;
import com.gogidix.ecommerce.pricing.application.dto.PricingRuleResponse;
import com.gogidix.ecommerce.pricing.application.dto.UpdatePricingRuleRequest;
import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PricingRuleMapperTest {

    private final PricingRuleMapper mapper = new PricingRuleMapper();
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");

    private PricingRule createRule() {
        PricingRule r = new PricingRule("t1");
        r.setId("id1");
        r.setRuleCode("RULE1");
        r.setName("Test Rule");
        r.setDescription("desc");
        r.setType(PricingRule.PricingType.FIXED);
        r.setStrategy(PricingRule.PricingStrategy.COST_PLUS);
        r.setBasePrice(BigDecimal.TEN);
        r.setSalePrice(BigDecimal.ONE);
        r.setCostPrice(BigDecimal.valueOf(5));
        r.setMinimumPrice(BigDecimal.ONE);
        r.setMaximumPrice(BigDecimal.valueOf(100));
        r.setDiscountPercentage(BigDecimal.TEN);
        r.setDiscountAmount(BigDecimal.ONE);
        r.setCurrency("USD");
        r.setProductId("p1");
        r.setCategoryId("c1");
        r.setApplicableProductIds(List.of("p1"));
        r.setApplicableCategoryIds(List.of("c1"));
        r.setEffectiveFrom(FIXED);
        r.setEffectiveTo(FIXED);
        r.setIsActive(true);
        r.setPriority(1);
        return r;
    }

    @Test
    void toResponse_full() {
        PricingRule rule = createRule();
        PricingRuleResponse resp = mapper.toPricingRuleResponse(rule);
        assertThat(resp.id()).isEqualTo("id1");
        assertThat(resp.ruleCode()).isEqualTo("RULE1");
        assertThat(resp.name()).isEqualTo("Test Rule");
        assertThat(resp.type()).isEqualTo("FIXED");
        assertThat(resp.strategy()).isEqualTo("COST_PLUS");
        assertThat(resp.basePrice()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(resp.salePrice()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(resp.costPrice()).isEqualByComparingTo(BigDecimal.valueOf(5));
        assertThat(resp.currency()).isEqualTo("USD");
        assertThat(resp.productId()).isEqualTo("p1");
        assertThat(resp.categoryId()).isEqualTo("c1");
        assertThat(resp.isActive()).isTrue();
        assertThat(resp.priority()).isEqualTo(1);
    }

    @Test
    void toResponse_null() {
        assertThat(mapper.toPricingRuleResponse(null)).isNull();
    }

    @Test
    void toResponse_nullEnums() {
        PricingRule rule = createRule();
        rule.setType(null);
        rule.setStrategy(null);
        PricingRuleResponse resp = mapper.toPricingRuleResponse(rule);
        assertThat(resp.type()).isNull();
        assertThat(resp.strategy()).isNull();
    }

    @Test
    void toResponseList() {
        List<PricingRuleResponse> list = mapper.toPricingRuleResponseList(List.of(createRule(), createRule()));
        assertThat(list).hasSize(2);
        assertThat(list.get(0).ruleCode()).isEqualTo("RULE1");
    }

    @Test
    void toPricingRule_full() {
        CreatePricingRuleRequest req = new CreatePricingRuleRequest(
                "CODE", "Name", "desc", "FIXED", "COST_PLUS",
                BigDecimal.TEN, BigDecimal.ONE, BigDecimal.valueOf(5),
                BigDecimal.ONE, BigDecimal.valueOf(100),
                BigDecimal.TEN, BigDecimal.ONE, "EUR", "p1", "c1",
                List.of("p1"), List.of("c1"), FIXED, FIXED, true, 1
        );
        PricingRule rule = mapper.toPricingRule(req);
        assertThat(rule.getRuleCode()).isEqualTo("CODE");
        assertThat(rule.getName()).isEqualTo("Name");
        assertThat(rule.getType()).isEqualTo(PricingRule.PricingType.FIXED);
        assertThat(rule.getStrategy()).isEqualTo(PricingRule.PricingStrategy.COST_PLUS);
        assertThat(rule.getCurrency()).isEqualTo("EUR");
    }

    @Test
    void toPricingRule_nullCurrency_defaults() {
        CreatePricingRuleRequest req = new CreatePricingRuleRequest(
                "CODE", "Name", null, "FIXED", null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null
        );
        PricingRule rule = mapper.toPricingRule(req);
        assertThat(rule.getCurrency()).isEqualTo("USD");
        assertThat(rule.getIsActive()).isTrue();
        assertThat(rule.getPriority()).isEqualTo(0);
    }

    @Test
    void toPricingRule_nullEnums() {
        CreatePricingRuleRequest req = new CreatePricingRuleRequest(
                "CODE", "Name", null, null, null,
                null, null, null, null, null, null, null,
                "USD", null, null, null, null, null, null, false, 5
        );
        PricingRule rule = mapper.toPricingRule(req);
        assertThat(rule.getType()).isNull();
        assertThat(rule.getStrategy()).isNull();
        assertThat(rule.getIsActive()).isFalse();
        assertThat(rule.getPriority()).isEqualTo(5);
    }

    @Test
    void updatePricingRule_allFields() {
        PricingRule rule = createRule();
        UpdatePricingRuleRequest req = new UpdatePricingRuleRequest(
                "NewName", "newdesc", "PERCENTAGE", "COMPETITIVE",
                BigDecimal.valueOf(20), BigDecimal.valueOf(2), BigDecimal.valueOf(10),
                BigDecimal.valueOf(2), BigDecimal.valueOf(200),
                BigDecimal.valueOf(20), BigDecimal.valueOf(2), "GBP",
                "p2", "c2", List.of("p2"), List.of("c2"),
                FIXED, FIXED, false, 5
        );
        mapper.updatePricingRuleFromRequest(rule, req);
        assertThat(rule.getName()).isEqualTo("NewName");
        assertThat(rule.getType()).isEqualTo(PricingRule.PricingType.PERCENTAGE);
        assertThat(rule.getStrategy()).isEqualTo(PricingRule.PricingStrategy.COMPETITIVE);
        assertThat(rule.getCurrency()).isEqualTo("GBP");
        assertThat(rule.getIsActive()).isFalse();
        assertThat(rule.getPriority()).isEqualTo(5);
    }

    @Test
    void updatePricingRule_nullFields_noChange() {
        PricingRule rule = createRule();
        UpdatePricingRuleRequest req = new UpdatePricingRuleRequest(
                null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null
        );
        mapper.updatePricingRuleFromRequest(rule, req);
        assertThat(rule.getName()).isEqualTo("Test Rule");
        assertThat(rule.getType()).isEqualTo(PricingRule.PricingType.FIXED);
        assertThat(rule.getCurrency()).isEqualTo("USD");
    }
}
