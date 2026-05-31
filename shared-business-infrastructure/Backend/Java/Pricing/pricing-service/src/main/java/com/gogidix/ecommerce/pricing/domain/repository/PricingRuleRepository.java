package com.gogidix.ecommerce.pricing.domain.repository;

import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingRuleRepository extends MongoRepository<PricingRule, String> {

    List<PricingRule> findByTenantIdAndIsActiveOrderByPriority(String tenantId, Boolean isActive);

    PricingRule findByTenantIdAndRuleCode(String tenantId, String ruleCode);

    List<PricingRule> findByTenantIdAndProductIdAndIsActive(String tenantId, String productId, Boolean isActive);

    List<PricingRule> findByTenantIdAndCategoryIdAndIsActive(String tenantId, String categoryId, Boolean isActive);

    List<PricingRule> findByTenantIdAndTypeAndIsActive(String tenantId, PricingRule.PricingType type, Boolean isActive);

    boolean existsByTenantIdAndRuleCode(String tenantId, String ruleCode);
}
