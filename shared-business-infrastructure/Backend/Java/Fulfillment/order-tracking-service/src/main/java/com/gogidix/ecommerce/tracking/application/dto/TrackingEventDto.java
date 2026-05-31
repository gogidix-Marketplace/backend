package com.gogidix.ecommerce.tracking.application.dto;

import java.time.Instant;
import java.util.List;

public record TrackingEventDto(
    String id, String tenantId, String shipmentId, String orderId,
    String carrier, String trackingNumber, List<TrackingUpdateDto> updates,
    String status, Instant createdAt, Instant updatedAt
) {}
