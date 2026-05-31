package com.gogidix.aiservices.voicerecognitionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Voice Recognition Domain Model Tests")
class RecognitionResultTest {

    @Nested
    @DisplayName("RecognitionResult Tests")
    class ResultTests {

        @Test
        @DisplayName("Should create recognition result")
        void shouldCreateResult() {
            RecognitionResult result = RecognitionResult.builder()
                    .recognitionId("rec-1")
                    .transcript("Hello world")
                    .confidence(0.92)
                    .language("en")
                    .duration(2000)
                    .build();

            assertThat(result).isNotNull();
            assertThat(result.getTranscript()).isEqualTo("Hello world");
            assertThat(result.getConfidence()).isEqualTo(0.92);
        }

        @Test
        @DisplayName("Should create result with null optional fields")
        void shouldCreateWithNullOptionals() {
            RecognitionResult result = RecognitionResult.builder()
                    .recognitionId("rec-1")
                    .transcript("Test")
                    .confidence(0.8)
                    .build();

            assertThat(result).isNotNull();
            assertThat(result.getLanguage()).isNull();
            assertThat(result.getDuration()).isNull();
        }
    }
}
