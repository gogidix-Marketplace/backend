package com.gogidix.ecommerce.vendor.onboarding.application.dto;

import java.time.Instant;

public record OnboardingResponse(String id, String tenantId, String status, Instant createdAt, Instant updatedAt) {}