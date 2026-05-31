package com.gogidix.ecommerce.marketplace.search.application.dto;
public record UpdateMarketplaceSearchRequest(
    String name, String description, boolean active
) {}
