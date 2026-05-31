package com.gogidix.ecommerce.pricing.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import com.gogidix.ecommerce.pricing.domain.port.out.PricingRuleRepositoryPort;
import com.gogidix.ecommerce.pricing.infrastructure.persistence.document.PricingRuleDocument;
import com.gogidix.ecommerce.pricing.infrastructure.persistence.repository.PricingRuleMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PricingRulePersistenceAdapter implements PricingRuleRepositoryPort {

    private final PricingRuleMongoRepository mongoRepository;

    public PricingRulePersistenceAdapter(PricingRuleMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public PricingRule save(PricingRule rule) {
        PricingRuleDocument doc = toDocument(rule);
        PricingRuleDocument saved = mongoRepository.save(doc);
        return toDomain(saved);
    }

    @Override
    public Optional<PricingRule> findById(String id) {
        return mongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<PricingRule> findByTenantIdAndIsActiveOrderByPriority(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActiveOrderByPriority(tenantId, isActive)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public PricingRule findByTenantIdAndRuleCode(String tenantId, String ruleCode) {
        PricingRuleDocument doc = mongoRepository.findByTenantIdAndRuleCode(tenantId, ruleCode);
        return doc != null ? toDomain(doc) : null;
    }

    @Override
    public List<PricingRule> findByTenantIdAndProductIdAndIsActive(String tenantId, String productId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndProductIdAndIsActive(tenantId, productId, isActive)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<PricingRule> findByTenantIdAndCategoryIdAndIsActive(String tenantId, String categoryId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndCategoryIdAndIsActive(tenantId, categoryId, isActive)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<PricingRule> findByTenantIdAndTypeAndIsActive(String tenantId, PricingRule.PricingType type, Boolean isActive) {
        return mongoRepository.findByTenantIdAndTypeAndIsActive(tenantId, type.name(), isActive)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByTenantIdAndRuleCode(String tenantId, String ruleCode) {
        return mongoRepository.existsByTenantIdAndRuleCode(tenantId, ruleCode);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    private PricingRuleDocument toDocument(PricingRule rule) {
        PricingRuleDocument doc = new PricingRuleDocument();
        doc.setId(rule.getId());
        doc.setTenantId(rule.getTenantId());
        doc.setRuleCode(rule.getRuleCode());
        doc.setName(rule.getName());
        doc.setDescription(rule.getDescription());
        doc.setType(rule.getType() != null ? rule.getType().name() : null);
        doc.setStrategy(rule.getStrategy() != null ? rule.getStrategy().name() : null);
        doc.setBasePrice(rule.getBasePrice());
        doc.setSalePrice(rule.getSalePrice());
        doc.setCostPrice(rule.getCostPrice());
        doc.setMinimumPrice(rule.getMinimumPrice());
        doc.setMaximumPrice(rule.getMaximumPrice());
        doc.setDiscountPercentage(rule.getDiscountPercentage());
        doc.setDiscountAmount(rule.getDiscountAmount());
        doc.setCurrency(rule.getCurrency());
        doc.setProductId(rule.getProductId());
        doc.setCategoryId(rule.getCategoryId());
        doc.setApplicableProductIds(rule.getApplicableProductIds());
        doc.setApplicableCategoryIds(rule.getApplicableCategoryIds());
        doc.setEffectiveFrom(rule.getEffectiveFrom());
        doc.setEffectiveTo(rule.getEffectiveTo());
        doc.setIsActive(rule.getIsActive());
        doc.setPriority(rule.getPriority());
        return doc;
    }

    private PricingRule toDomain(PricingRuleDocument doc) {
        PricingRule rule = new PricingRule();
        rule.setId(doc.getId());
        rule.setTenantId(doc.getTenantId());
        rule.setRuleCode(doc.getRuleCode());
        rule.setName(doc.getName());
        rule.setDescription(doc.getDescription());
        rule.setType(doc.getType() != null ? PricingRule.PricingType.valueOf(doc.getType()) : null);
        rule.setStrategy(doc.getStrategy() != null ? PricingRule.PricingStrategy.valueOf(doc.getStrategy()) : null);
        rule.setBasePrice(doc.getBasePrice());
        rule.setSalePrice(doc.getSalePrice());
        rule.setCostPrice(doc.getCostPrice());
        rule.setMinimumPrice(doc.getMinimumPrice());
        rule.setMaximumPrice(doc.getMaximumPrice());
        rule.setDiscountPercentage(doc.getDiscountPercentage());
        rule.setDiscountAmount(doc.getDiscountAmount());
        rule.setCurrency(doc.getCurrency());
        rule.setProductId(doc.getProductId());
        rule.setCategoryId(doc.getCategoryId());
        rule.setApplicableProductIds(doc.getApplicableProductIds());
        rule.setApplicableCategoryIds(doc.getApplicableCategoryIds());
        rule.setEffectiveFrom(doc.getEffectiveFrom());
        rule.setEffectiveTo(doc.getEffectiveTo());
        rule.setIsActive(doc.getIsActive());
        rule.setPriority(doc.getPriority());
        return rule;
    }
}
