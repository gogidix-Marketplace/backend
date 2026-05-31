package com.gogidix.aiservices.aicontentgenerationservice.application.service;

import com.gogidix.aiservices.aicontentgenerationservice.application.dto.request.GenerateContentRequest;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.request.OptimizeContentRequest;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.response.ContentResponse;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.response.OptimizationResponse;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.*;
import com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.ContentGenerationPort;
import com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.ContentRepository;
import com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.EventPublisherPort;
import com.gogidix.aiservices.aicontentgenerationservice.domain.policy.ContentGenerationPolicy;
import com.gogidix.aiservices.aicontentgenerationservice.shared.exception.ContentGenerationException;
import com.gogidix.aiservices.aicontentgenerationservice.shared.exception.ContentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentGenerationService {

    private final ContentGenerationPort contentGenerationPort;
    private final ContentRepository contentRepository;
    private final EventPublisherPort eventPublisher;
    private final ContentGenerationPolicy policy;

    private static final int MAX_VARIATIONS = 5;
    private static final int DEFAULT_VARIATIONS = 1;

    public ContentResponse generateContent(GenerateContentRequest request) {
        policy.validatePrompt(request.getPrompt());
        policy.validateContentType(request.getContentType());

        ContentTone tone = request.getTone() != null ? request.getTone() : ContentTone.NEUTRAL;
        String language = request.getLanguage() != null ? request.getLanguage() : "en";

        String contentId = UUID.randomUUID().toString();
        GeneratedContent content = GeneratedContent.builder()
                .contentId(contentId)
                .prompt(request.getPrompt())
                .contentType(request.getContentType())
                .tone(tone)
                .language(language)
                .build();

        content.markAsProcessing();
        contentRepository.save(content);

        try {
            GeneratedContent generated = contentGenerationPort.generateContent(
                    request.getPrompt(),
                    request.getContentType(),
                    tone,
                    language
            );

            content.markAsCompleted(generated.getContent());
            content = enrichWithMetadata(content);

            contentRepository.save(content);
            eventPublisher.publishContentGenerated(contentId, request.getUserId(), request.getContentType().toString());

            log.info("Content generated successfully: {}", contentId);
            return toContentResponse(content);

        } catch (Exception e) {
            content.markAsFailed(e.getMessage());
            contentRepository.save(content);
            eventPublisher.publishContentFailed(contentId, request.getUserId(), e.getMessage());
            throw new ContentGenerationException("Failed to generate content: " + e.getMessage(), e);
        }
    }

    public ContentResponse generateVariations(GenerateContentRequest request) {
        policy.validatePrompt(request.getPrompt());
        policy.validateContentType(request.getContentType());

        int variations = Math.min(
                request.getVariations() != null ? request.getVariations() : DEFAULT_VARIATIONS,
                MAX_VARIATIONS
        );

        ContentTone tone = request.getTone() != null ? request.getTone() : ContentTone.NEUTRAL;
        String language = request.getLanguage() != null ? request.getLanguage() : "en";

        String contentId = UUID.randomUUID().toString();

        try {
            List<GeneratedContent> generatedContents = contentGenerationPort.generateVariations(
                    request.getPrompt(),
                    request.getContentType(),
                    tone,
                    language,
                    variations
            );

            GeneratedContent primaryContent = generatedContents.get(0);
            primaryContent = GeneratedContent.builder()
                    .contentId(contentId)
                    .content(primaryContent.getContent())
                    .prompt(request.getPrompt())
                    .contentType(request.getContentType())
                    .tone(tone)
                    .language(language)
                    .status(GenerationStatus.COMPLETED)
                    .quality(ContentQuality.HIGH)
                    .build();

            primaryContent = enrichWithMetadata(primaryContent);
            contentRepository.save(primaryContent);

            List<ContentResponse> variationResponses = generatedContents.stream()
                    .skip(1)
                    .map(c -> {
                        GeneratedContent saved = contentRepository.save(enrichWithMetadata(c));
                        return toContentResponse(saved);
                    })
                    .collect(Collectors.toList());

            eventPublisher.publishContentGenerated(contentId, request.getUserId(), request.getContentType().toString());

            ContentResponse response = toContentResponse(primaryContent);
            response.getVariations();

            log.info("Generated {} variations for content: {}", variations, contentId);
            return response;

        } catch (Exception e) {
            eventPublisher.publishContentFailed(contentId, request.getUserId(), e.getMessage());
            throw new ContentGenerationException("Failed to generate content variations: " + e.getMessage(), e);
        }
    }

    public ContentResponse getContent(String contentId) {
        GeneratedContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException("Content not found: " + contentId));
        return toContentResponse(content);
    }

    public List<ContentResponse> getUserContent(String userId) {
        List<GeneratedContent> contents = contentRepository.findByUserId(userId);
        return contents.stream()
                .map(this::toContentResponse)
                .collect(Collectors.toList());
    }

    public OptimizationResponse optimizeContent(OptimizeContentRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content to optimize cannot be empty");
        }

        String optimized = contentGenerationPort.optimizeContent(
                request.getContent(),
                request.getContentType(),
                request.getOptimizationGoal()
        );

        int originalWordCount = countWords(request.getContent());
        int optimizedWordCount = countWords(optimized);

        return OptimizationResponse.builder()
                .originalContent(request.getContent())
                .optimizedContent(optimized)
                .improvements(determineImprovements(request.getOptimizationGoal()))
                .originalWordCount(originalWordCount)
                .optimizedWordCount(optimizedWordCount)
                .build();
    }

    public void deleteContent(String contentId) {
        GeneratedContent content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException("Content not found: " + contentId));
        contentRepository.delete(contentId);
        log.info("Content deleted: {}", contentId);
    }

    private GeneratedContent enrichWithMetadata(GeneratedContent content) {
        int wordCount = countWords(content.getContent());
        double relevanceScore = contentGenerationPort.calculateRelevanceScore(content.getContent(), content.getPrompt());

        return GeneratedContent.builder()
                .contentId(content.getContentId())
                .content(content.getContent())
                .prompt(content.getPrompt())
                .contentType(content.getContentType())
                .tone(content.getTone())
                .language(content.getLanguage())
                .status(content.getStatus())
                .quality(determineQuality(wordCount, relevanceScore))
                .wordCount(wordCount)
                .relevanceScore(relevanceScore)
                .createdAt(content.getCreatedAt())
                .completedAt(content.getCompletedAt())
                .errorMessage(content.getErrorMessage())
                .build();
    }

    private ContentQuality determineQuality(int wordCount, double relevanceScore) {
        if (relevanceScore >= 0.9 && wordCount >= 100) {
            return ContentQuality.PREMIUM;
        } else if (relevanceScore >= 0.7 && wordCount >= 50) {
            return ContentQuality.HIGH;
        } else if (relevanceScore >= 0.5 && wordCount >= 20) {
            return ContentQuality.MEDIUM;
        }
        return ContentQuality.LOW;
    }

    private int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    private String determineImprovements(String goal) {
        if (goal == null) {
            return "General optimization applied";
        }
        return switch (goal.toLowerCase()) {
            case "seo" -> "SEO optimization: keywords added, meta tags improved";
            case "readability" -> "Readability improved: shorter sentences, simpler words";
            case "engagement" -> "Engagement increased: stronger hooks, clear CTAs";
            case "conciseness" -> "Content made more concise: removed fluff, focused on key points";
            default -> "General optimization applied based on goal: " + goal;
        };
    }

    private ContentResponse toContentResponse(GeneratedContent content) {
        return ContentResponse.builder()
                .contentId(content.getContentId())
                .content(content.getContent())
                .contentType(content.getContentType())
                .tone(content.getTone())
                .quality(content.getQuality())
                .status(content.getStatus())
                .errorMessage(content.getErrorMessage())
                .wordCount(content.getWordCount())
                .relevanceScore(content.getRelevanceScore())
                .language(content.getLanguage())
                .createdAt(content.getCreatedAt())
                .completedAt(content.getCompletedAt())
                .build();
    }
}
