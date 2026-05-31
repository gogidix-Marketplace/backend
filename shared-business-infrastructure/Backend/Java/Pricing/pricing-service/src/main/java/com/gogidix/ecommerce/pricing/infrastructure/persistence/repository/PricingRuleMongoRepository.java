package com.gogidix.ecommerce.pricing.infrastructure.persistence.repository;

import com.gogidix.ecommerce.pricing.infrastructure.persistence.document.PricingRuleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingRuleMongoRepository extends MongoRepository<PricingRuleDocument, String> {

    List<PricingRuleDocument> findByTenantIdAndIsActiveOrderByPriority(String tenantId, Boolean isActive);

    PricingRuleDocument findByTenantIdAndRuleCode(String tenantId, String ruleCode);

    List<PricingRuleDocument> findByTenantIdAndProductIdAndIsActive(String tenantId, String productId, Boolean isActive);

    List<PricingRuleDocument> findByTenantIdAndCategoryIdAndIsActive(String tenantId, String categoryId, Boolean isActive);

    List<PricingRuleDocument> findByTenantIdAndTypeAndIsActive(String tenantId, String type, Boolean isActive);

    boolean existsByTenantIdAndRuleCode(String tenantId, String ruleCode);
}
