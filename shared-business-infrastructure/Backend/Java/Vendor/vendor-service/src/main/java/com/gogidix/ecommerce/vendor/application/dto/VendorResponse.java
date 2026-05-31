package com.gogidix.ecommerce.vendor.application.dto;

import java.time.Instant;

public record VendorResponse(String id, String tenantId, String status, Instant createdAt, Instant updatedAt) {}