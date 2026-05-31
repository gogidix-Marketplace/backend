package com.gogidix.aiservices.aipersonalizationservice.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.Recommendation;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.RecommendationReason;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.Segment;
import com.gogidix.aiservices.aipersonalizationservice.domain.port.out.RecommendationEnginePort;
import com.gogidix.aiservices.aipersonalizationservice.infrastructure.config.AiServiceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiInferenceAdapter implements RecommendationEnginePort {

    private final RestTemplate restTemplate;
    private final AiServiceProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    public List<Recommendation> generateRecommendations(UserProfile profile, RecommendationContext context) {
        try {
            String url = properties.getBaseUrl() + properties.getRecommendationEndpoint();

            Map<String, Object> request = buildRequest(profile, context);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response == null || !response.containsKey("recommendations")) {
                return List.of();
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> recList = (List<Map<String, Object>>) response.get("recommendations");

            return recList.stream()
                    .map(this::mapToRecommendation)
                    .collect(Collectors.toList());

        } catch (RestClientException e) {
            log.error("Failed to get recommendations from AI service: {}", e.getMessage());
            throw new RuntimeException("AI inference service unavailable", e);
        }
    }

    @Override
    public Map<String, Double> calculateAffinityScores(UserProfile profile) {
        try {
            String url = properties.getBaseUrl() + properties.getAffinityEndpoint();

            Map<String, Object> request = Map.of(
                    "userId", profile.getUserId().toString(),
                    "behaviors", profile.getBehaviorHistory().stream()
                            .map(b -> Map.of(
                                    "eventType", b.getEventType().toString(),
                                    "itemId", b.getItemId(),
                                    "timestamp", b.getTimestamp().toString(),
                                    "properties", b.getProperties()
                            )).toList()
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response == null || !response.containsKey("affinityScores")) {
                return Map.of();
            }

            @SuppressWarnings("unchecked")
            Map<String, Double> scores = (Map<String, Double>) response.get("affinityScores");

            return normalizeScores(scores);

        } catch (RestClientException e) {
            log.error("Failed to calculate affinity scores: {}", e.getMessage());
            return Map.of();
        }
    }

    @Override
    public Segment predictSegment(UserProfile profile) {
        try {
            String url = properties.getBaseUrl() + properties.getSegmentEndpoint();

            Map<String, Object> request = Map.of(
                    "userId", profile.getUserId().toString(),
                    "interactionCount", profile.getInteractionCount(),
                    "purchaseCount", profile.getPurchaseCount()
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response == null || !response.containsKey("predictedSegment")) {
                return Segment.NEW_USER;
            }

            @SuppressWarnings("unchecked")
            Double confidence = (Double) response.getOrDefault("confidence", 0.0);

            if (confidence < 0.7) {
                return Segment.NEW_USER;
            }

            String predictedSegment = (String) response.get("predictedSegment");
            return Segment.fromString(predictedSegment);

        } catch (RestClientException | IllegalArgumentException e) {
            log.error("Failed to predict segment: {}", e.getMessage());
            return Segment.NEW_USER;
        }
    }

    private Map<String, Object> buildRequest(UserProfile profile, RecommendationContext context) {
        return Map.of(
                "userId", profile.getUserId().toString(),
                "segment", profile.getSegment().toString(),
                "limit", context.limit(),
                "type", context.type(),
                "context", context.context(),
                "affinityScores", profile.getAffinityScores(),
                "behaviors", profile.getBehaviorHistory().stream()
                        .map(b -> Map.of(
                                "eventType", b.getEventType().toString(),
                                "itemId", b.getItemId(),
                                "timestamp", b.getTimestamp().toString()
                        )).toList()
        );
    }

    private Recommendation mapToRecommendation(Map<String, Object> map) {
        String itemId = (String) map.get("itemId");
        double score = ((Number) map.getOrDefault("score", 0.0)).doubleValue();
        String reasonStr = (String) map.get("reason");
        String category = (String) map.get("category");

        RecommendationReason reason;
        try {
            reason = RecommendationReason.fromString(reasonStr);
        } catch (IllegalArgumentException e) {
            reason = RecommendationReason.CONTEXTUAL;
        }

        return Recommendation.builder()
                .itemId(itemId)
                .score(score)
                .reason(reason)
                .category(category)
                .build();
    }

    private Map<String, Double> normalizeScores(Map<String, Double> scores) {
        double max = scores.values().stream().max(Double::compare).orElse(1.0);
        if (max <= 0) {
            return scores.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> 0.0
                    ));
        }

        Map<String, Double> normalized = new HashMap<>();
        for (var entry : scores.entrySet()) {
            double value = entry.getValue();
            double normValue = Math.max(0.0, Math.min(1.0, value / max));
            normalized.put(entry.getKey(), normValue);
        }
        return normalized;
    }
}
