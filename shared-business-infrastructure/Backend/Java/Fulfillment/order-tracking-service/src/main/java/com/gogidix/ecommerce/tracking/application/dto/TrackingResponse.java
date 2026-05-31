package com.gogidix.ecommerce.tracking.application.dto;

import java.util.List;

public record TrackingResponse(
    String trackingNumber, String carrier, String status,
    List<TrackingUpdateDto> updates
) {
    public static TrackingResponse from(TrackingEventDto dto) {
        return new TrackingResponse(dto.trackingNumber(), dto.carrier(), dto.status(), dto.updates());
    }
}
