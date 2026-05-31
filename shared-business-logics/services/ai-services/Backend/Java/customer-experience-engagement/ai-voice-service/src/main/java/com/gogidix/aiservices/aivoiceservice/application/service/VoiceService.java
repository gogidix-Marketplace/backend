package com.gogidix.aiservices.aivoiceservice.application.service;

import com.gogidix.aiservices.aivoiceservice.domain.model.SynthesisResult;
import com.gogidix.aiservices.aivoiceservice.domain.model.VoiceType;
import com.gogidix.aiservices.aivoiceservice.domain.port.in.VoiceUseCase;
import com.gogidix.aiservices.aivoiceservice.domain.port.out.VoiceSynthesisPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoiceService implements VoiceUseCase {

    private final VoiceSynthesisPort voiceSynthesisPort;

    @Override
    public SynthesisResult synthesize(String text, VoiceType voiceType, String format) {
        return voiceSynthesisPort.synthesize(text, voiceType, format);
    }
}
