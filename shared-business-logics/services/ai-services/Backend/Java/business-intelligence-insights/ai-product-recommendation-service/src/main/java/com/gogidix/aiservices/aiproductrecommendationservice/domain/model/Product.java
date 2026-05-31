package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Product entity representing a catalog item.
 */
public class Product {

    private ProductId id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer stockQuantity;
    private Boolean available;
    private String imageUrl;
    private String tenantId;

    // Private constructor for Builder
    private Product() {
    }

    public Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.category = builder.category;
        this.price = builder.price;
        this.stockQuantity = builder.stockQuantity;
        this.available = builder.available;
        this.imageUrl = builder.imageUrl;
        this.tenantId = builder.tenantId;
    }

    // Getters
    public ProductId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public Boolean isAvailable() {
        return available;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTenantId() {
        return tenantId;
    }

    // Business methods
    public boolean isInStock() {
        return available != null && available && stockQuantity != null && stockQuantity > 0;
    }

    public boolean isOutOfStock() {
        return available != null && available && stockQuantity != null && stockQuantity == 0;
    }

    public boolean isEligibleForRecommendation(RecommendationContext context) {
        return isInStock() && (context.getExcludedCategories() == null
                || !context.getExcludedCategories().contains(category));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Product) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }

    /**
     * Builder for Product.
     */
    public static class Builder {
        private ProductId id;
        private String name;
        private String description;
        private String category;
        private BigDecimal price;
        private Integer stockQuantity;
        private Boolean available;
        private String imageUrl;
        private String tenantId;

        public Builder id(ProductId id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder stockQuantity(Integer stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public Builder available(Boolean available) {
            this.available = available;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    /**
     * Static factory method for builder.
     */
    public static Builder builder() {
        return new Builder();
    }
}
