package com.gogidix.ecommerce.inventory.sync.application.dto;

import java.time.Instant;

public record InventorySyncDto(
    String id, String tenantId, String name, String description,
    boolean active, Instant createdAt, Instant updatedAt
) {}
