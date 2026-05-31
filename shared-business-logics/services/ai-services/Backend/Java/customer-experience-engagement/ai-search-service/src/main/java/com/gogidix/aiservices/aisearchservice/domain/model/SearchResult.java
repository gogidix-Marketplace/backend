package com.gogidix.aiservices.aisearchservice.domain.model;

import lombok.Builder;

import java.util.Map;

@Builder
public class SearchResult {
    private final String itemId;
    private final String title;
    private final String description;
    private final double relevanceScore;
    private final String category;
    private final String url;
    private final String imageUrl;
    private final Map<String, Object> highlights;
    private final String source;

    public String getItemId() { return itemId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getRelevanceScore() { return relevanceScore; }
    public String getCategory() { return category; }
    public String getUrl() { return url; }
    public String getImageUrl() { return imageUrl; }
    public Map<String, Object> getHighlights() { return highlights; }
    public String getSource() { return source; }
}
