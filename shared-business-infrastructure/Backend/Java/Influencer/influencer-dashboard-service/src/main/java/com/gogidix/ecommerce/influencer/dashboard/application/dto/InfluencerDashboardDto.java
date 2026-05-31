package com.gogidix.ecommerce.influencer.dashboard.application.dto;

import java.time.Instant;

public record InfluencerDashboardDto(
    String id, String tenantId, String name, String description,
    boolean active, Instant createdAt, Instant updatedAt
) {}
