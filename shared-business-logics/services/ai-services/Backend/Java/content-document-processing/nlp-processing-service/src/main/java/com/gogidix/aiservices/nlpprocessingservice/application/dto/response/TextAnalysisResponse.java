package com.gogidix.aiservices.nlpprocessingservice.application.dto.response;

import java.util.List;

public record TextAnalysisResponse(
        String analysisId,
        String language,
        List<EntityDto> entities,
        SentimentDto sentiment,
        List<CategoryDto> categories,
        List<KeywordDto> keywords
) {
    public record EntityDto(
            String text,
            String type,
            double confidence
    ) {}

    public record SentimentDto(
            String label,
            double score
    ) {}

    public record CategoryDto(
            String label,
            double confidence
    ) {}

    public record KeywordDto(
            String text,
            double relevance
    ) {}
}
