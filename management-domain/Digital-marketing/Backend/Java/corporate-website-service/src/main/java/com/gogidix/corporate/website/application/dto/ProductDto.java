package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.LocalizedContent;
import com.gogidix.corporate.website.domain.model.Product;
import com.gogidix.corporate.website.domain.model.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String id;
    private String productKey;
    private String slug;
    private LocalizedContent localizedContent;
    private Language language;
    private String productType;
    private String category;
    private List<ProductFeatureDto> features;
    private Set<Region> availableRegions;
    private String imageUrl;
    private String imageAlt;
    private List<String> gallery;
    private PricingInfoDto pricing;
    private boolean requiresContact;
    private String ctaText;
    private String ctaLink;
    private SeoMetadataDto seoMetadata;
    private ContentStatus status;
    private LocalDateTime launchDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> tags;
    private Integer sortOrder;
    private boolean featured;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductFeatureDto {
        private String icon;
        private String title;
        private String description;
        private boolean included;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PricingInfoDto {
        private BigDecimal basePrice;
        private String currency;
        private String billingCycle;
        private boolean displayPricing;
        private String startingFromText;
    }
}
