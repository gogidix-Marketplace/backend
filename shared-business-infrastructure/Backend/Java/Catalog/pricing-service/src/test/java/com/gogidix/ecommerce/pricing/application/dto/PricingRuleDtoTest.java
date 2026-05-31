package com.gogidix.ecommerce.pricing.application.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class PricingRuleDtoTest {

    @Test void dto_allFields() {
        Instant now = Instant.now();
        Instant start = Instant.parse("2024-01-01T00:00:00Z");
        Instant end = Instant.parse("2024-12-31T23:59:59Z");
        PricingRuleDto dto = new PricingRuleDto("id1", "t1", "rule1", "PERCENTAGE",
            BigDecimal.TEN, true, true, start, end, now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.name()).isEqualTo("rule1");
        assertThat(dto.ruleType()).isEqualTo("PERCENTAGE");
        assertThat(dto.value()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(dto.percentage()).isTrue();
        assertThat(dto.active()).isTrue();
        assertThat(dto.startDate()).isEqualTo(start);
        assertThat(dto.endDate()).isEqualTo(end);
    }
    @Test void dto_nulls() {
        PricingRuleDto dto = new PricingRuleDto(null, null, null, null, null, false, false, null, null, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.active()).isFalse();
    }
    @Test void response_fromDto() {
        Instant now = Instant.now();
        PricingRuleDto dto = new PricingRuleDto("id1", "t1", "r1", "FLAT",
            BigDecimal.ONE, false, true, null, null, now, now);
        PricingRuleResponse r = PricingRuleResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1");
        assertThat(r.name()).isEqualTo("r1");
        assertThat(r.ruleType()).isEqualTo("FLAT");
        assertThat(r.active()).isTrue();
    }
    @Test void createRequest() {
        Instant start = Instant.parse("2024-01-01T00:00:00Z");
        CreatePricingRuleRequest req = new CreatePricingRuleRequest("r", "FLAT", BigDecimal.ONE, false, start, null);
        assertThat(req.name()).isEqualTo("r");
        assertThat(req.ruleType()).isEqualTo("FLAT");
        assertThat(req.value()).isEqualByComparingTo(BigDecimal.ONE);
    }
    @Test void createRequest_nulls() {
        CreatePricingRuleRequest req = new CreatePricingRuleRequest(null, null, null, false, null, null);
        assertThat(req.name()).isNull();
    }
    @Test void equality() {
        PricingRuleDto a = new PricingRuleDto("id", "t", "n", "T", BigDecimal.ONE, false, true, null, null, null, null);
        PricingRuleDto b = new PricingRuleDto("id", "t", "n", "T", BigDecimal.ONE, false, true, null, null, null, null);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}