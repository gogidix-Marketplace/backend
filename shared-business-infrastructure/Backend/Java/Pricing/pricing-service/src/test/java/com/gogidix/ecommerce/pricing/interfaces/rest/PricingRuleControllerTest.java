package com.gogidix.ecommerce.pricing.interfaces.rest;

import com.gogidix.ecommerce.pricing.application.dto.*;
import com.gogidix.ecommerce.pricing.application.service.PricingRuleService;
import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricingRuleControllerTest {
    @Mock private PricingRuleService service;
    @InjectMocks private PricingRuleController controller;

    private PricingRuleResponse buildResponse() { return mock(PricingRuleResponse.class); }

    @Test void getAllPricingRules() {
        when(service.getAllActivePricingRules()).thenReturn(List.of(buildResponse()));
        assertThat(controller.getAllPricingRules().getBody()).hasSize(1);
    }
    @Test void getPricingRule() {
        when(service.getPricingRuleById("id1")).thenReturn(buildResponse());
        assertThat(controller.getPricingRule("id1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getPricingRuleByCode() {
        when(service.getPricingRuleByCode("CODE1")).thenReturn(buildResponse());
        assertThat(controller.getPricingRuleByCode("CODE1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getPricingRulesByProduct() {
        when(service.getPricingRulesByProduct("p1")).thenReturn(List.of(buildResponse()));
        assertThat(controller.getPricingRulesByProduct("p1").getBody()).hasSize(1);
    }
    @Test void getPricingRulesByCategory() {
        when(service.getPricingRulesByCategory("c1")).thenReturn(List.of(buildResponse()));
        assertThat(controller.getPricingRulesByCategory("c1").getBody()).hasSize(1);
    }
    @Test void getPricingRulesByType() {
        when(service.getPricingRulesByType(PricingRule.PricingType.FIXED)).thenReturn(List.of(buildResponse()));
        assertThat(controller.getPricingRulesByType(PricingRule.PricingType.FIXED).getBody()).hasSize(1);
    }
    @Test void createPricingRule() {
        when(service.createPricingRule(any())).thenReturn(buildResponse());
        CreatePricingRuleRequest req = new CreatePricingRuleRequest("C1","T","d","FIXED","FLAT",
            BigDecimal.TEN,null,null,null,null,null,null,"USD",null,null,null,null,null,null,true,0);
        assertThat(controller.createPricingRule(req).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void updatePricingRule() {
        when(service.updatePricingRule(eq("id1"),any())).thenReturn(buildResponse());
        UpdatePricingRuleRequest req = new UpdatePricingRuleRequest("U",null,null,null,null,null,null,null,null,null,null,null,"USD",null,null,null,null,null,true,0);
        assertThat(controller.updatePricingRule("id1",req).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void deletePricingRule() {
        assertThat(controller.deletePricingRule("id1").getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(service).deletePricingRule("id1");
    }
}