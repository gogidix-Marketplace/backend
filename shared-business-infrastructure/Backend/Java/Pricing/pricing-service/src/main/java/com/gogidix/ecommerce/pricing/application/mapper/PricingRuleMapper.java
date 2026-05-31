package com.gogidix.ecommerce.pricing.application.mapper;

import com.gogidix.ecommerce.pricing.application.dto.*;
import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PricingRuleMapper {

    public PricingRuleResponse toPricingRuleResponse(PricingRule rule) {
        if (rule == null) return null;

        return new PricingRuleResponse(
                rule.getId(),
                rule.getRuleCode(),
                rule.getName(),
                rule.getDescription(),
                rule.getType() != null ? rule.getType().name() : null,
                rule.getStrategy() != null ? rule.getStrategy().name() : null,
                rule.getBasePrice(),
                rule.getSalePrice(),
                rule.getCostPrice(),
                rule.getMinimumPrice(),
                rule.getMaximumPrice(),
                rule.getDiscountPercentage(),
                rule.getDiscountAmount(),
                rule.getCurrency(),
                rule.getProductId(),
                rule.getCategoryId(),
                rule.getApplicableProductIds(),
                rule.getApplicableCategoryIds(),
                rule.getEffectiveFrom(),
                rule.getEffectiveTo(),
                rule.getIsActive(),
                rule.getPriority(),
                rule.getCreatedAt(),
                rule.getUpdatedAt()
        );
    }

    public List<PricingRuleResponse> toPricingRuleResponseList(List<PricingRule> rules) {
        return rules.stream()
                .map(this::toPricingRuleResponse)
                .collect(Collectors.toList());
    }

    public PricingRule toPricingRule(CreatePricingRuleRequest request) {
        PricingRule rule = new PricingRule();
        rule.setRuleCode(request.ruleCode());
        rule.setName(request.name());
        rule.setDescription(request.description());
        rule.setType(request.type() != null ? PricingRule.PricingType.valueOf(request.type()) : null);
        rule.setStrategy(request.strategy() != null ? PricingRule.PricingStrategy.valueOf(request.strategy()) : null);
        rule.setBasePrice(request.basePrice());
        rule.setSalePrice(request.salePrice());
        rule.setCostPrice(request.costPrice());
        rule.setMinimumPrice(request.minimumPrice());
        rule.setMaximumPrice(request.maximumPrice());
        rule.setDiscountPercentage(request.discountPercentage());
        rule.setDiscountAmount(request.discountAmount());
        rule.setCurrency(request.currency() != null ? request.currency() : "USD");
        rule.setProductId(request.productId());
        rule.setCategoryId(request.categoryId());
        rule.setApplicableProductIds(request.applicableProductIds());
        rule.setApplicableCategoryIds(request.applicableCategoryIds());
        rule.setEffectiveFrom(request.effectiveFrom());
        rule.setEffectiveTo(request.effectiveTo());
        rule.setIsActive(request.isActive() != null ? request.isActive() : true);
        rule.setPriority(request.priority() != null ? request.priority() : 0);
        return rule;
    }

    public void updatePricingRuleFromRequest(PricingRule rule, UpdatePricingRuleRequest request) {
        if (request.name() != null) rule.setName(request.name());
        if (request.description() != null) rule.setDescription(request.description());
        if (request.type() != null) rule.setType(PricingRule.PricingType.valueOf(request.type()));
        if (request.strategy() != null) rule.setStrategy(PricingRule.PricingStrategy.valueOf(request.strategy()));
        if (request.basePrice() != null) rule.setBasePrice(request.basePrice());
        if (request.salePrice() != null) rule.setSalePrice(request.salePrice());
        if (request.costPrice() != null) rule.setCostPrice(request.costPrice());
        if (request.minimumPrice() != null) rule.setMinimumPrice(request.minimumPrice());
        if (request.maximumPrice() != null) rule.setMaximumPrice(request.maximumPrice());
        if (request.discountPercentage() != null) rule.setDiscountPercentage(request.discountPercentage());
        if (request.discountAmount() != null) rule.setDiscountAmount(request.discountAmount());
        if (request.currency() != null) rule.setCurrency(request.currency());
        if (request.productId() != null) rule.setProductId(request.productId());
        if (request.categoryId() != null) rule.setCategoryId(request.categoryId());
        if (request.applicableProductIds() != null) rule.setApplicableProductIds(request.applicableProductIds());
        if (request.applicableCategoryIds() != null) rule.setApplicableCategoryIds(request.applicableCategoryIds());
        if (request.effectiveFrom() != null) rule.setEffectiveFrom(request.effectiveFrom());
        if (request.effectiveTo() != null) rule.setEffectiveTo(request.effectiveTo());
        if (request.isActive() != null) rule.setIsActive(request.isActive());
        if (request.priority() != null) rule.setPriority(request.priority());
    }
}
