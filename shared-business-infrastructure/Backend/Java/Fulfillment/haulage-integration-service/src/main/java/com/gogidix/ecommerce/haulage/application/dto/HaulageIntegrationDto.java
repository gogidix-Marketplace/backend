package com.gogidix.ecommerce.haulage.application.dto;

import java.time.Instant;

public record HaulageIntegrationDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
