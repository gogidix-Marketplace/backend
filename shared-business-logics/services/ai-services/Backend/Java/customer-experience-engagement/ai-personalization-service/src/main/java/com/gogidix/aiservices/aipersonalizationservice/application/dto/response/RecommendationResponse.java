package com.gogidix.aiservices.aipersonalizationservice.application.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record RecommendationResponse(
        String userId,
        List<ItemRecommendation> recommendations,
        Metadata metadata
) {
    public record ItemRecommendation(
            String itemId,
            double score,
            String reason,
            String category
    ) {}

    public record Metadata(
            String algorithm,
            Instant generatedAt
    ) {}
}
