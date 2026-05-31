package com.gogidix.ecommerce.pricing.application.service;

import com.gogidix.ecommerce.pricing.application.dto.*;
import com.gogidix.ecommerce.pricing.application.mapper.PricingRuleMapper;
import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import com.gogidix.ecommerce.pricing.domain.repository.PricingRuleRepository;
import com.gogidix.ecommerce.pricing.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.pricing.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricingRuleServiceTest {
    @Mock private PricingRuleRepository repository;
    @Mock private PricingRuleMapper mapper;
    @InjectMocks private PricingRuleService service;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private PricingRule buildRule() {
        PricingRule r = new PricingRule(); r.setId("id1"); r.setRuleCode("RULE1"); r.setTenantId("t1");
        return r;
    }
    private PricingRuleResponse buildResponse() { return mock(PricingRuleResponse.class); }

    @Test void getAllActivePricingRules() {
        when(repository.findByTenantIdAndIsActiveOrderByPriority("t1",true)).thenReturn(List.of(buildRule()));
        when(mapper.toPricingRuleResponse(any())).thenReturn(buildResponse());
        assertThat(service.getAllActivePricingRules()).hasSize(1);
    }
    @Test void getPricingRuleById_found() {
        when(repository.findById("id1")).thenReturn(Optional.of(buildRule()));
        when(mapper.toPricingRuleResponse(any())).thenReturn(buildResponse());
        assertThat(service.getPricingRuleById("id1")).isNotNull();
    }
    @Test void getPricingRuleById_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getPricingRuleById("x")).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void getPricingRuleByCode_found() {
        when(repository.findByTenantIdAndRuleCode("t1","CODE1")).thenReturn(buildRule());
        when(mapper.toPricingRuleResponse(any())).thenReturn(buildResponse());
        assertThat(service.getPricingRuleByCode("CODE1")).isNotNull();
    }
    @Test void getPricingRuleByCode_notFound() {
        when(repository.findByTenantIdAndRuleCode("t1","x")).thenReturn(null);
        assertThatThrownBy(() -> service.getPricingRuleByCode("x")).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void getPricingRulesByProduct() {
        when(repository.findByTenantIdAndProductIdAndIsActive("t1","prod1",true)).thenReturn(List.of(buildRule()));
        when(mapper.toPricingRuleResponse(any())).thenReturn(buildResponse());
        assertThat(service.getPricingRulesByProduct("prod1")).hasSize(1);
    }
    @Test void getPricingRulesByCategory() {
        when(repository.findByTenantIdAndCategoryIdAndIsActive("t1","cat1",true)).thenReturn(List.of(buildRule()));
        when(mapper.toPricingRuleResponse(any())).thenReturn(buildResponse());
        assertThat(service.getPricingRulesByCategory("cat1")).hasSize(1);
    }
    @Test void getPricingRulesByType() {
        when(repository.findByTenantIdAndTypeAndIsActive("t1",PricingRule.PricingType.FIXED,true)).thenReturn(List.of(buildRule()));
        when(mapper.toPricingRuleResponse(any())).thenReturn(buildResponse());
        assertThat(service.getPricingRulesByType(PricingRule.PricingType.FIXED)).hasSize(1);
    }
    @Test void createPricingRule() {
        when(repository.existsByTenantIdAndRuleCode("t1","CODE1")).thenReturn(false);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toPricingRule(any(CreatePricingRuleRequest.class))).thenReturn(buildRule());
        when(mapper.toPricingRuleResponse(any())).thenReturn(buildResponse());
        CreatePricingRuleRequest req = new CreatePricingRuleRequest("CODE1","Test","desc","FIXED","FLAT",
            BigDecimal.TEN,null,null,null,null,null,null,"USD",null,null,null,null,null,null,true,0);
        assertThat(service.createPricingRule(req)).isNotNull();
        verify(repository).save(argThat(r -> r.getTenantId().equals("t1")));
    }
    @Test void createPricingRule_duplicateCode() {
        when(repository.existsByTenantIdAndRuleCode("t1","CODE1")).thenReturn(true);
        CreatePricingRuleRequest req = new CreatePricingRuleRequest("CODE1","Test","desc","FIXED","FLAT",
            BigDecimal.TEN,null,null,null,null,null,null,"USD",null,null,null,null,null,null,true,0);
        assertThatThrownBy(() -> service.createPricingRule(req)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void updatePricingRule() {
        when(repository.findById("id1")).thenReturn(Optional.of(buildRule()));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toPricingRuleResponse(any())).thenReturn(buildResponse());
        UpdatePricingRuleRequest req = new UpdatePricingRuleRequest("Updated",null,null,null,null,null,null,null,null,null,null,null,"USD",null,null,null,null,null,true,0);
        assertThat(service.updatePricingRule("id1",req)).isNotNull();
    }
    @Test void deletePricingRule() {
        when(repository.findById("id1")).thenReturn(Optional.of(buildRule()));
        service.deletePricingRule("id1");
        verify(repository).deleteById("id1");
    }
    @Test void deletePricingRule_notFound() {
        when(repository.findById("x")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deletePricingRule("x")).isInstanceOf(IllegalArgumentException.class);
    }
}