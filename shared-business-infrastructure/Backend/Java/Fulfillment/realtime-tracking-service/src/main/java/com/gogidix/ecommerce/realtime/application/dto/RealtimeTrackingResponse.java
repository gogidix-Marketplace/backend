package com.gogidix.ecommerce.realtime.application.dto;

public record RealtimeTrackingResponse(String id, String name, boolean active) {
    public static RealtimeTrackingResponse from(RealtimeTrackingDto dto) {
        return new RealtimeTrackingResponse(dto.id(), dto.name(), dto.active());
    }
}
