package com.gogidix.ecommerce.marketplace.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "product_listings")
public class ProductListing {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String productId;
    private String sku;
    private String name;
    private String description;

    @Indexed
    private String categoryId;
    private List<String> categoryIds;

    private String vendorId;
    private String vendorName;
    private Float vendorRating;

    private BigDecimal price;
    private String currency;
    private BigDecimal compareAtPrice;

    private String imageUrl;
    private List<String> imageUrls;

    private Boolean inStock;
    private Integer stockQuantity;

    private List<String> tags;

    @Indexed
    private Boolean isActive;

    private Float rating;
    private Integer reviewCount;
    private Integer soldCount;

    private ListingType listingType;
    private ProductType productType;

    public enum ListingType {
        STANDARD, FEATURED, SPONSORED, MARKETPLACE
    }

    public enum ProductType {
        PHYSICAL, DIGITAL, SERVICE
    }
}
