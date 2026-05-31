package com.gogidix.ecommerce.marketplace.search.application.dto;
import java.time.Instant;

public record MarketplaceSearchDto(
    String id, String tenantId, String name, String description,
    boolean active, Instant createdAt, Instant updatedAt
) {}
