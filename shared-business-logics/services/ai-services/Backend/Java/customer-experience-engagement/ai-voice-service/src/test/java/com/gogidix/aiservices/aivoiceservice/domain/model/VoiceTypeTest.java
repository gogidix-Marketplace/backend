package com.gogidix.aiservices.aivoiceservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Voice Synthesis Domain Model Tests")
class VoiceTypeTest {

    @Nested
    @DisplayName("VoiceType Tests")
    class TypeTests {

        @ParameterizedTest
        @EnumSource(VoiceType.class)
        @DisplayName("Should accept all voice types")
        void shouldAcceptAllTypes(VoiceType type) {
            assertThat(type).isNotNull();
        }

        @Test
        @DisplayName("Should create synthesis result")
        void shouldCreateResult() {
            SynthesisResult result = SynthesisResult.builder()
                    .synthesisId("synth-1")
                    .audioUrl("https://example.com/audio.mp3")
                    .text("Hello world")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1500)
                    .format("mp3")
                    .build();

            assertThat(result).isNotNull();
            assertThat(result.getSynthesisId()).isEqualTo("synth-1");
            assertThat(result.getVoiceType()).isEqualTo(VoiceType.FEMALE);
        }
    }
}
