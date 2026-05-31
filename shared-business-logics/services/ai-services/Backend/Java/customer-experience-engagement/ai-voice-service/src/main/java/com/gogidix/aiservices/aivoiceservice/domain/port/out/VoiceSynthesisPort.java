package com.gogidix.aiservices.aivoiceservice.domain.port.out;

import com.gogidix.aiservices.aivoiceservice.domain.model.SynthesisResult;
import com.gogidix.aiservices.aivoiceservice.domain.model.VoiceType;

public interface VoiceSynthesisPort {
    SynthesisResult synthesize(String text, VoiceType voiceType, String format);
}
