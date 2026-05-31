package com.gogidix.aiservices.aivoiceservice.domain.model;

import lombok.Builder;

@Builder
public class SynthesisResult {
    private final String synthesisId;
    private final String audioUrl;
    private final String text;
    private final VoiceType voiceType;
    private final Integer duration;
    private final String format;

    public String getSynthesisId() { return synthesisId; }
    public String getAudioUrl() { return audioUrl; }
    public String getText() { return text; }
    public VoiceType getVoiceType() { return voiceType; }
    public Integer getDuration() { return duration; }
    public String getFormat() { return format; }
}
