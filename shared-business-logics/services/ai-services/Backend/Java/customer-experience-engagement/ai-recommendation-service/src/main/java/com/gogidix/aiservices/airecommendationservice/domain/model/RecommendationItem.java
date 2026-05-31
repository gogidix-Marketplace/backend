package com.gogidix.aiservices.airecommendationservice.domain.model;

import lombok.Builder;

import java.util.Map;
import java.util.Objects;

@Builder
public class RecommendationItem {
    private final String itemId;
    private final String title;
    private final String category;
    private final double score;
    private final RecommendationReason reason;
    private final Map<String, Object> metadata;
    private final String imageUrl;
    private final Double price;

    public RecommendationItem(String itemId, String title, String category, double score,
                            RecommendationReason reason, Map<String, Object> metadata,
                            String imageUrl, Double price) {
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        if (score < 0.0 || score > 1.0) {
            throw new IllegalArgumentException("Score must be between 0 and 1");
        }
        this.itemId = itemId;
        this.title = title;
        this.category = category;
        this.score = score;
        this.reason = reason != null ? reason : RecommendationReason.CONTENT_BASED;
        this.metadata = metadata;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getItemId() { return itemId; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public double getScore() { return score; }
    public RecommendationReason getReason() { return reason; }
    public Map<String, Object> getMetadata() { return metadata; }
    public String getImageUrl() { return imageUrl; }
    public Double getPrice() { return price; }

    public boolean meetsThreshold(double threshold) {
        return score >= threshold;
    }

    public boolean matchesCategory(String category) {
        return this.category == null || this.category.equals(category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendationItem that = (RecommendationItem) o;
        return Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
