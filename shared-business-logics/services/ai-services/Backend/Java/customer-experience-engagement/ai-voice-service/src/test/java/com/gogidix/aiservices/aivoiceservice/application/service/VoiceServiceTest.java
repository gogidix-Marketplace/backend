package com.gogidix.aiservices.aivoiceservice.application.service;

import com.gogidix.aiservices.aivoiceservice.domain.model.SynthesisResult;
import com.gogidix.aiservices.aivoiceservice.domain.model.VoiceType;
import com.gogidix.aiservices.aivoiceservice.domain.port.out.VoiceSynthesisPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoiceServiceTest {

    @Mock
    private VoiceSynthesisPort voiceSynthesisPort;

    @InjectMocks
    private VoiceService voiceService;

    @Test
    void shouldSynthesize() {
        SynthesisResult result = SynthesisResult.builder()
                .synthesisId("synth-1")
                .audioUrl("https://example.com/audio.mp3")
                .text("Hello world")
                .voiceType(VoiceType.FEMALE)
                .duration(1500)
                .format("mp3")
                .build();

        when(voiceSynthesisPort.synthesize(any(), any(), any())).thenReturn(result);

        SynthesisResult synthesis = voiceService.synthesize("Hello world", VoiceType.FEMALE, "mp3");

        assertThat(synthesis).isNotNull();
        assertThat(synthesis.getSynthesisId()).isEqualTo("synth-1");
    }
}
