package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Result of product recommendation engine execution.
 */
public class RecommendationResult {

    private RecommendationResultId id;
    private String customerId;
    private String tenantId;
    private List<ProductScore> rankedProducts;
    private Instant executedAt;
    private String algorithmVersion;

    public RecommendationResult() {
        this.id = RecommendationResultId.randomUUID();
        this.rankedProducts = new ArrayList<>();
        this.executedAt = Instant.now();
    }

    public RecommendationResult(RecommendationResultId id, String customerId, String tenantId, List<ProductScore> rankedProducts, Instant executedAt, String algorithmVersion) {
        this.id = id;
        this.customerId = customerId;
        this.tenantId = tenantId;
        this.rankedProducts = rankedProducts;
        this.executedAt = executedAt;
        this.algorithmVersion = algorithmVersion;
    }

    public RecommendationResultId getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public List<ProductScore> getRankedProducts() {
        return rankedProducts;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public String getAlgorithmVersion() {
        return algorithmVersion;
    }

    public void setRankedProducts(List<ProductScore> rankedProducts) {
        this.rankedProducts = rankedProducts;
    }

    public void setAlgorithmVersion(String algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return java.util.Objects.equals(id, ((RecommendationResult) o).id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, customerId, tenantId);
    }

    @Override
    public String toString() {
        return "RecommendationResult{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", products=" + rankedProducts.size() + '\'' +
                ", executedAt='" + executedAt + '\'' +
                ", algorithm='" + algorithmVersion + '\'' +
                '}';
    }

    /**
     * Constructor for Builder.
     */
    private RecommendationResult(Builder builder) {
        this.id = builder.id != null ? builder.id : RecommendationResultId.randomUUID();
        this.customerId = builder.customerId;
        this.tenantId = builder.tenantId;
        this.rankedProducts = builder.rankedProducts != null ? builder.rankedProducts : new ArrayList<>();
        this.executedAt = builder.executedAt != null ? builder.executedAt : Instant.now();
        this.algorithmVersion = builder.algorithmVersion;
    }

    /**
     * Builder for RecommendationResult.
     */
    public static class Builder {
        private RecommendationResultId id;
        private String customerId;
        private String tenantId;
        private List<ProductScore> rankedProducts;
        private Instant executedAt;
        private String algorithmVersion;

        public Builder id(RecommendationResultId id) {
            this.id = id;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder rankedProducts(List<ProductScore> rankedProducts) {
            this.rankedProducts = rankedProducts;
            return this;
        }

        public Builder executedAt(Instant executedAt) {
            this.executedAt = executedAt;
            return this;
        }

        public Builder algorithmVersion(String algorithmVersion) {
            this.algorithmVersion = algorithmVersion;
            return this;
        }

        public RecommendationResult build() {
            return new RecommendationResult(this);
        }
    }

    /**
     * Static factory method for builder.
     */
    public static Builder builder() {
        return new Builder();
    }
}
