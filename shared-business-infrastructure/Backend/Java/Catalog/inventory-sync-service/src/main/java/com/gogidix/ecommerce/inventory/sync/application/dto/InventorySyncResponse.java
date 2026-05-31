package com.gogidix.ecommerce.inventory.sync.application.dto;

public record InventorySyncResponse(
    String id, String name, String description, boolean active
) {
    public static InventorySyncResponse from(InventorySyncDto dto) {
        return new InventorySyncResponse(dto.id(), dto.name(), dto.description(), dto.active());
    }
}
