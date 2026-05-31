package com.gogidix.ecommerce.marketplace.domain.service;
import com.gogidix.ecommerce.marketplace.shared.requestcontext.RequestContextHolder;

import com.gogidix.ecommerce.marketplace.domain.model.ProductListing;
import com.gogidix.ecommerce.marketplace.domain.repository.ProductListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MarketplaceSearchService {

    private final ProductListingRepository listingRepository;

    public Page<ProductListing> searchListings(String tenantId, String query, Pageable pageable) {
        return listingRepository.search(tenantId, query, pageable);
    }

    public Page<ProductListing> getListingsByCategory(String tenantId, String categoryId, Pageable pageable) {
        return listingRepository.findByTenantIdAndCategoryIdAndIsActive(tenantId, categoryId, true, pageable);
    }

    public Page<ProductListing> getListingsByPriceRange(String tenantId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return listingRepository.findByPriceRange(tenantId, minPrice, maxPrice, pageable);
    }

    public Page<ProductListing> getVendorListings(String vendorId, Pageable pageable) {
        return listingRepository.findByVendorIdAndIsActive(vendorId, true, pageable);
    }
}
