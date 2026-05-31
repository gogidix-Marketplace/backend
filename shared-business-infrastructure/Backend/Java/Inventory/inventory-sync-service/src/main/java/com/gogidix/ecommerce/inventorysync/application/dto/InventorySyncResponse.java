package com.gogidix.ecommerce.inventorysync.application.dto;

import java.time.Instant;

public record InventorySyncResponse(
    String id,
    String name,
    String description,
    String type,
    String sourceSystem,
    String targetSystem,
    String syncStatus,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
