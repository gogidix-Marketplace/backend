package com.gogidix.corporatecms.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Domain model representing product catalog entries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Indexed(unique = true)
    private String sku;

    @Indexed
    private String slug;

    private String name;

    private String tagline;

    private String description;

    private String longDescription;

    private String featuredImageId;

    private List<String> galleryImageIds;

    private String demoVideoId;

    @Indexed
    private String categoryId;

    private String categoryName;

    private List<String> tags;

    private Map<String, Object> features;

    private Map<String, Object> specifications;

    private List<ProductVariant> variants;

    private List<ProductPricing> pricing;

    @Indexed
    private Boolean published;

    private LocalDateTime publishedAt;

    @Indexed
    private String tenantId;

    private String documentationLink;

    private String apiReferenceLink;

    private String supportLink;

    private String releaseNotesLink;

    private String productVersion;

    private LocalDateTime lastUpdatedAt;

    @Builder.Default
    private Map<String, BigDecimal> regionalPricing = new HashMap<>();

    @Builder.Default
    private List<String> compatibleProducts = new ArrayList<>();

    @Builder.Default
    private List<String> requiredProducts = new ArrayList<>();

    private Integer sortOrder;

    @Indexed
    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Version
    private Long version;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductVariant {
        private String id;
        private String name;
        private String sku;
        private Map<String, String> attributes;
        private BigDecimal price;
        private Boolean available;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPricing {
        private String planType;
        private String name;
        private BigDecimal price;
        private String billingCycle;
        private Map<String, Object> features;
        private Boolean popular;
    }
}
