package com.gogidix.aiservices.aipersonalizationservice.application.dto.response;

import com.gogidix.aiservices.aipersonalizationservice.domain.model.Segment;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;

@Builder
public record ProfileResponse(
        String profileId,
        String userId,
        Segment segment,
        Map<String, Object> attributes,
        Map<String, Double> affinityScores,
        Instant createdAt,
        Instant updatedAt
) {}
