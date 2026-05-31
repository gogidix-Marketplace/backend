package com.gogidix.ecommerce.marketplace.domain.service;

import com.gogidix.ecommerce.marketplace.domain.model.ProductListing;
import com.gogidix.ecommerce.marketplace.domain.repository.ProductListingRepository;
import com.gogidix.ecommerce.marketplace.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.marketplace.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketplaceSearchServiceTest {
    @Mock private ProductListingRepository listingRepository;
    @InjectMocks private MarketplaceSearchService service;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private ProductListing createListing() {
        ProductListing p = new ProductListing();
        p.setId("id1"); p.setTenantId("t1"); p.setName("Widget"); p.setIsActive(true);
        return p;
    }

    @Test void searchListings() {
        Page<ProductListing> page = new PageImpl<>(List.of(createListing()));
        when(listingRepository.search("t1", "widget", PageRequest.of(0, 10))).thenReturn(page);
        Page<ProductListing> result = service.searchListings("t1", "widget", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
    }
    @Test void getListingsByCategory() {
        Page<ProductListing> page = new PageImpl<>(List.of(createListing()));
        when(listingRepository.findByTenantIdAndCategoryIdAndIsActive("t1", "cat1", true, PageRequest.of(0, 10))).thenReturn(page);
        Page<ProductListing> result = service.getListingsByCategory("t1", "cat1", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
    }
    @Test void getListingsByPriceRange() {
        Page<ProductListing> page = new PageImpl<>(List.of(createListing()));
        when(listingRepository.findByPriceRange("t1", BigDecimal.TEN, BigDecimal.valueOf(100), PageRequest.of(0, 10))).thenReturn(page);
        Page<ProductListing> result = service.getListingsByPriceRange("t1", BigDecimal.TEN, BigDecimal.valueOf(100), PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
    }
    @Test void getVendorListings() {
        Page<ProductListing> page = new PageImpl<>(List.of(createListing()));
        when(listingRepository.findByVendorIdAndIsActive("v1", true, PageRequest.of(0, 10))).thenReturn(page);
        Page<ProductListing> result = service.getVendorListings("v1", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
    }
}