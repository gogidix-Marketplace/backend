package com.gogidix.ecommerce.marketplace.interfaces.rest;

import com.gogidix.ecommerce.marketplace.domain.model.ProductListing;
import com.gogidix.ecommerce.marketplace.domain.service.MarketplaceSearchService;
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
import org.springframework.http.HttpStatus;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketplaceSearchControllerTest {
    @Mock private MarketplaceSearchService searchService;
    @InjectMocks private MarketplaceSearchController controller;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private Page<ProductListing> page() { return new PageImpl<>(List.of(new ProductListing())); }

    @Test void search() {
        when(searchService.searchListings(eq("t1"),eq("widget"),any())).thenReturn(page());
        assertThat(controller.search("widget", PageRequest.of(0,10)).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getByCategory() {
        when(searchService.getListingsByCategory(eq("t1"),eq("cat1"),any())).thenReturn(page());
        assertThat(controller.getByCategory("cat1",PageRequest.of(0,10)).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getByVendor() {
        when(searchService.getVendorListings(eq("v1"),any())).thenReturn(page());
        assertThat(controller.getByVendor("v1",PageRequest.of(0,10)).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}