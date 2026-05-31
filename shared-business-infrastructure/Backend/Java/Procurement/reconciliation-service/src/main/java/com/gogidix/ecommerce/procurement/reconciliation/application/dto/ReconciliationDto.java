package com.gogidix.ecommerce.procurement.reconciliation.application.dto;
import java.time.Instant;
public record ReconciliationDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
