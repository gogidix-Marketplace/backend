package com.gogidix.ecommerce.influencer.social.application.dto;

public record UpdateSocialIntegrationRequest(
    String name, String description, boolean active
) {}
