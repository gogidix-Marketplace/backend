package com.gogidix.aiservices.multimodalprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ContentModality Domain Model Tests")
class ContentModalityTest {

    @Nested
    @DisplayName("Modality Type Tests")
    class ModalityTypeTests {

        @ParameterizedTest
        @EnumSource(ContentModality.class)
        @DisplayName("Should have valid modality types")
        void shouldHaveValidModalities(ContentModality modality) {
            assertThat(modality).isNotNull();
            assertThat(modality.name()).isIn("TEXT", "IMAGE", "AUDIO", "VIDEO");
        }

        @Test
        @DisplayName("Should parse modality from string")
        void shouldParseFromString() {
            assertThat(ContentModality.fromString("text")).isEqualTo(ContentModality.TEXT);
            assertThat(ContentModality.fromString("TEXT")).isEqualTo(ContentModality.TEXT);
            assertThat(ContentModality.fromString("image")).isEqualTo(ContentModality.IMAGE);
            assertThat(ContentModality.fromString("audio")).isEqualTo(ContentModality.AUDIO);
            assertThat(ContentModality.fromString("video")).isEqualTo(ContentModality.VIDEO);
        }

        @Test
        @DisplayName("Should return UNKNOWN for invalid string")
        void shouldReturnUnknownForInvalid() {
            assertThat(ContentModality.fromString("invalid")).isEqualTo(ContentModality.UNKNOWN);
            assertThat(ContentModality.fromString(null)).isEqualTo(ContentModality.UNKNOWN);
        }

        @Test
        @DisplayName("Should detect modality from MIME type")
        void shouldDetectFromMimeType() {
            assertThat(ContentModality.fromMimeType("text/plain")).isEqualTo(ContentModality.TEXT);
            assertThat(ContentModality.fromMimeType("image/png")).isEqualTo(ContentModality.IMAGE);
            assertThat(ContentModality.fromMimeType("audio/mp3")).isEqualTo(ContentModality.AUDIO);
            assertThat(ContentModality.fromMimeType("video/mp4")).isEqualTo(ContentModality.VIDEO);
        }

        @Test
        @DisplayName("Should return UNKNOWN for unknown MIME type")
        void shouldReturnUnknownForUnknownMimeType() {
            assertThat(ContentModality.fromMimeType("application/pdf")).isEqualTo(ContentModality.UNKNOWN);
        }
    }

    @Nested
    @DisplayName("Embedding Dimension Tests")
    class EmbeddingDimensionTests {

        @Test
        @DisplayName("Should get embedding dimension for modality")
        void shouldGetEmbeddingDimension() {
            assertThat(ContentModality.TEXT.getEmbeddingDimension()).isEqualTo(768);
            assertThat(ContentModality.IMAGE.getEmbeddingDimension()).isEqualTo(768);
            assertThat(ContentModality.AUDIO.getEmbeddingDimension()).isEqualTo(768);
            assertThat(ContentModality.VIDEO.getEmbeddingDimension()).isEqualTo(768);
        }

        @Test
        @DisplayName("Should validate embedding size")
        void shouldValidateEmbeddingSize() {
            float[] validEmbedding = new float[768];
            assertThat(ContentModality.TEXT.isValidEmbeddingSize(validEmbedding.length)).isTrue();

            float[] invalidEmbedding = new float[512];
            assertThat(ContentModality.TEXT.isValidEmbeddingSize(invalidEmbedding.length)).isFalse();
        }
    }

    @Nested
    @DisplayName("Supported Formats Tests")
    class SupportedFormatsTests {

        @Test
        @DisplayName("Should get supported formats for TEXT")
        void shouldGetTextFormats() {
            assertThat(ContentModality.TEXT.getSupportedFormats())
                    .contains("txt", "pdf", "html", "md");
        }

        @Test
        @DisplayName("Should get supported formats for IMAGE")
        void shouldGetImageFormats() {
            assertThat(ContentModality.IMAGE.getSupportedFormats())
                    .contains("png", "jpg", "jpeg", "gif", "webp");
        }

        @Test
        @DisplayName("Should get supported formats for AUDIO")
        void shouldGetAudioFormats() {
            assertThat(ContentModality.AUDIO.getSupportedFormats())
                    .contains("mp3", "wav", "ogg", "flac");
        }

        @Test
        @DisplayName("Should get supported formats for VIDEO")
        void shouldGetVideoFormats() {
            assertThat(ContentModality.VIDEO.getSupportedFormats())
                    .contains("mp4", "avi", "mov", "webm");
        }
    }

    @Nested
    @DisplayName("Max Content Size Tests")
    class MaxContentSizeTests {

        @Test
        @DisplayName("Should get max size for TEXT")
        void shouldGetMaxTextSize() {
            assertThat(ContentModality.TEXT.getMaxSizeMB()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should get max size for IMAGE")
        void shouldGetMaxImageSize() {
            assertThat(ContentModality.IMAGE.getMaxSizeMB()).isEqualTo(20);
        }

        @Test
        @DisplayName("Should get max size for AUDIO")
        void shouldGetMaxAudioSize() {
            assertThat(ContentModality.AUDIO.getMaxSizeMB()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should get max size for VIDEO")
        void shouldGetMaxVideoSize() {
            assertThat(ContentModality.VIDEO.getMaxSizeMB()).isEqualTo(500);
        }
    }
}
