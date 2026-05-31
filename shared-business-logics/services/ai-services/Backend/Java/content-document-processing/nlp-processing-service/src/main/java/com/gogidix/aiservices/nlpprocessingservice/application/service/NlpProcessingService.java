package com.gogidix.aiservices.nlpprocessingservice.application.service;

import com.gogidix.aiservices.nlpprocessingservice.application.dto.request.AnalyzeTextRequest;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.request.SummarizeTextRequest;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.response.TextAnalysisResponse;
import com.gogidix.aiservices.nlpprocessingservice.application.dto.response.TextSummaryResponse;
import com.gogidix.aiservices.nlpprocessingservice.domain.aggregate.TextAnalysis;
import com.gogidix.aiservices.nlpprocessingservice.domain.model.*;
import com.gogidix.aiservices.nlpprocessingservice.domain.port.out.NlpEnginePort;
import com.gogidix.aiservices.nlpprocessingservice.shared.exception.NlpProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NlpProcessingService {

    private final NlpEnginePort nlpEngine;

    private static final int MAX_TEXT_LENGTH = 100000;
    private static final int PROCESSING_TIMEOUT_MS = 30000;

    public TextAnalysisResponse analyzeText(AnalyzeTextRequest request) {
        validateTextLength(request.text());

        LanguageCode language = request.language() != null
                ? request.language()
                : LanguageCode.AUTO;

        TextAnalysis analysis = TextAnalysis.create(request.text(), language);

        // Detect language if AUTO
        if (language == LanguageCode.AUTO) {
            language = nlpEngine.detectLanguage(request.text());
        }

        // Extract entities if requested
        if (request.features().entities()) {
            var entities = nlpEngine.extractEntities(request.text(), language);
            analysis.addEntities(entities);
        }

        // Analyze sentiment if requested
        if (request.features().sentiment()) {
            var sentiment = nlpEngine.analyzeSentiment(request.text(), language);
            analysis.setSentiment(sentiment);
        }

        // Categorize if requested
        if (request.features().categories()) {
            var categories = nlpEngine.categorizeText(request.text(), language);
            categories.forEach(analysis::setCategoryScore);
        }

        // Extract keywords if requested
        if (request.features().keywords()) {
            var keywords = nlpEngine.extractKeywords(request.text(), language);
            analysis.addKeywords(keywords);
        }

        return toResponse(analysis);
    }

    public TextSummaryResponse summarizeText(SummarizeTextRequest request) {
        validateTextLength(request.text());

        if (request.ratio() < 0.1 || request.ratio() > 0.9) {
            throw new NlpProcessingException("Summary ratio must be between 0.1 and 0.9");
        }

        LanguageCode language = request.language() != null
                ? request.language()
                : LanguageCode.AUTO;

        String summary = nlpEngine.summarize(request.text(), language, request.ratio());

        return new TextSummaryResponse(
                summary,
                request.text().length(),
                summary.length(),
                language
        );
    }

    private void validateTextLength(String text) {
        if (text == null || text.isEmpty()) {
            throw new NlpProcessingException("Text cannot be null or empty");
        }
        if (text.length() > MAX_TEXT_LENGTH) {
            throw new NlpProcessingException("Text length exceeds maximum of " + MAX_TEXT_LENGTH + " characters");
        }
    }

    private TextAnalysisResponse toResponse(TextAnalysis analysis) {
        return new TextAnalysisResponse(
                analysis.getAnalysisId().toString(),
                analysis.getDetectedLanguage().getIsoCode(),
                analysis.getEntities().stream()
                        .map(e -> new TextAnalysisResponse.EntityDto(
                                e.entityText(),
                                e.type().getShortCode(),
                                e.confidence()
                        ))
                        .toList(),
                analysis.getSentiment() != null
                        ? new TextAnalysisResponse.SentimentDto(
                                analysis.getSentiment().label().getDisplayName(),
                                analysis.getSentiment().score()
                        )
                        : null,
                analysis.getCategoryScores().entrySet().stream()
                        .map(e -> new TextAnalysisResponse.CategoryDto(
                                e.getKey(),
                                e.getValue()
                        ))
                        .toList(),
                analysis.getKeywords().stream()
                        .map(k -> new TextAnalysisResponse.KeywordDto(
                                k.text(),
                                k.relevance()
                        ))
                        .toList()
        );
    }
}
