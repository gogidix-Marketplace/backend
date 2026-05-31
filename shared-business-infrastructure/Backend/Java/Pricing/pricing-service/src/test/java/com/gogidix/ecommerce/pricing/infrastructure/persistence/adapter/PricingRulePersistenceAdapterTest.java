package com.gogidix.ecommerce.pricing.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import com.gogidix.ecommerce.pricing.infrastructure.persistence.document.PricingRuleDocument;
import com.gogidix.ecommerce.pricing.infrastructure.persistence.repository.PricingRuleMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PricingRulePersistenceAdapterTest {

    private PricingRuleMongoRepository mongoRepo;
    private PricingRulePersistenceAdapter adapter;
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");

    @BeforeEach
    void setUp() {
        mongoRepo = mock(PricingRuleMongoRepository.class);
        adapter = new PricingRulePersistenceAdapter(mongoRepo);
    }

    private PricingRuleDocument createDoc() {
        PricingRuleDocument doc = new PricingRuleDocument();
        doc.setId("id1");
        doc.setTenantId("t1");
        doc.setRuleCode("RULE1");
        doc.setName("Name");
        doc.setType("FIXED");
        doc.setStrategy("COST_PLUS");
        doc.setBasePrice(BigDecimal.TEN);
        doc.setIsActive(true);
        doc.setPriority(1);
        doc.setCreatedAt(FIXED);
        return doc;
    }

    private PricingRule createRule() {
        PricingRule r = new PricingRule("t1");
        r.setId("id1");
        r.setRuleCode("RULE1");
        r.setName("Name");
        r.setType(PricingRule.PricingType.FIXED);
        r.setStrategy(PricingRule.PricingStrategy.COST_PLUS);
        r.setBasePrice(BigDecimal.TEN);
        r.setIsActive(true);
        r.setPriority(1);
        return r;
    }

    @Test
    void save() {
        when(mongoRepo.save(any())).thenReturn(createDoc());
        PricingRule saved = adapter.save(createRule());
        assertThat(saved.getId()).isEqualTo("id1");
        assertThat(saved.getRuleCode()).isEqualTo("RULE1");
        verify(mongoRepo).save(any());
    }

    @Test
    void findById_present() {
        when(mongoRepo.findById("id1")).thenReturn(Optional.of(createDoc()));
        Optional<PricingRule> result = adapter.findById("id1");
        assertThat(result).isPresent();
        assertThat(result.get().getRuleCode()).isEqualTo("RULE1");
    }

    @Test
    void findById_empty() {
        when(mongoRepo.findById("x")).thenReturn(Optional.empty());
        assertThat(adapter.findById("x")).isEmpty();
    }

    @Test
    void findByTenantIdAndIsActive() {
        when(mongoRepo.findByTenantIdAndIsActiveOrderByPriority("t1", true))
                .thenReturn(List.of(createDoc()));
        List<PricingRule> result = adapter.findByTenantIdAndIsActiveOrderByPriority("t1", true);
        assertThat(result).hasSize(1);
    }

    @Test
    void findByTenantIdAndRuleCode_found() {
        when(mongoRepo.findByTenantIdAndRuleCode("t1", "RULE1")).thenReturn(createDoc());
        PricingRule result = adapter.findByTenantIdAndRuleCode("t1", "RULE1");
        assertThat(result).isNotNull();
        assertThat(result.getRuleCode()).isEqualTo("RULE1");
    }

    @Test
    void findByTenantIdAndRuleCode_notFound() {
        when(mongoRepo.findByTenantIdAndRuleCode("t1", "X")).thenReturn(null);
        assertThat(adapter.findByTenantIdAndRuleCode("t1", "X")).isNull();
    }

    @Test
    void findByTenantIdAndProductId() {
        when(mongoRepo.findByTenantIdAndProductIdAndIsActive("t1", "p1", true))
                .thenReturn(List.of(createDoc()));
        assertThat(adapter.findByTenantIdAndProductIdAndIsActive("t1", "p1", true)).hasSize(1);
    }

    @Test
    void findByTenantIdAndCategoryId() {
        when(mongoRepo.findByTenantIdAndCategoryIdAndIsActive("t1", "c1", true))
                .thenReturn(List.of(createDoc()));
        assertThat(adapter.findByTenantIdAndCategoryIdAndIsActive("t1", "c1", true)).hasSize(1);
    }

    @Test
    void findByTenantIdAndType() {
        when(mongoRepo.findByTenantIdAndTypeAndIsActive("t1", "FIXED", true))
                .thenReturn(List.of(createDoc()));
        assertThat(adapter.findByTenantIdAndTypeAndIsActive("t1", PricingRule.PricingType.FIXED, true)).hasSize(1);
    }

    @Test
    void existsByTenantIdAndRuleCode() {
        when(mongoRepo.existsByTenantIdAndRuleCode("t1", "RULE1")).thenReturn(true);
        assertThat(adapter.existsByTenantIdAndRuleCode("t1", "RULE1")).isTrue();
    }

    @Test
    void deleteById() {
        adapter.deleteById("id1");
        verify(mongoRepo).deleteById("id1");
    }
}
