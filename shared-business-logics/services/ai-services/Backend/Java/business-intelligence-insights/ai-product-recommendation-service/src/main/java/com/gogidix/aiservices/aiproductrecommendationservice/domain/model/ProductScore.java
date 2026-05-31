package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.util.Map;
import java.util.Objects;

/**
 * Product with relevance score for recommendation.
 */
public class ProductScore {

    private ProductId product;
    private Double score;
    private String reason;
    private Map<String, Object> attributes;

    public ProductScore() {
        this.attributes = new java.util.HashMap<>();
    }

    public ProductScore(ProductId product, Double score, String reason, Map<String, Object> attributes) {
        this.product = product;
        this.score = score;
        this.reason = reason;
        this.attributes = attributes != null ? attributes : new java.util.HashMap<>();
    }

    public ProductScore(ProductId product, Double score, String reason) {
        this(product, score, reason, null);
    }

    // Getters
    public ProductId getProduct() {
        return product;
    }

    public Double getScore() {
        return score;
    }

    public String getReason() {
        return reason;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new java.util.HashMap<>();
        }
        this.attributes.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(product, ((ProductScore) o).product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, score, reason);
    }

    @Override
    public String toString() {
        return "ProductScore{" +
                "product='" + product + '\'' +
                ", score='" + score + '\'' +
                ", reason='" + reason + '\'' +
                ", attributes=" + attributes.size() + " attr" + '\'' +
                '}';
    }

    /**
     * Constructor for Builder.
     */
    private ProductScore(Builder builder) {
        this.product = builder.product;
        this.score = builder.score;
        this.reason = builder.reason;
        this.attributes = builder.attributes != null ? builder.attributes : new java.util.HashMap<>();
    }

    /**
     * Builder for ProductScore.
     */
    public static class Builder {
        private ProductId product;
        private Double score;
        private String reason;
        private Map<String, Object> attributes;

        public Builder product(ProductId product) {
            this.product = product;
            return this;
        }

        public Builder score(Double score) {
            this.score = score;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public ProductScore build() {
            return new ProductScore(this);
        }
    }

    /**
     * Static factory method for builder.
     */
    public static Builder builder() {
        return new Builder();
    }
}
