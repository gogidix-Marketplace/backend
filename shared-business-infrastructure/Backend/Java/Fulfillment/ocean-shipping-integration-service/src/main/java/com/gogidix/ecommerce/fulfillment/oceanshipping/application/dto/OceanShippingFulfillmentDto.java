package com.gogidix.ecommerce.fulfillment.oceanshipping.application.dto;

import java.time.Instant;

public record OceanShippingFulfillmentDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
