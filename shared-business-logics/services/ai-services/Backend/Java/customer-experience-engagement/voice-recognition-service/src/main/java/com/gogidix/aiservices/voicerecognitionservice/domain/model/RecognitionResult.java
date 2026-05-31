package com.gogidix.aiservices.voicerecognitionservice.domain.model;

import lombok.Builder;

@Builder
public class RecognitionResult {
    private final String recognitionId;
    private final String transcript;
    private final double confidence;
    private final String language;
    private final Integer duration;

    public String getRecognitionId() { return recognitionId; }
    public String getTranscript() { return transcript; }
    public double getConfidence() { return confidence; }
    public String getLanguage() { return language; }
    public Integer getDuration() { return duration; }
}
