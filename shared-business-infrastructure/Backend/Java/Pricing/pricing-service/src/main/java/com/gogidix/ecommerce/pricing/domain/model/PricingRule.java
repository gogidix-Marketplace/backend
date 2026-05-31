package com.gogidix.ecommerce.pricing.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "pricing_rules")
public class PricingRule extends BaseEntity {

    @Indexed(unique = true)
    @Field("rule_code")
    private String ruleCode;

    private String name;
    private String description;

    private PricingType type;
    private PricingStrategy strategy;

    private BigDecimal basePrice;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private BigDecimal minimumPrice;
    private BigDecimal maximumPrice;

    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;

    private String currency;

    @Indexed
    @Field("product_id")
    private String productId;

    @Indexed
    @Field("category_id")
    private String categoryId;

    private List<String> applicableProductIds;
    private List<String> applicableCategoryIds;

    private Instant effectiveFrom;
    private Instant effectiveTo;

    @Indexed
    @Field("is_active")
    private Boolean isActive;

    private Integer priority;

    private PricingCondition condition;

    public PricingRule() {
        super();
        this.isActive = true;
        this.currency = "USD";
        this.priority = 0;
    }

    public PricingRule(String tenantId) {
        super(tenantId);
        this.isActive = true;
        this.currency = "USD";
        this.priority = 0;
    }

    public static PricingRule create(String tenantId, String ruleCode, String name, PricingType type) {
        PricingRule rule = new PricingRule(tenantId);
        rule.ruleCode = ruleCode;
        rule.name = name;
        rule.type = type;
        return rule;
    }

    public boolean isEffectiveNow() {
        Instant now = Instant.now();
        boolean afterStart = effectiveFrom == null || !now.isBefore(effectiveFrom);
        boolean beforeEnd = effectiveTo == null || !now.isAfter(effectiveTo);
        return afterStart && beforeEnd;
    }

    @Data
    public static class PricingCondition {
        private String customerTier;
        private String region;
        private BigDecimal minimumOrderValue;
        private Integer minimumQuantity;
        private String channel;
    }

    public enum PricingType {
        FIXED, PERCENTAGE, TIERED, VOLUME, DYNAMIC
    }

    public enum PricingStrategy {
        COST_PLUS, COMPETITIVE, VALUE_BASED, PENETRATION, SKIMMING, BUNDLE
    }
}
