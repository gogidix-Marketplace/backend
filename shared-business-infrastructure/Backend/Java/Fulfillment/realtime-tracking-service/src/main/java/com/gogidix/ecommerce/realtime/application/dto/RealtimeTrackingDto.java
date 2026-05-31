package com.gogidix.ecommerce.realtime.application.dto;

import java.time.Instant;

public record RealtimeTrackingDto(String id, String tenantId, String name, boolean active, Instant createdAt, Instant updatedAt) {}
