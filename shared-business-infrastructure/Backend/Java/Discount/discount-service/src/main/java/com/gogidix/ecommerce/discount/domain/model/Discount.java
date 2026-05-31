package com.gogidix.ecommerce.discount.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;

@Document(collection = "discounts")
public class Discount extends BaseEntity {

    private String name;
    private String description;
    private String type;

    @Indexed
    @Field("is_active")
    private Boolean isActive;

    private Integer priority;

    private java.math.BigDecimal discountPercentage;
    private java.math.BigDecimal discountAmount;
    private java.math.BigDecimal minOrderValue;

    public Discount() {
        this.isActive = true;
        this.priority = 0;
    }

    public Discount(String tenantId) {
        super(tenantId);
        this.isActive = true;
        this.priority = 0;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public java.math.BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(java.math.BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public java.math.BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(java.math.BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public java.math.BigDecimal getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(java.math.BigDecimal minOrderValue) { this.minOrderValue = minOrderValue; }
}
