package com.gogidix.aiservices.airecommendationservice.domain.port.out;

import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationItem;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationResult;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationType;

import java.util.List;

public interface RecommendationEnginePort {
    RecommendationResult generateRecommendations(String userId, RecommendationType type,
                                                String context, int limit);

    List<RecommendationItem> getSimilarItems(String itemId, int limit);

    List<RecommendationItem> getTrendingItems(String category, int limit);

    void trackInteraction(String userId, String itemId, String interactionType);
}
