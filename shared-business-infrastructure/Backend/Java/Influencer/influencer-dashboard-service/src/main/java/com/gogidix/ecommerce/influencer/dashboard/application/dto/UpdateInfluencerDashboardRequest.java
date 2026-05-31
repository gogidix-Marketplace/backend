package com.gogidix.ecommerce.influencer.dashboard.application.dto;

public record UpdateInfluencerDashboardRequest(
    String name, String description, boolean active
) {}
