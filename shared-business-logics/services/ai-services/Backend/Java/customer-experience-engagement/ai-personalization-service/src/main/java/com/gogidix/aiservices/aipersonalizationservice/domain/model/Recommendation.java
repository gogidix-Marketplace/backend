package com.gogidix.aiservices.aipersonalizationservice.domain.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Builder
public class Recommendation implements Comparable<Recommendation> {
    private final String itemId;
    private double score;
    private final RecommendationReason reason;
    private final String category;

    public Recommendation(String itemId, double score, RecommendationReason reason, String category) {
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        if (score < 0.0 || score > 1.0) {
            throw new IllegalArgumentException("Score must be between 0 and 1");
        }
        if (reason == null) {
            throw new IllegalArgumentException("Reason cannot be null");
        }
        this.itemId = itemId;
        this.score = BigDecimal.valueOf(score).setScale(4, RoundingMode.HALF_UP).doubleValue();
        this.reason = reason;
        this.category = category;
    }

    public String getItemId() {
        return itemId;
    }

    public double getScore() {
        return score;
    }

    public RecommendationReason getReason() {
        return reason;
    }

    public String getCategory() {
        return category;
    }

    public ScoreTier getScoreTier() {
        if (score >= 0.7) return ScoreTier.HIGH;
        if (score >= 0.4) return ScoreTier.MEDIUM;
        return ScoreTier.LOW;
    }

    public boolean meetsThreshold(double threshold) {
        return score >= threshold;
    }

    public boolean matchesCategory(String category) {
        return this.category == null || this.category.equals(category);
    }

    public boolean hasReason(RecommendationReason reason) {
        return this.reason == reason;
    }

    public Recommendation withBoost(double boostFactor) {
        double newScore = Math.min(1.0, this.score * boostFactor);
        return new Recommendation(itemId, newScore, reason, category);
    }

    public Recommendation withFreshContentBoost() {
        return withBoost(1.2);
    }

    public Recommendation withTrendingBoost() {
        return withBoost(1.15);
    }

    @Override
    public int compareTo(Recommendation other) {
        int scoreCompare = Double.compare(other.score, this.score);
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        return this.itemId.compareTo(other.itemId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
