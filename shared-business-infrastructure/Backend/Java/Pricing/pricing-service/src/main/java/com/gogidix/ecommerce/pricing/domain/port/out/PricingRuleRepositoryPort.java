package com.gogidix.ecommerce.pricing.domain.port.out;

import com.gogidix.ecommerce.pricing.domain.model.PricingRule;

import java.util.List;
import java.util.Optional;

public interface PricingRuleRepositoryPort {

    PricingRule save(PricingRule rule);

    Optional<PricingRule> findById(String id);

    List<PricingRule> findByTenantIdAndIsActiveOrderByPriority(String tenantId, Boolean isActive);

    PricingRule findByTenantIdAndRuleCode(String tenantId, String ruleCode);

    List<PricingRule> findByTenantIdAndProductIdAndIsActive(String tenantId, String productId, Boolean isActive);

    List<PricingRule> findByTenantIdAndCategoryIdAndIsActive(String tenantId, String categoryId, Boolean isActive);

    List<PricingRule> findByTenantIdAndTypeAndIsActive(String tenantId, PricingRule.PricingType type, Boolean isActive);

    boolean existsByTenantIdAndRuleCode(String tenantId, String ruleCode);

    void deleteById(String id);
}
