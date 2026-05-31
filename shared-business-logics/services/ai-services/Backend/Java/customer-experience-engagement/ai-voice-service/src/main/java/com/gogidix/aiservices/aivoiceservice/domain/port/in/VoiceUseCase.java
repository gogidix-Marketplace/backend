package com.gogidix.aiservices.aivoiceservice.domain.port.in;

import com.gogidix.aiservices.aivoiceservice.domain.model.SynthesisResult;
import com.gogidix.aiservices.aivoiceservice.domain.model.VoiceType;

public interface VoiceUseCase {
    SynthesisResult synthesize(String text, VoiceType voiceType, String format);
}
