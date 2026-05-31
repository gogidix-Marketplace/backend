package com.gogidix.ecommerce.vendor.dashboard.application.dto;

import java.time.Instant;

public record VendorDashboardResponse(String id, String tenantId, String status, Instant createdAt, Instant updatedAt) {}