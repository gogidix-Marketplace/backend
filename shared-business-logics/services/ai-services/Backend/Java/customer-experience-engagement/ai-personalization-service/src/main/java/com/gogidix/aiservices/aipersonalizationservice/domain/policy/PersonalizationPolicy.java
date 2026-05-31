package com.gogidix.aiservices.aipersonalizationservice.domain.policy;

import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class PersonalizationPolicy {
    private static final int INTERACTIONS_FOR_ACTIVE = 10;
    private static final int INTERACTIONS_FOR_VIP = 100;
    private static final int PURCHASES_FOR_VIP = 10;
    private static final Duration INACTIVE_THRESHOLD = Duration.ofDays(30);
    private static final Duration CHURNED_THRESHOLD = Duration.ofDays(90);
    private static final double MIN_SCORE_THRESHOLD = 0.3;
    private static final int MAX_RECOMMENDATIONS = 100;

    public Segment determineSegment(UserProfile profile) {
        int interactionCount = profile.getInteractionCount();
        long purchaseCount = profile.getPurchaseCount();

        Instant latestActivity = profile.getBehaviorHistory().isEmpty()
                ? profile.getCreatedAt()
                : profile.getBehaviorHistory().get(profile.getBehaviorHistory().size() - 1).getTimestamp();
        Duration timeSinceLastActivity = Duration.between(latestActivity, Instant.now());

        if (timeSinceLastActivity.compareTo(CHURNED_THRESHOLD) > 0 && interactionCount >= 10) {
            return Segment.CHURNED;
        } else if (timeSinceLastActivity.compareTo(INACTIVE_THRESHOLD) > 0 && interactionCount >= 5) {
            return Segment.INACTIVE;
        } else if (interactionCount >= INTERACTIONS_FOR_VIP && purchaseCount >= PURCHASES_FOR_VIP) {
            return Segment.VIP;
        } else if (interactionCount >= INTERACTIONS_FOR_ACTIVE) {
            return Segment.ACTIVE;
        } else {
            return Segment.NEW_USER;
        }
    }

    public Map<String, Double> calculateAffinityScores(UserProfile profile) {
        Map<String, Double> affinityScores = new HashMap<>();

        Map<EventType, Double> weights = new EnumMap<>(EventType.class);
        weights.put(EventType.PURCHASE, 5.0);
        weights.put(EventType.SHARE, 2.0);
        weights.put(EventType.LIKE, 1.5);
        weights.put(EventType.CLICK, 1.0);
        weights.put(EventType.VIEW, 0.5);
        weights.put(EventType.SEARCH, 1.0);

        Map<String, Double> rawScores = new HashMap<>();

        for (var event : profile.getBehaviorHistory()) {
            double weight = weights.getOrDefault(event.getEventType(), 1.0);

            double age = Duration.between(event.getTimestamp(), Instant.now()).toDays();
            double decay = Math.exp(-age / 30.0);
            double adjustedWeight = weight * decay;

            String category = event.getProperty("category") != null
                    ? event.getProperty("category").toString()
                    : event.getItemId();

            rawScores.merge(category, adjustedWeight, Double::sum);
        }

        double maxScore = rawScores.values().stream().max(Double::compare).orElse(1.0);
        if (maxScore > 0) {
            rawScores.forEach((key, value) -> {
                double normalized = Math.min(1.0, value / maxScore);
                affinityScores.put(key, normalized);
            });
        }

        return affinityScores;
    }

    public boolean isEligibleForRecommendations(UserProfile profile) {
        return profile.getInteractionCount() >= 5;
    }

    public boolean hasMinimumAffinity(UserProfile profile) {
        Map<String, Double> scores = calculateAffinityScores(profile);
        return !scores.isEmpty() && scores.values().stream().anyMatch(s -> s >= 0.3);
    }

    public List<Recommendation> filterByScoreThreshold(List<Recommendation> recommendations, double threshold) {
        return recommendations.stream()
                .filter(r -> r.meetsThreshold(threshold))
                .collect(Collectors.toList());
    }

    public List<Recommendation> filterByUserPreferences(List<Recommendation> recommendations, UserProfile profile) {
        if (profile.getAttributes() == null ||
            profile.getAttributes().getPreferences() == null ||
            profile.getAttributes().getPreferences().getCategories().isEmpty()) {
            return recommendations;
        }

        Set<String> preferredCategories = new HashSet<>(
                profile.getAttributes().getPreferences().getCategories());

        return recommendations.stream()
                .filter(r -> r.getCategory() == null || preferredCategories.contains(r.getCategory()))
                .collect(Collectors.toList());
    }

    public List<Recommendation> limitRecommendations(List<Recommendation> recommendations, int limit) {
        int actualLimit = Math.min(limit, MAX_RECOMMENDATIONS);
        return recommendations.stream()
                .sorted()
                .limit(actualLimit)
                .collect(Collectors.toList());
    }

    public List<Recommendation> applyDiversity(List<Recommendation> recommendations, int maxPerCategory) {
        Map<String, List<Recommendation>> byCategory = recommendations.stream()
                .collect(Collectors.groupingBy(r -> r.getCategory() != null ? r.getCategory() : "default"));

        List<Recommendation> diversified = new ArrayList<>();
        for (var entry : byCategory.entrySet()) {
            diversified.addAll(entry.getValue().stream()
                    .sorted()
                    .limit(maxPerCategory)
                    .toList());
        }

        return diversified.stream().sorted().toList();
    }

    public List<Recommendation> applyReasonDiversity(List<Recommendation> recommendations) {
        Map<RecommendationReason, List<Recommendation>> byReason = recommendations.stream()
                .collect(Collectors.groupingBy(Recommendation::getReason));

        List<Recommendation> diversified = new ArrayList<>();
        List<Queue<Recommendation>> queues = byReason.values().stream()
                .map(ArrayDeque::new)
                .collect(Collectors.toList());

        boolean hasElements = true;
        while (hasElements) {
            hasElements = false;
            for (var queue : queues) {
                if (!queue.isEmpty()) {
                    diversified.add(queue.poll());
                    hasElements = true;
                }
            }
        }

        return diversified;
    }

    public Recommendation applyFreshContentBoost(Recommendation recommendation) {
        return recommendation.withFreshContentBoost();
    }

    public Recommendation applyTrendingBoost(Recommendation recommendation) {
        return recommendation.withTrendingBoost();
    }

    public Recommendation applyContextualBoost(Recommendation recommendation, String context) {
        double boostFactor = switch (context) {
            case "search" -> 1.3;
            case "homepage" -> 1.1;
            default -> 1.0;
        };
        return recommendation.withBoost(boostFactor);
    }
}
