package com.gogidix.aiservices.nlpprocessingservice.domain.aggregate;

import com.gogidix.aiservices.nlpprocessingservice.domain.model.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TextAnalysis {
    private static final int MAX_TEXT_LENGTH = 100000;
    private static final int MIN_SUMMARY_LENGTH_RATIO = 10; // 10%

    private final UUID analysisId;
    private final String text;
    private final LanguageCode detectedLanguage;
    private final List<Entity> entities;
    private final Map<String, Double> categoryScores;
    private final List<Keyword> keywords;
    private SentimentResult sentiment;
    private String summary;
    private final Instant createdAt;

    private TextAnalysis(String text, LanguageCode language) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
        if (text.length() > MAX_TEXT_LENGTH) {
            throw new IllegalArgumentException("Text length exceeds maximum of " + MAX_TEXT_LENGTH);
        }

        this.analysisId = UUID.randomUUID();
        this.text = text;
        this.detectedLanguage = language != null ? language : LanguageCode.detectFromText(text);
        this.entities = new ArrayList<>();
        this.categoryScores = new ConcurrentHashMap<>();
        this.keywords = new ArrayList<>();
        this.createdAt = Instant.now();
    }

    public static TextAnalysis create(String text, LanguageCode language) {
        return new TextAnalysis(text, language);
    }

    public static TextAnalysis create(String text) {
        return new TextAnalysis(text, LanguageCode.AUTO);
    }

    public void addEntity(Entity entity) {
        if (entity != null) {
            entities.add(entity);
        }
    }

    public void addEntities(List<Entity> entityList) {
        if (entityList != null) {
            entities.addAll(entityList);
        }
    }

    public void setCategoryScore(String category, double score) {
        categoryScores.put(category, Math.max(0, Math.min(1, score)));
    }

    public void setSentiment(SentimentResult sentiment) {
        this.sentiment = sentiment;
    }

    public void setSummary(String summary) {
        int minSummaryLength = Math.max(10, text.length() / MIN_SUMMARY_LENGTH_RATIO);
        if (summary.length() < minSummaryLength) {
            throw new IllegalArgumentException("Summary must be at least " + minSummaryLength + " characters");
        }
        this.summary = summary;
    }

    public void addKeyword(Keyword keyword) {
        if (keyword != null) {
            keywords.add(keyword);
        }
    }

    public void addKeywords(List<Keyword> keywordList) {
        if (keywordList != null) {
            keywords.addAll(keywordList);
        }
    }

    public List<Entity> getEntitiesByType(EntityType type) {
        return entities.stream()
                .filter(e -> e.type() == type)
                .collect(Collectors.toList());
    }

    public List<CategoryScore> getTopCategories(int limit) {
        return categoryScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(e -> new CategoryScore(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public boolean containsEntity(String entityText) {
        return entities.stream()
                .anyMatch(e -> e.entityText().equalsIgnoreCase(entityText));
    }

    // Getters
    public UUID getAnalysisId() { return analysisId; }
    public String getText() { return text; }
    public LanguageCode getDetectedLanguage() { return detectedLanguage; }
    public List<Entity> getEntities() { return Collections.unmodifiableList(entities); }
    public Map<String, Double> getCategoryScores() { return Collections.unmodifiableMap(categoryScores); }
    public List<Keyword> getKeywords() { return Collections.unmodifiableList(keywords); }
    public SentimentResult getSentiment() { return sentiment; }
    public String getSummary() { return summary; }
    public Instant getCreatedAt() { return createdAt; }
    public int getTextLength() { return text.length(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextAnalysis that = (TextAnalysis) o;
        return Objects.equals(analysisId, that.analysisId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(analysisId);
    }

    // Nested classes
    public record Entity(String entityText, EntityType type, int startPosition, int endPosition, double confidence) {}

    public record SentimentResult(SentimentLabel label, double score) {
        public SentimentResult {
            score = Math.max(-1, Math.min(1, score));
        }
    }

    public record Keyword(String text, double relevance) {}

    public record CategoryScore(String category, double score) {}
}
