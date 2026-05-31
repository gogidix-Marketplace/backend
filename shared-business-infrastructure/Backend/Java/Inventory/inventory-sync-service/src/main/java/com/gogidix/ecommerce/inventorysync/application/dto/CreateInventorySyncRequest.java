package com.gogidix.ecommerce.inventorysync.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateInventorySyncRequest(
    @NotBlank String name,
    String description,
    String type,
    String sourceSystem,
    String targetSystem,
    String syncStatus
) {}
