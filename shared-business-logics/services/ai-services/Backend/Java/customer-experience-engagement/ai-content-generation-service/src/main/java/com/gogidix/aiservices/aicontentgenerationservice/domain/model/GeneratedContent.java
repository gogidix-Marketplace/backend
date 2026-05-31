package com.gogidix.aiservices.aicontentgenerationservice.domain.model;

import lombok.Builder;

import java.time.Instant;
import java.util.Objects;

@Builder
public class GeneratedContent {
    private final String contentId;
    private String content;
    private final ContentType contentType;
    private final ContentTone tone;
    private final String prompt;
    private final ContentQuality quality;
    private GenerationStatus status;
    private String errorMessage;
    private final Instant createdAt;
    private Instant completedAt;
    private final Integer wordCount;
    private final Double relevanceScore;
    private final String language;

    public GeneratedContent(String contentId, String content, ContentType contentType, ContentTone tone,
                           String prompt, ContentQuality quality, GenerationStatus status,
                           String errorMessage, Instant createdAt, Instant completedAt,
                           Integer wordCount, Double relevanceScore, String language) {
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Content ID cannot be null or empty");
        }
        if (contentType == null) {
            throw new IllegalArgumentException("Content type cannot be null");
        }
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be null or empty");
        }
        this.contentId = contentId;
        this.content = content;
        this.contentType = contentType;
        this.tone = tone;
        this.prompt = prompt;
        this.quality = quality != null ? quality : ContentQuality.MEDIUM;
        this.status = status != null ? status : GenerationStatus.PENDING;
        this.errorMessage = errorMessage;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.completedAt = completedAt;
        this.wordCount = wordCount;
        this.relevanceScore = relevanceScore;
        this.language = language;
    }

    public String getContentId() {
        return contentId;
    }

    public String getContent() {
        return content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public ContentTone getTone() {
        return tone;
    }

    public String getPrompt() {
        return prompt;
    }

    public ContentQuality getQuality() {
        return quality;
    }

    public GenerationStatus getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public Double getRelevanceScore() {
        return relevanceScore;
    }

    public String getLanguage() {
        return language;
    }

    public void markAsCompleted(String generatedContent) {
        this.content = generatedContent;
        this.status = GenerationStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    public void markAsFailed(String error) {
        this.status = GenerationStatus.FAILED;
        this.errorMessage = error;
        this.completedAt = Instant.now();
    }

    public void markAsProcessing() {
        this.status = GenerationStatus.PROCESSING;
    }

    public boolean isCompleted() {
        return status == GenerationStatus.COMPLETED;
    }

    public boolean isFailed() {
        return status == GenerationStatus.FAILED;
    }

    public boolean isProcessing() {
        return status == GenerationStatus.PROCESSING;
    }

    public boolean isPending() {
        return status == GenerationStatus.PENDING;
    }

    public boolean meetsQualityThreshold(ContentQuality threshold) {
        return quality.ordinal() >= threshold.ordinal();
    }

    public boolean hasRelevanceScore() {
        return relevanceScore != null && relevanceScore > 0;
    }

    public boolean meetsRelevanceThreshold(double threshold) {
        return relevanceScore != null && relevanceScore >= threshold;
    }

    public GeneratedContent withContent(String newContent) {
        return new GeneratedContent(
                this.contentId, newContent, this.contentType, this.tone,
                this.prompt, this.quality, this.status, this.errorMessage,
                this.createdAt, this.completedAt, this.wordCount, this.relevanceScore, this.language
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneratedContent that = (GeneratedContent) o;
        return Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId);
    }
}
