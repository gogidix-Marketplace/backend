package com.gogidix.ecommerce.orchestrator.application.dto;

import java.time.Instant;

public record FulfillmentOrchestratorDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
