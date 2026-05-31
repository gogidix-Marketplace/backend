package com.gogidix.aiservices.aicontentgenerationservice.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentQuality;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentTone;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentType;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GenerationStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ContentResponse {

    private String contentId;
    private String content;
    private ContentType contentType;
    private ContentTone tone;
    private ContentQuality quality;
    private GenerationStatus status;
    private String errorMessage;
    private Integer wordCount;
    private Double relevanceScore;
    private String language;
    private Instant createdAt;
    private Instant completedAt;
    private List<ContentResponse> variations;

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

    public ContentQuality getQuality() {
        return quality;
    }

    public GenerationStatus getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public List<ContentResponse> getVariations() {
        return variations;
    }
}
