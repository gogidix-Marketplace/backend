package com.gogidix.aiservices.aitranslationservice.domain.model;

import lombok.Builder;

import java.time.Instant;

@Builder
public class TranslationResult {
    private final String translationId;
    private final String sourceText;
    private final String translatedText;
    private final Language sourceLanguage;
    private final Language targetLanguage;
    private final double confidence;
    private final Instant createdAt;
    private final Integer characterCount;
    private final String detectedLanguage;

    public String getTranslationId() { return translationId; }
    public String getSourceText() { return sourceText; }
    public String getTranslatedText() { return translatedText; }
    public Language getSourceLanguage() { return sourceLanguage; }
    public Language getTargetLanguage() { return targetLanguage; }
    public double getConfidence() { return confidence; }
    public Instant getCreatedAt() { return createdAt; }
    public Integer getCharacterCount() { return characterCount; }
    public String getDetectedLanguage() { return detectedLanguage; }
}
