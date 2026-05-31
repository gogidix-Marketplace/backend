package com.gogidix.corporatecms.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for Product entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product DTO for managing product catalog")
public class ProductDTO {

    @Schema(description = "Product ID")
    private String id;

    @Schema(description = "Product SKU")
    @NotBlank(message = "SKU is required")
    private String sku;

    @Schema(description = "URL-friendly slug")
    @NotBlank(message = "Slug is required")
    private String slug;

    @Schema(description = "Product name")
    @NotBlank(message = "Product name is required")
    @Size(max = 500, message = "Product name must not exceed 500 characters")
    private String name;

    @Schema(description = "Product tagline")
    @Size(max = 200, message = "Tagline must not exceed 200 characters")
    private String tagline;

    @Schema(description = "Product description")
    private String description;

    @Schema(description = "Long description")
    private String longDescription;

    @Schema(description = "Featured image ID")
    private String featuredImageId;

    @Schema(description = "Gallery image IDs")
    private List<String> galleryImageIds;

    @Schema(description = "Demo video ID")
    private String demoVideoId;

    @Schema(description = "Category ID")
    private String categoryId;

    @Schema(description = "Category name")
    private String categoryName;

    @Schema(description = "Product tags")
    private List<String> tags;

    @Schema(description = "Product features")
    private Map<String, Object> features;

    @Schema(description = "Product specifications")
    private Map<String, Object> specifications;

    @Schema(description = "Product variants")
    private List<ProductVariantDTO> variants;

    @Schema(description = "Product pricing plans")
    private List<ProductPricingDTO> pricing;

    @Schema(description = "Is published")
    private Boolean published;

    @Schema(description = "Documentation link")
    private String documentationLink;

    @Schema(description = "API reference link")
    private String apiReferenceLink;

    @Schema(description = "Support link")
    private String supportLink;

    @Schema(description = "Release notes link")
    private String releaseNotesLink;

    @Schema(description = "Product version")
    private String version;

    @Schema(description = "Regional pricing")
    private Map<String, BigDecimal> regionalPricing;

    @Schema(description = "Compatible products")
    private List<String> compatibleProducts;

    @Schema(description = "Required products")
    private List<String> requiredProducts;

    @Schema(description = "Sort order")
    private Integer sortOrder;

    @Schema(description = "Published at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publishedAt;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductVariantDTO {
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
    public static class ProductPricingDTO {
        private String planType;
        private String name;
        private BigDecimal price;
        private String billingCycle;
        private Map<String, Object> features;
        private Boolean popular;
    }
}
