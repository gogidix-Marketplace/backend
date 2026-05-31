package com.gogidix.ecommerce.influencer.dashboard.application.dto;

public record InfluencerDashboardResponse(
    String id, String name, String description, boolean active
) {
    public static InfluencerDashboardResponse from(InfluencerDashboardDto dto) {
        return new InfluencerDashboardResponse(dto.id(), dto.name(), dto.description(), dto.active());
    }
}
