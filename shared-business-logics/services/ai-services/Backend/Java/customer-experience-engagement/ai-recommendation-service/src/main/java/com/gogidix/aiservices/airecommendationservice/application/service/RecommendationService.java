package com.gogidix.aiservices.airecommendationservice.application.service;

import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationItem;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationResult;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationType;
import com.gogidix.aiservices.airecommendationservice.domain.port.out.RecommendationEnginePort;
import com.gogidix.aiservices.airecommendationservice.domain.policy.RecommendationPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationEnginePort recommendationEngine;
    private final RecommendationPolicy policy;

    private static final int DEFAULT_LIMIT = 10;
    private static final double MIN_SCORE_THRESHOLD = 0.3;

    public RecommendationResult getRecommendations(String userId, RecommendationType type,
                                                   String context, Integer limit) {
        int actualLimit = limit != null ? Math.min(limit, policy.getMaxRecommendations()) : DEFAULT_LIMIT;

        RecommendationResult result = recommendationEngine.generateRecommendations(
                userId, type, context, actualLimit
        );

        result = applyFilters(result);

        return result;
    }

    public List<RecommendationItem> getSimilarItems(String itemId, Integer limit) {
        int actualLimit = limit != null ? limit : DEFAULT_LIMIT;
        return recommendationEngine.getSimilarItems(itemId, actualLimit);
    }

    public List<RecommendationItem> getTrendingItems(String category, Integer limit) {
        int actualLimit = limit != null ? limit : DEFAULT_LIMIT;
        return recommendationEngine.getTrendingItems(category, actualLimit);
    }

    public void trackInteraction(String userId, String itemId, String interactionType) {
        recommendationEngine.trackInteraction(userId, itemId, interactionType);
    }

    private RecommendationResult applyFilters(RecommendationResult result) {
        List<RecommendationItem> filteredItems = policy.filterByScore(result.getItems(), MIN_SCORE_THRESHOLD);
        filteredItems = policy.limitByCategory(filteredItems, 5);
        filteredItems = policy.applyDiversity(filteredItems);

        return RecommendationResult.builder()
                .requestId(result.getRequestId())
                .userId(result.getUserId())
                .type(result.getType())
                .items(filteredItems)
                .sessionId(result.getSessionId())
                .generatedAt(result.getGeneratedAt())
                .algorithm(result.getAlgorithm())
                .confidence(result.getConfidence())
                .build();
    }
}
