package com.gogidix.aiservices.aipersonalizationservice.application.dto.response;

import lombok.Builder;

@Builder
public record BehaviorTrackingResponse(
        int eventsProcessed,
        boolean profileUpdated
) {}
