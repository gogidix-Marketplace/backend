package com.gogidix.aiservices.airecommendationservice.domain.policy;

import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationItem;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationResult;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationType;

import java.util.List;
import java.util.stream.Collectors;

public class RecommendationPolicy {

    private static final int MAX_RECOMMENDATIONS = 100;
    private static final double DEFAULT_MIN_SCORE = 0.3;
    private static final int DIVERSITY_MAX_PER_CATEGORY = 5;

    public List<RecommendationItem> filterByScore(List<RecommendationItem> items, double minScore) {
        return items.stream()
                .filter(item -> item.meetsThreshold(minScore))
                .collect(Collectors.toList());
    }

    public List<RecommendationItem> applyDiversity(List<RecommendationItem> items) {
        return items.stream()
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(MAX_RECOMMENDATIONS)
                .collect(Collectors.toList());
    }

    public List<RecommendationItem> limitByCategory(List<RecommendationItem> items, int maxPerCategory) {
        return items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getCategory() != null ? item.getCategory() : "default"
                ))
                .values()
                .stream()
                .flatMap(categoryItems -> categoryItems.stream()
                        .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                        .limit(maxPerCategory))
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());
    }

    public boolean isValidRecommendationType(RecommendationType type) {
        return type != null;
    }

    public int getMaxRecommendations() {
        return MAX_RECOMMENDATIONS;
    }

    public RecommendationResult deduplicateResults(RecommendationResult result) {
        List<RecommendationItem> uniqueItems = result.getItems().stream()
                .distinct()
                .collect(Collectors.toList());

        return RecommendationResult.builder()
                .requestId(result.getRequestId())
                .userId(result.getUserId())
                .type(result.getType())
                .items(uniqueItems)
                .sessionId(result.getSessionId())
                .generatedAt(result.getGeneratedAt())
                .algorithm(result.getAlgorithm())
                .confidence(result.getConfidence())
                .build();
    }
}
