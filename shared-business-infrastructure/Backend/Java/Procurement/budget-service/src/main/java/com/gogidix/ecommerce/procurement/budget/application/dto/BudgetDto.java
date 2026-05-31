package com.gogidix.ecommerce.procurement.budget.application.dto;
import java.time.Instant;
public record BudgetDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
