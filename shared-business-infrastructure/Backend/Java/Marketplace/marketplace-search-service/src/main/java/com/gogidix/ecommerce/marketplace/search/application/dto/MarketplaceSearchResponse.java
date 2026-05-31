package com.gogidix.ecommerce.marketplace.search.application.dto;
public record MarketplaceSearchResponse(
    String id, String name, String description, boolean active
) {
    public static MarketplaceSearchResponse from(MarketplaceSearchDto dto) {
        return new MarketplaceSearchResponse(dto.id(), dto.name(), dto.description(), dto.active());
    }
}
