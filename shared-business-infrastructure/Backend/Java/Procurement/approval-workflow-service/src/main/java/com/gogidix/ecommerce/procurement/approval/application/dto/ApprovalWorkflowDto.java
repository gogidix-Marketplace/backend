package com.gogidix.ecommerce.procurement.approval.application.dto;
import java.time.Instant;
public record ApprovalWorkflowDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
