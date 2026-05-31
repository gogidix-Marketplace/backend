package com.gogidix.aiservices.airecommendationservice.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
public class RecommendationResult {
    private final String requestId;
    private final String userId;
    private final RecommendationType type;
    private final List<RecommendationItem> items;
    private final String sessionId;
    private final Instant generatedAt;
    private final String algorithm;
    private final Double confidence;

    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
