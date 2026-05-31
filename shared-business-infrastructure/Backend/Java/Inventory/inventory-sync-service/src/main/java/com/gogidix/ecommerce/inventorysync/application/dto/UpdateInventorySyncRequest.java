package com.gogidix.ecommerce.inventorysync.application.dto;

public record UpdateInventorySyncRequest(
    String name,
    String description,
    String type,
    String sourceSystem,
    String targetSystem,
    String syncStatus,
    Boolean isActive
) {}
