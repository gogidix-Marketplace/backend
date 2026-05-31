package com.gogidix.ecommerce.marketplace.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class ProductListingTest {

    private ProductListing createFull() {
        ProductListing p = new ProductListing();
        p.setId("id1"); p.setTenantId("t1"); p.setProductId("p1");
        p.setSku("sku1"); p.setName("Widget"); p.setDescription("A widget");
        p.setCategoryId("cat1"); p.setCategoryIds(List.of("cat1", "cat2"));
        p.setVendorId("v1"); p.setVendorName("Vendor1"); p.setVendorRating(4.5f);
        p.setPrice(BigDecimal.valueOf(19.99)); p.setCurrency("USD");
        p.setCompareAtPrice(BigDecimal.valueOf(29.99));
        p.setImageUrl("http://img"); p.setImageUrls(List.of("http://img1", "http://img2"));
        p.setInStock(true); p.setStockQuantity(100);
        p.setTags(List.of("new", "sale"));
        p.setIsActive(true); p.setRating(4.8f); p.setReviewCount(50); p.setSoldCount(200);
        p.setListingType(ProductListing.ListingType.FEATURED);
        p.setProductType(ProductListing.ProductType.PHYSICAL);
        return p;
    }

    @Test void allFields() {
        ProductListing p = createFull();
        assertThat(p.getId()).isEqualTo("id1");
        assertThat(p.getTenantId()).isEqualTo("t1");
        assertThat(p.getProductId()).isEqualTo("p1");
        assertThat(p.getSku()).isEqualTo("sku1");
        assertThat(p.getName()).isEqualTo("Widget");
        assertThat(p.getDescription()).isEqualTo("A widget");
        assertThat(p.getCategoryId()).isEqualTo("cat1");
        assertThat(p.getCategoryIds()).containsExactly("cat1", "cat2");
        assertThat(p.getVendorId()).isEqualTo("v1");
        assertThat(p.getVendorName()).isEqualTo("Vendor1");
        assertThat(p.getVendorRating()).isEqualTo(4.5f);
        assertThat(p.getPrice()).isEqualByComparingTo("19.99");
        assertThat(p.getCurrency()).isEqualTo("USD");
        assertThat(p.getCompareAtPrice()).isEqualByComparingTo("29.99");
        assertThat(p.getImageUrl()).isEqualTo("http://img");
        assertThat(p.getImageUrls()).containsExactly("http://img1", "http://img2");
        assertThat(p.getInStock()).isTrue();
        assertThat(p.getStockQuantity()).isEqualTo(100);
        assertThat(p.getTags()).containsExactly("new", "sale");
        assertThat(p.getIsActive()).isTrue();
        assertThat(p.getRating()).isEqualTo(4.8f);
        assertThat(p.getReviewCount()).isEqualTo(50);
        assertThat(p.getSoldCount()).isEqualTo(200);
        assertThat(p.getListingType()).isEqualTo(ProductListing.ListingType.FEATURED);
        assertThat(p.getProductType()).isEqualTo(ProductListing.ProductType.PHYSICAL);
    }

    @Test void nullDefaults() {
        ProductListing p = new ProductListing();
        assertThat(p.getId()).isNull();
        assertThat(p.getName()).isNull();
        assertThat(p.getPrice()).isNull();
        assertThat(p.getIsActive()).isNull();
        assertThat(p.getTags()).isNull();
    }

    @Test void listingTypeEnum() {
        assertThat(ProductListing.ListingType.values()).hasSize(4);
        assertThat(ProductListing.ListingType.valueOf("SPONSORED")).isEqualTo(ProductListing.ListingType.SPONSORED);
    }

    @Test void productTypeEnum() {
        assertThat(ProductListing.ProductType.values()).hasSize(3);
        assertThat(ProductListing.ProductType.valueOf("DIGITAL")).isEqualTo(ProductListing.ProductType.DIGITAL);
    }
}