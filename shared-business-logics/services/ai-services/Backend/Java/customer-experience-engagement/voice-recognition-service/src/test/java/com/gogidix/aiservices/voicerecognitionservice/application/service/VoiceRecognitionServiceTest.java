package com.gogidix.aiservices.voicerecognitionservice.application.service;

import com.gogidix.aiservices.voicerecognitionservice.domain.model.RecognitionResult;
import com.gogidix.aiservices.voicerecognitionservice.domain.port.out.VoiceRecognitionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoiceRecognitionServiceTest {

    @Mock
    private VoiceRecognitionPort voiceRecognitionPort;

    @InjectMocks
    private VoiceRecognitionService voiceRecognitionService;

    @Test
    void shouldRecognize() {
        RecognitionResult result = RecognitionResult.builder()
                .recognitionId("rec-1")
                .transcript("Hello world")
                .confidence(0.92)
                .language("en")
                .duration(2000)
                .build();

        when(voiceRecognitionPort.recognize(any(), any(), any())).thenReturn(result);

        RecognitionResult recognition = voiceRecognitionService.recognize(new byte[]{1, 2, 3}, "en", "audio/mp3");

        assertThat(recognition).isNotNull();
        assertThat(recognition.getTranscript()).isEqualTo("Hello world");
    }

    @Test
    void shouldRecognizeFromUrl() {
        RecognitionResult result = RecognitionResult.builder()
                .recognitionId("rec-2")
                .transcript("Test recognition")
                .confidence(0.88)
                .language("en")
                .build();

        when(voiceRecognitionPort.recognizeFromUrl(any(), any())).thenReturn(result);

        RecognitionResult recognition = voiceRecognitionService.recognizeFromUrl("https://example.com/audio.mp3", "en");

        assertThat(recognition).isNotNull();
        assertThat(recognition.getTranscript()).isEqualTo("Test recognition");
    }
}
