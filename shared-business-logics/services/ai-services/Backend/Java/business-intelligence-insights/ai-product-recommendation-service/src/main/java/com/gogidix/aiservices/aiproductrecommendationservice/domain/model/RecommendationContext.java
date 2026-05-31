package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Context for recommendation requests.
 */
public class RecommendationContext {

    private String customerId;
    private String tenantId;
    private RecommendationType type;
    private Integer maxResults;
    private List<String> excludedCategories;
    private Boolean includeOutOfStock;

    public RecommendationContext() {
        this.maxResults = 10;
        this.excludedCategories = new ArrayList<>();
        this.includeOutOfStock = true;
    }

    public RecommendationContext(String customerId, String tenantId, RecommendationType type) {
        this.customerId = customerId;
        this.tenantId = tenantId;
        this.type = type;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public RecommendationType getType() {
        return type;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public List<String> getExcludedCategories() {
        return excludedCategories;
    }

    public void setExcludedCategories(List<String> categories) {
        this.excludedCategories = categories;
    }

    public Boolean isIncludeOutOfStock() {
        return includeOutOfStock;
    }

    public void setIncludeOutOfStock(Boolean includeOutOfStock) {
        this.includeOutOfStock = includeOutOfStock;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setType(RecommendationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RecommendationContext{" +
                "customerId='" + customerId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", type='" + type + '\'' +
                ", maxResults='" + maxResults + '\'' +
                ", excludedCategories=" + (excludedCategories != null ? excludedCategories.size() : 0) + " categories" + '\'' +
                ", includeOutOfStock=" + includeOutOfStock + '\'' +
                '}';
    }

    /**
     * Private constructor for Builder.
     */
    private RecommendationContext(Builder builder) {
        this.customerId = builder.customerId;
        this.tenantId = builder.tenantId;
        this.type = builder.type;
        this.maxResults = builder.maxResults != null ? builder.maxResults : 10;
        this.excludedCategories = builder.excludedCategories != null ? builder.excludedCategories : new ArrayList<>();
        this.includeOutOfStock = builder.includeOutOfStock != null ? builder.includeOutOfStock : true;
    }

    /**
     * Builder for RecommendationContext.
     */
    public static class Builder {
        private String customerId;
        private String tenantId;
        private RecommendationType type;
        private Integer maxResults;
        private List<String> excludedCategories;
        private Boolean includeOutOfStock;

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder type(RecommendationType type) {
            this.type = type;
            return this;
        }

        public Builder maxResults(Integer maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public Builder excludedCategories(List<String> categories) {
            this.excludedCategories = categories;
            return this;
        }

        public Builder includeOutOfStock(Boolean includeOutOfStock) {
            this.includeOutOfStock = includeOutOfStock;
            return this;
        }

        public RecommendationContext build() {
            return new RecommendationContext(this);
        }
    }

    /**
     * Static factory method for builder.
     */
    public static Builder builder() {
        return new Builder();
    }
}
