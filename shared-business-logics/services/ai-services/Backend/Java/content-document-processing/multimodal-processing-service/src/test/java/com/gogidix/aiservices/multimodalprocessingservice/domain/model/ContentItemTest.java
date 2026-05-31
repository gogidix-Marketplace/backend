package com.gogidix.aiservices.multimodalprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ContentItem Domain Model Tests")
class ContentItemTest {

    @Nested
    @DisplayName("Content Item Creation Tests")
    class ItemCreationTests {

        @Test
        @DisplayName("Should create content item with all parameters")
        void shouldCreateContentItemWithAllParameters() {
            Map<String, Object> metadata = Map.of("size", 1024, "author", "test");

            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.txt",
                    metadata
            );

            assertThat(item).isNotNull();
            assertThat(item.modality()).isEqualTo(ContentModality.TEXT);
            assertThat(item.url()).isEqualTo("https://example.com/document.txt");
            assertThat(item.metadata()).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should create content item with null metadata")
        void shouldCreateContentItemWithNullMetadata() {
            ContentItem item = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/image.png",
                    null
            );

            assertThat(item.metadata()).isNotNull();
            assertThat(item.metadata()).isEmpty();
        }

        @Test
        @DisplayName("Should create content item with empty metadata")
        void shouldCreateContentItemWithEmptyMetadata() {
            Map<String, Object> emptyMetadata = Map.of();

            ContentItem item = new ContentItem(
                    ContentModality.AUDIO,
                    "https://example.com/audio.mp3",
                    emptyMetadata
            );

            assertThat(item.metadata()).isEmpty();
        }

        @Test
        @DisplayName("Should reject null modality")
        void shouldRejectNullModality() {
            assertThatThrownBy(() -> new ContentItem(
                    null,
                    "https://example.com/document.txt",
                    Map.of()
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Modality cannot be null");
        }

        @Test
        @DisplayName("Should reject null URL")
        void shouldRejectNullUrl() {
            assertThatThrownBy(() -> new ContentItem(
                    ContentModality.TEXT,
                    null,
                    Map.of()
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("URL cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject empty URL")
        void shouldRejectEmptyUrl() {
            assertThatThrownBy(() -> new ContentItem(
                    ContentModality.TEXT,
                    "",
                    Map.of()
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("URL cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject whitespace URL")
        void shouldRejectWhitespaceUrl() {
            assertThatThrownBy(() -> new ContentItem(
                    ContentModality.TEXT,
                    "   ",
                    Map.of()
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("URL cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Format Detection Tests")
    class FormatDetectionTests {

        @Test
        @DisplayName("Should detect text format")
        void shouldDetectTextFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.txt",
                    Map.of()
            );

            assertThat(item.getFormat()).isEqualTo("txt");
        }

        @Test
        @DisplayName("Should detect pdf format")
        void shouldDetectPdfFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/report.pdf",
                    Map.of()
            );

            assertThat(item.getFormat()).isEqualTo("pdf");
        }

        @Test
        @DisplayName("Should detect image format")
        void shouldDetectImageFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/picture.png",
                    Map.of()
            );

            assertThat(item.getFormat()).isEqualTo("png");
        }

        @Test
        @DisplayName("Should detect jpeg format")
        void shouldDetectJpegFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/photo.jpeg",
                    Map.of()
            );

            assertThat(item.getFormat()).isEqualTo("jpeg");
        }

        @Test
        @DisplayName("Should detect audio format")
        void shouldDetectAudioFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.AUDIO,
                    "https://example.com/sound.mp3",
                    Map.of()
            );

            assertThat(item.getFormat()).isEqualTo("mp3");
        }

        @Test
        @DisplayName("Should detect video format")
        void shouldDetectVideoFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.VIDEO,
                    "https://example.com/clip.mp4",
                    Map.of()
            );

            assertThat(item.getFormat()).isEqualTo("mp4");
        }

