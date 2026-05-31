package com.gogidix.ecommerce.pricing.domain.port.in;

import com.gogidix.ecommerce.pricing.application.dto.CreatePricingRuleRequest;
import com.gogidix.ecommerce.pricing.application.dto.PricingRuleResponse;
import com.gogidix.ecommerce.pricing.application.dto.UpdatePricingRuleRequest;
import com.gogidix.ecommerce.pricing.domain.model.PricingRule;

import java.util.List;

public interface PricingRuleUseCase {

    PricingRuleResponse createPricingRule(CreatePricingRuleRequest request);

    PricingRuleResponse updatePricingRule(String id, UpdatePricingRuleRequest request);

    void deletePricingRule(String id);

    PricingRuleResponse getPricingRuleById(String id);

    PricingRuleResponse getPricingRuleByCode(String ruleCode);

    List<PricingRuleResponse> getAllActivePricingRules();

    List<PricingRuleResponse> getPricingRulesByProduct(String productId);

    List<PricingRuleResponse> getPricingRulesByCategory(String categoryId);

    List<PricingRuleResponse> getPricingRulesByType(PricingRule.PricingType type);
}
