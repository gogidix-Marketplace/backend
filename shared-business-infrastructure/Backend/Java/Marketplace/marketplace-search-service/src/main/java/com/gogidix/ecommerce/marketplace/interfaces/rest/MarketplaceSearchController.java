package com.gogidix.ecommerce.marketplace.interfaces.rest;

import com.gogidix.ecommerce.marketplace.domain.model.ProductListing;
import com.gogidix.ecommerce.marketplace.domain.service.MarketplaceSearchService;
import com.gogidix.ecommerce.marketplace.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/marketplace/search")
@RequiredArgsConstructor
@Tag(name = "Marketplace Search", description = "APIs for searching marketplace listings")
public class MarketplaceSearchController {

    private final MarketplaceSearchService searchService;

    @GetMapping
    @Operation(summary = "Search marketplace listings", description = "Search for product listings in the marketplace")
    public ResponseEntity<Page<ProductListing>> search(
            @Parameter(description = "Search query") @RequestParam String q,
            Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();
        return ResponseEntity.ok(searchService.searchListings(tenantId, q, pageable));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get listings by category", description = "Retrieve product listings by category")
    public ResponseEntity<Page<ProductListing>> getByCategory(
            @Parameter(description = "Category ID") @PathVariable String categoryId,
            Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();
        return ResponseEntity.ok(searchService.getListingsByCategory(tenantId, categoryId, pageable));
    }

    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get listings by vendor", description = "Retrieve product listings by vendor")
    public ResponseEntity<Page<ProductListing>> getByVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            Pageable pageable) {
        return ResponseEntity.ok(searchService.getVendorListings(vendorId, pageable));
    }
}
