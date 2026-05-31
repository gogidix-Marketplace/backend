package com.gogidix.ecommerce.fulfillment.warehouse.application.dto;

import java.time.Instant;

public record WarehouseFulfillmentDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
