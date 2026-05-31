package com.gogidix.ecommerce.pricing.infrastructure.persistence.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "pricing_rules")
public class PricingRuleDocument {

    @Id
    private String id;

    @Transient
    private String tenantId;

    @Indexed(unique = true)
    @Field("rule_code")
    private String ruleCode;

    private String name;
    private String description;

    @Field("type")
    private String type;
    @Field("strategy")
    private String strategy;

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

    private Instant createdAt;
    private Instant updatedAt;

    public PricingRuleDocument() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    public BigDecimal getMinimumPrice() { return minimumPrice; }
    public void setMinimumPrice(BigDecimal minimumPrice) { this.minimumPrice = minimumPrice; }
    public BigDecimal getMaximumPrice() { return maximumPrice; }
    public void setMaximumPrice(BigDecimal maximumPrice) { this.maximumPrice = maximumPrice; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public List<String> getApplicableProductIds() { return applicableProductIds; }
    public void setApplicableProductIds(List<String> applicableProductIds) { this.applicableProductIds = applicableProductIds; }
    public List<String> getApplicableCategoryIds() { return applicableCategoryIds; }
    public void setApplicableCategoryIds(List<String> applicableCategoryIds) { this.applicableCategoryIds = applicableCategoryIds; }
    public Instant getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(Instant effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public Instant getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(Instant effectiveTo) { this.effectiveTo = effectiveTo; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
