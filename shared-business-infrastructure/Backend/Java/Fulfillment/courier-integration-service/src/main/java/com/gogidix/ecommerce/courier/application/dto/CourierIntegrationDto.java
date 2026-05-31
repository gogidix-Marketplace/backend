package com.gogidix.ecommerce.courier.application.dto;

import java.time.Instant;

public record CourierIntegrationDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