        @Test
        @DisplayName("Should return empty string when no extension")
        void shouldReturnEmptyStringWhenNoExtension() {
            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document",
                    Map.of()
            );

            assertThat(item.getFormat()).isEmpty();
        }

        @Test
        @DisplayName("Should handle complex URL path")
        void shouldHandleComplexUrlPath() {
            ContentItem item = new ContentItem(
                    ContentModality.IMAGE,
                    "https://cdn.example.com/v2/images/processed/photo.jpg",
                    Map.of()
            );

            assertThat(item.getFormat()).isEqualTo("jpg");
        }
    }

    @Nested
    @DisplayName("Format Validation Tests")
    class FormatValidationTests {

        @Test
        @DisplayName("Should validate supported text format")
        void shouldValidateSupportedTextFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.txt",
                    Map.of()
            );

            assertThat(item.isFormatSupported()).isTrue();
        }

        @Test
        @DisplayName("Should validate supported image format")
        void shouldValidateSupportedImageFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/picture.png",
                    Map.of()
            );

            assertThat(item.isFormatSupported()).isTrue();
        }

        @Test
        @DisplayName("Should validate supported audio format")
        void shouldValidateSupportedAudioFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.AUDIO,
                    "https://example.com/sound.wav",
                    Map.of()
            );

            assertThat(item.isFormatSupported()).isTrue();
        }

        @Test
        @DisplayName("Should validate supported video format")
        void shouldValidateSupportedVideoFormat() {
            ContentItem item = new ContentItem(
                    ContentModality.VIDEO,
                    "https://example.com/clip.webm",
                    Map.of()
            );

            assertThat(item.isFormatSupported()).isTrue();
        }

        @Test
        @DisplayName("Should reject unsupported format for text")
        void shouldRejectUnsupportedFormatForText() {
            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/document.exe",
                    Map.of()
            );

            assertThat(item.isFormatSupported()).isFalse();
        }

        @Test
        @DisplayName("Should reject unsupported format for image")
        void shouldRejectUnsupportedFormatForImage() {
            ContentItem item = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/picture.bmp",
                    Map.of()
            );

            assertThat(item.isFormatSupported()).isFalse();
        }
    }

    @Nested
    @DisplayName("Metadata Tests")
    class MetadataTests {

        @Test
        @DisplayName("Should store size in metadata")
        void shouldStoreSizeInMetadata() {
            Map<String, Object> metadata = Map.of("size", 2048, "quality", "high");

            ContentItem item = new ContentItem(
                    ContentModality.IMAGE,
                    "https://example.com/picture.png",
                    metadata
            );

            assertThat(item.metadata()).containsKey("size");
            assertThat(item.metadata().get("size")).isEqualTo(2048);
        }

        @Test
        @DisplayName("Should store complex metadata")
        void shouldStoreComplexMetadata() {
            Map<String, Object> metadata = Map.of(
                    "size", 1024,
                    "duration", 120,
                    "codec", "h264",
                    "resolution", Map.of("width", 1920, "height", 1080)
            );

            ContentItem item = new ContentItem(
                    ContentModality.VIDEO,
                    "https://example.com/video.mp4",
                    metadata
            );

            assertThat(item.metadata()).hasSize(4);
            assertThat(item.metadata()).containsKey("resolution");
        }

        @Test
        @DisplayName("Should handle metadata with various types")
        void shouldHandleMetadataWithVariousTypes() {
            Map<String, Object> metadata = Map.of(
                    "count", 42,
                    "enabled", true,
                    "name", "test",
                    "rating", 4.5
            );

            ContentItem item = new ContentItem(
                    ContentModality.TEXT,
                    "https://example.com/doc.txt",
                    metadata
            );

            assertThat(item.metadata()).containsValue(42);
            assertThat(item.metadata()).containsValue(true);
            assertThat(item.metadata()).containsValue("test");
            assertThat(item.metadata()).containsValue(4.5);
        }
    }
}
