package com.gogidix.ecommerce.procurement.requisition.application.dto;
import java.time.Instant;
public record RequisitionDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
