package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Configuration for a recommendation rule set.
 */
public class RecommendationRule {

    private RecommendationRuleId id;
    private String name;
    private String ruleSet;
    private String algorithm;
    private Double weight;
    private Boolean active;

    public RecommendationRule() {
        this.id = RecommendationRuleId.randomUUID();
        this.active = true;
    }

    public RecommendationRule(RecommendationRuleId id, String name, String ruleSet, String algorithm, Double weight, Boolean active) {
        this.id = id;
        this.name = name;
        this.ruleSet = ruleSet;
        this.algorithm = algorithm;
        this.weight = weight;
        this.active = active != null ? active : true;
    }

    // Constructor for Builder
    private RecommendationRule(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.ruleSet = builder.ruleSet;
        this.algorithm = builder.algorithm;
        this.weight = builder.weight;
        this.active = builder.active != null ? builder.active : true;
    }

    // Getters
    public RecommendationRuleId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRuleSet() {
        return ruleSet;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Double getWeight() {
        return weight;
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
        return java.util.Objects.equals(id, ((RecommendationRule) o).id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, ruleSet, algorithm, weight);
    }

    @Override
    public String toString() {
        return "RecommendationRule{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ruleSet='" + ruleSet + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", weight='" + weight + '\'' +
                ", active='" + active + '\'' +
                '}';
    }

    /**
     * Builder for RecommendationRule.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RecommendationRuleId id;
        private String name;
        private String ruleSet;
        private String algorithm;
        private Double weight;
        private Boolean active;

        public Builder id(RecommendationRuleId id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ruleSet(String ruleSet) {
            this.ruleSet = ruleSet;
            return this;
        }

        public Builder algorithm(String algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder weight(Double weight) {
            this.weight = weight;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public RecommendationRule build() {
            return new RecommendationRule(this);
        }
    }
}
