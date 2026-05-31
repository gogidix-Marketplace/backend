package com.gogidix.ecommerce.vendor.dropship.application.dto;

import java.time.Instant;

public record DropshipResponse(String id, String tenantId, String status, Instant createdAt, Instant updatedAt) {}