package com.gogidix.ecommerce.inventory.sync.application.dto;

public record UpdateInventorySyncRequest(
    String name, String description, boolean active
) {}
