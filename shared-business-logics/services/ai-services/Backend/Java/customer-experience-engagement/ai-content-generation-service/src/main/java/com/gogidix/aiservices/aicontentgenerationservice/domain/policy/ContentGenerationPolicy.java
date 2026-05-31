package com.gogidix.aiservices.aicontentgenerationservice.domain.policy;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentType;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentTone;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GeneratedContent;

import java.util.List;

public class ContentGenerationPolicy {
    private static final int MAX_PROMPT_LENGTH = 5000;
    private static final int MIN_PROMPT_LENGTH = 10;
    private static final int MAX_CONTENT_LENGTH = 10000;
    private static final double DEFAULT_RELEVANCE_THRESHOLD = 0.7;

    public void validatePrompt(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be null or empty");
        }
        if (prompt.length() < MIN_PROMPT_LENGTH) {
            throw new IllegalArgumentException("Prompt must be at least " + MIN_PROMPT_LENGTH + " characters");
        }
        if (prompt.length() > MAX_PROMPT_LENGTH) {
            throw new IllegalArgumentException("Prompt cannot exceed " + MAX_PROMPT_LENGTH + " characters");
        }
    }

    public void validateContentType(ContentType contentType) {
        if (contentType == null) {
            throw new IllegalArgumentException("Content type cannot be null");
        }
    }

    public void validateContentTone(ContentTone tone) {
        if (tone == null) {
            throw new IllegalArgumentException("Content tone cannot be null");
        }
    }

    public void validateGeneratedContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Generated content cannot be null or empty");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("Generated content cannot exceed " + MAX_CONTENT_LENGTH + " characters");
        }
    }

    public boolean meetsQualityStandards(GeneratedContent content) {
        if (content == null) {
            return false;
        }
        if (!content.isCompleted()) {
            return false;
        }
        if (content.getContent() == null || content.getContent().trim().isEmpty()) {
            return false;
        }
        if (content.getWordCount() != null && content.getWordCount() < 10) {
            return false;
        }
        if (content.hasRelevanceScore()) {
            return content.meetsRelevanceThreshold(DEFAULT_RELEVANCE_THRESHOLD);
        }
        return true;
    }

    public List<GeneratedContent> filterByQuality(List<GeneratedContent> contents) {
        return contents.stream()
                .filter(this::meetsQualityStandards)
                .toList();
    }

    public GeneratedContent selectBestContent(List<GeneratedContent> contents) {
        return contents.stream()
                .filter(this::meetsQualityStandards)
                .max((c1, c2) -> {
                    if (c1.getRelevanceScore() != null && c2.getRelevanceScore() != null) {
                        return Double.compare(c1.getRelevanceScore(), c2.getRelevanceScore());
                    }
                    if (c1.getWordCount() != null && c2.getWordCount() != null) {
                        return Integer.compare(c1.getWordCount(), c2.getWordCount());
                    }
                    return 0;
                })
                .orElse(null);
    }

    public boolean requiresModeration(ContentType contentType) {
        return contentType == ContentType.AD_COPY ||
               contentType == ContentType.SOCIAL_MEDIA ||
               contentType == ContentType.NEWS;
    }

    public int getTargetWordCount(ContentType contentType) {
        return switch (contentType) {
            case PRODUCT_DESCRIPTION -> 150;
            case BLOG_POST -> 1000;
            case SOCIAL_MEDIA -> 50;
            case EMAIL -> 300;
            case AD_COPY -> 100;
            case LANDING_PAGE -> 500;
            case FAQ -> 100;
            case REVIEW -> 200;
            case NEWS -> 500;
            case TUTORIAL -> 1500;
        };
    }
}
