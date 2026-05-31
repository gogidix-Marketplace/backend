package com.gogidix.ecommerce.vendor.analytics.application.dto;

import java.time.Instant;

public record VendorAnalyticsResponse(String id, String tenantId, String status, Instant createdAt, Instant updatedAt) {}