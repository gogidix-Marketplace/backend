package com.gogidix.ecommerce.influencer.social.application.dto;

public record SocialIntegrationResponse(
    String id, String name, String description, boolean active
) {
    public static SocialIntegrationResponse from(SocialIntegrationDto dto) {
        return new SocialIntegrationResponse(dto.id(), dto.name(), dto.description(), dto.active());
    }
}
