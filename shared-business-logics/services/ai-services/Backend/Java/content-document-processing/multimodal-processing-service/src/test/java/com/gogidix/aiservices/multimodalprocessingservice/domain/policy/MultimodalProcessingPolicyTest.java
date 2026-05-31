package com.gogidix.aiservices.multimodalprocessingservice.domain.policy;

import com.gogidix.aiservices.multimodalprocessingservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MultimodalProcessingPolicy Tests")
class MultimodalProcessingPolicyTest {

    private final MultimodalProcessingPolicy policy = new MultimodalProcessingPolicy();

    @Nested
    @DisplayName("Content Validation Tests")
    class ContentValidationTests {

        @Test
        @DisplayName("Should validate content item count")
        void shouldValidateItemCount() {
            List<ContentItem> validItems = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "url1", null),
                    new ContentItem(ContentModality.IMAGE, "url2", null)
            );

            assertThat(policy.isValidItemCount(validItems)).isTrue();

            List<ContentItem> tooManyItems = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "url1", null),
                    new ContentItem(ContentModality.TEXT, "url2", null),
                    new ContentItem(ContentModality.TEXT, "url3", null),
                    new ContentItem(ContentModality.TEXT, "url4", null),
                    new ContentItem(ContentModality.TEXT, "url5", null),
                    new ContentItem(ContentModality.TEXT, "url6", null),
                    new ContentItem(ContentModality.TEXT, "url7", null),
                    new ContentItem(ContentModality.TEXT, "url8", null),
                    new ContentItem(ContentModality.TEXT, "url9", null),
                    new ContentItem(ContentModality.TEXT, "url10", null),
                    new ContentItem(ContentModality.TEXT, "url11", null)
            );

            assertThat(policy.isValidItemCount(tooManyItems)).isFalse();
        }

        @Test
        @DisplayName("Should get maximum content items")
        void shouldGetMaxItems() {
            assertThat(policy.getMaxContentItems()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should validate URL format")
        void shouldValidateUrlFormat() {
            assertThat(policy.isValidUrl("https://example.com/content.mp4")).isTrue();
            assertThat(policy.isValidUrl("http://example.com/content.mp4")).isTrue();
            assertThat(policy.isValidUrl("s3://bucket/content.mp4")).isTrue();
            assertThat(policy.isValidUrl("gs://bucket/content.mp4")).isTrue();
            assertThat(policy.isValidUrl("invalid-url")).isFalse();
            assertThat(policy.isValidUrl(null)).isFalse();
        }

        @Test
        @DisplayName("Should validate video length")
        void shouldValidateVideoLength() {
            assertThat(policy.isValidVideoLength(Duration.ofMinutes(5))).isTrue();
            assertThat(policy.isValidVideoLength(Duration.ofMinutes(10))).isTrue();
            assertThat(policy.isValidVideoLength(Duration.ofMinutes(11))).isFalse();
        }

        @Test
        @DisplayName("Should get max video length")
        void shouldGetMaxVideoLength() {
            assertThat(policy.getMaxVideoLength()).isEqualTo(Duration.ofMinutes(10));
        }
    }

    @Nested
    @DisplayName("Embedding Tests")
    class EmbeddingTests {

        @Test
        @DisplayName("Should get embedding dimension")
        void shouldGetEmbeddingDimension() {
            assertThat(policy.getEmbeddingDimension()).isEqualTo(768);
        }

        @Test
        @DisplayName("Should validate embedding dimension")
        void shouldValidateEmbeddingDimension() {
            float[] validEmbedding = new float[768];
            assertThat(policy.isValidEmbeddingDimension(validEmbedding.length)).isTrue();

            float[] invalidEmbedding = new float[512];
            assertThat(policy.isValidEmbeddingDimension(invalidEmbedding.length)).isFalse();
        }

        @Test
        @DisplayName("Should calculate similarity threshold")
        void shouldCalculateSimilarityThreshold() {
            assertThat(policy.getSimilarityThreshold()).isEqualTo(0.7);
        }
    }

    @Nested
    @DisplayName("Output Format Tests")
    class OutputFormatTests {

        @Test
        @DisplayName("Should validate output format")
        void shouldValidateOutputFormat() {
            assertThat(policy.isValidOutputFormat(OutputFormat.EMBEDDING)).isTrue();
            assertThat(policy.isValidOutputFormat(OutputFormat.SUMMARY)).isTrue();
            assertThat(policy.isValidOutputFormat(OutputFormat.TAGS)).isTrue();
        }

        @Test
        @DisplayName("Should parse output format from string")
        void shouldParseOutputFormat() {
            assertThat(policy.parseOutputFormat("embedding")).isEqualTo(OutputFormat.EMBEDDING);
            assertThat(policy.parseOutputFormat("summary")).isEqualTo(OutputFormat.SUMMARY);
            assertThat(policy.parseOutputFormat("tags")).isEqualTo(OutputFormat.TAGS);
            assertThat(policy.parseOutputFormat("invalid")).isEqualTo(OutputFormat.EMBEDDING);
        }
    }

    @Nested
    @DisplayName("Fusion Strategy Tests")
    class FusionStrategyTests {

        @Test
        @DisplayName("Should have fusion strategies")
        void shouldHaveFusionStrategies() {
            assertThat(policy.getFusionStrategies()).contains("concat", "average", "attention");
        }

        @Test
        @DisplayName("Should get default fusion strategy")
        void shouldGetDefaultFusionStrategy() {
            assertThat(policy.getDefaultFusionStrategy()).isEqualTo("average");
        }

        @Test
        @DisplayName("Should validate fusion strategy")
        void shouldValidateFusionStrategy() {
            assertThat(policy.isValidFusionStrategy("concat")).isTrue();
            assertThat(policy.isValidFusionStrategy("average")).isTrue();
            assertThat(policy.isValidFusionStrategy("attention")).isTrue();
            assertThat(policy.isValidFusionStrategy("invalid")).isFalse();
        }
    }

    @Nested
    @DisplayName("Content Size Tests")
    class ContentSizeTests {

        @Test
        @DisplayName("Should get max size for modality")
        void shouldGetMaxSizeForModality() {
            assertThat(policy.getMaxSizeForModality(ContentModality.TEXT)).isEqualTo(10 * 1024 * 1024L);
            assertThat(policy.getMaxSizeForModality(ContentModality.IMAGE)).isEqualTo(20 * 1024 * 1024L);
            assertThat(policy.getMaxSizeForModality(ContentModality.AUDIO)).isEqualTo(100 * 1024 * 1024L);
            assertThat(policy.getMaxSizeForModality(ContentModality.VIDEO)).isEqualTo(500 * 1024 * 1024L);
        }

        @Test
        @DisplayName("Should validate content size")
        void shouldValidateContentSize() {
            assertThat(policy.isValidContentSize(ContentModality.TEXT, 5 * 1024 * 1024L)).isTrue();
            assertThat(policy.isValidContentSize(ContentModality.TEXT, 15 * 1024 * 1024L)).isFalse();
        }
    }
}
