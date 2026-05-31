package com.gogidix.ecommerce.marketplace.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class ProductListingLombokTest {
    private ProductListing createFull() {
        ProductListing p = new ProductListing();
        p.setId("id1"); p.setTenantId("t1"); p.setProductId("prod1"); p.setSku("SKU1");
        p.setName("Widget"); p.setDescription("A widget"); p.setCategoryId("cat1");
        p.setCategoryIds(List.of("cat1")); p.setVendorId("v1"); p.setVendorName("Vendor1");
        p.setVendorRating(4.5f); p.setPrice(BigDecimal.TEN); p.setCurrency("USD");
        p.setCompareAtPrice(BigDecimal.valueOf(20)); p.setImageUrl("img.png");
        p.setImageUrls(List.of("img.png")); p.setInStock(true); p.setStockQuantity(100);
        p.setTags(List.of("tag1")); p.setIsActive(true);
        p.setRating(4.5f); p.setReviewCount(10); p.setSoldCount(5);
        p.setListingType(ProductListing.ListingType.STANDARD);
        p.setProductType(ProductListing.ProductType.PHYSICAL);
        return p;
    }

    @Test void equals_same() { assertThat(createFull()).isEqualTo(createFull()); }
    @Test void equals_different() {
        ProductListing p1 = createFull(); ProductListing p2 = createFull(); p2.setName("Other");
        assertThat(p1).isNotEqualTo(p2);
    }
    @Test void equals_null() { assertThat(createFull()).isNotEqualTo(null); }
    @Test void hashCode_consistency() { assertThat(createFull().hashCode()).isEqualTo(createFull().hashCode()); }
    @Test void toString_notNull() { assertThat(createFull().toString()).contains("ProductListing"); }
    @Test void canEqual() { assertThat(createFull().canEqual(new ProductListing())).isTrue(); }
    @Test void canEqual_false() { assertThat(createFull().canEqual("str")).isFalse(); }
    @Test void equals_self() { ProductListing p = createFull(); assertThat(p).isEqualTo(p); }
}