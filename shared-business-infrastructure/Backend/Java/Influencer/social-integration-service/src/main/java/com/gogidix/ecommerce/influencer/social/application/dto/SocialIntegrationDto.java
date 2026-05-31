package com.gogidix.ecommerce.influencer.social.application.dto;

import java.time.Instant;

public record SocialIntegrationDto(
    String id, String tenantId, String name, String description,
    boolean active, Instant createdAt, Instant updatedAt
) {}
