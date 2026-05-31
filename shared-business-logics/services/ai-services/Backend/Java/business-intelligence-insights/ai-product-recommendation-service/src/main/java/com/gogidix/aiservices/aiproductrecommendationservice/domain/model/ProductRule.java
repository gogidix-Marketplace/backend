package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Business rule for product recommendation eligibility.
 */
public class ProductRule {

    /**
     * Types of product rules.
     */
    public enum RuleType {
        AVAILABILITY,
        CATEGORY_COMPATIBILITY,
        PRICE_ELASTICITY,
        CROSS_SELL,
        UPSELL,
        SEASONALITY,
        INVENTORY_LEVEL
    }

    private ProductRuleId id;
    private String name;
    private RuleType type;
    private String condition;
    private Object value;
    private Integer priority;
    private Boolean active;
    private String algorithm;

    public ProductRule() {
        this.active = false;
    }

    // Constructor for Builder
    private ProductRule(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.type = builder.type;
        this.condition = builder.condition;
        this.value = builder.value;
        this.priority = builder.priority;
        this.active = builder.active != null ? builder.active : true;
        this.algorithm = builder.algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public ProductRule(ProductRuleId id, String name, RuleType type, String condition, Object value, Integer priority) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.condition = condition;
        this.value = value;
        this.priority = priority;
        this.active = true;
    }

    public ProductRule(ProductRuleId id, String name, RuleType type, String condition, Object value, Integer priority, Boolean active) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.condition = condition;
        this.value = value;
        this.priority = priority;
        this.active = active != null ? active : true;
    }

    // Getters
    public ProductRuleId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RuleType getType() {
        return type;
    }

    public String getCondition() {
        return condition;
    }

    public Object getValue() {
        return value;
    }

    public Integer getPriority() {
        return priority;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((ProductRule) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, priority);
    }

    @Override
    public String toString() {
        return "ProductRule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", condition='" + condition + '\'' +
                ", priority=" + priority +
                ", active=" + active +
                ", algorithm='" + algorithm + '\'' +
                '}';
    }

    /**
     * Builder for ProductRule.
     */
    public static class Builder {
        private ProductRuleId id;
        private String name;
        private RuleType type;
        private String condition;
        private Object value;
        private Integer priority;
        private Boolean active;
        private String algorithm;

        public Builder id(ProductRuleId id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(RuleType type) {
            this.type = type;
            return this;
        }

        public Builder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Builder algorithm(String algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public ProductRule build() {
            return new ProductRule(this);
        }
    }

    /**
     * Static factory method for builder.
     */
    public static Builder builder() {
        return new Builder();
    }
}
