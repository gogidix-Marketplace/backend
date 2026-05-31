package com.gogidix.aiservices.multimodalprocessingservice.domain.aggregate;

import com.gogidix.aiservices.multimodalprocessingservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MultimodalContent Aggregate Tests")
class MultimodalContentTest {

    private static final String CONTENT_ID = "content-123";
    private static final String USER_ID = "user-123";

    @Nested
    @DisplayName("Content Creation Tests")
    class ContentCreationTests {

        @Test
        @DisplayName("Should create multimodal content")
        void shouldCreateContent() {
            List<ContentItem> items = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "https://example.com/text.txt", Map.of()),
                    new ContentItem(ContentModality.IMAGE, "https://example.com/image.png", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, USER_ID);

            assertThat(content).isNotNull();
            assertThat(content.getContentId()).isNotNull();
            assertThat(content.getUserId()).isEqualTo(USER_ID);
            assertThat(content.getItems()).hasSize(2);
            assertThat(content.getCreatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should reject null items")
        void shouldRejectNullItems() {
            assertThatThrownBy(() -> MultimodalContent.create(null, USER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Content items cannot be null");
        }

        @Test
        @DisplayName("Should reject empty items")
        void shouldRejectEmptyItems() {
            assertThatThrownBy(() -> MultimodalContent.create(Collections.emptyList(), USER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Content items cannot be empty");
        }

        @Test
        @DisplayName("Should reject more than 10 items")
        void shouldRejectTooManyItems() {
            List<ContentItem> items = new ArrayList<>();
            for (int i = 0; i < 11; i++) {
                items.add(new ContentItem(ContentModality.TEXT, "url" + i, Map.of()));
            }

            assertThatThrownBy(() -> MultimodalContent.create(items, USER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Cannot process more than 10 content items");
        }
    }

    @Nested
    @DisplayName("Embedding Generation Tests")
    class EmbeddingTests {

        @Test
        @DisplayName("Should add embedding for item")
        void shouldAddEmbedding() {
            List<ContentItem> items = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "https://example.com/text.txt", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, USER_ID);
            float[] textEmbedding = new float[768];
            Arrays.fill(textEmbedding, 0.1f);

            content.addEmbedding(ContentModality.TEXT, textEmbedding);

            assertThat(content.getEmbedding(ContentModality.TEXT)).isNotNull();
            assertThat(content.getEmbedding(ContentModality.TEXT).length).isEqualTo(768);
        }

        @Test
        @DisplayName("Should generate fused embedding")
        void shouldGenerateFusedEmbedding() {
            List<ContentItem> items = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "https://example.com/text.txt", Map.of()),
                    new ContentItem(ContentModality.IMAGE, "https://example.com/image.png", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, USER_ID);

            float[] textEmbedding = new float[768];
            float[] imageEmbedding = new float[768];
            Arrays.fill(textEmbedding, 0.5f);
            Arrays.fill(imageEmbedding, 0.3f);

            content.addEmbedding(ContentModality.TEXT, textEmbedding);
            content.addEmbedding(ContentModality.IMAGE, imageEmbedding);
            content.generateFusedEmbedding();

            assertThat(content.getFusedEmbedding()).isNotNull();
            assertThat(content.getFusedEmbedding().length).isEqualTo(768);
        }

        @Test
        @DisplayName("Should validate embedding dimension")
        void shouldValidateEmbeddingDimension() {
            List<ContentItem> items = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "https://example.com/text.txt", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, USER_ID);

            float[] invalidEmbedding = new float[512];

            assertThatThrownBy(() -> content.addEmbedding(ContentModality.TEXT, invalidEmbedding))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Embedding dimension must be 768");
        }
    }

    @Nested
    @DisplayName("Content Analysis Tests")
    class AnalysisTests {

        @Test
        @DisplayName("Should add summary")
        void shouldAddSummary() {
            List<ContentItem> items = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "https://example.com/text.txt", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, USER_ID);
            content.setSummary("This is a summary");

            assertThat(content.getSummary()).isEqualTo("This is a summary");
        }

        @Test
        @DisplayName("Should add tags")
        void shouldAddTags() {
            List<ContentItem> items = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "https://example.com/text.txt", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, USER_ID);
            content.setTags(Arrays.asList("technology", "ai", "document"));

            assertThat(content.getTags()).containsExactly("technology", "ai", "document");
        }

        @Test
        @DisplayName("Should get modalities present")
        void shouldGetModalitiesPresent() {
            List<ContentItem> items = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "https://example.com/text.txt", Map.of()),
                    new ContentItem(ContentModality.IMAGE, "https://example.com/image.png", Map.of()),
                    new ContentItem(ContentModality.AUDIO, "https://example.com/audio.mp3", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, USER_ID);

            Set<ContentModality> modalities = content.getModalities();

            assertThat(modalities).containsExactlyInAnyOrder(
                    ContentModality.TEXT, ContentModality.IMAGE, ContentModality.AUDIO
            );
        }
    }

    @Nested
    @DisplayName("Similarity Tests")
    class SimilarityTests {

        @Test
        @DisplayName("Should calculate similarity between embeddings")
        void shouldCalculateSimilarity() {
            List<ContentItem> items = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "https://example.com/text.txt", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, USER_ID);

            float[] embedding1 = new float[768];
            float[] embedding2 = new float[768];
            Arrays.fill(embedding1, 1.0f);
            Arrays.fill(embedding2, 1.0f);

            double similarity = content.calculateSimilarity(embedding1, embedding2);

            assertThat(similarity).isGreaterThan(0.99); // Should be very close to 1.0
        }

        @Test
        @DisplayName("Should find similar content")
        void shouldFindSimilarContent() {
            List<ContentItem> items = Arrays.asList(
                    new ContentItem(ContentModality.TEXT, "https://example.com/text.txt", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, USER_ID);

            float[] embedding = new float[768];
            Arrays.fill(embedding, 0.5f);

            content.addEmbedding(ContentModality.TEXT, embedding);
            content.generateFusedEmbedding();

            List<MultimodalContent> candidates = Arrays.asList(
                    createContentWithEmbedding(new float[768]),
                    createContentWithEmbedding(embedding)
            );

            List<MultimodalContent> similar = content.findSimilar(candidates, 0.8);

            assertThat(similar).hasSize(1);
        }
    }

    private MultimodalContent createContentWithEmbedding(float[] embedding) {
        List<ContentItem> items = Arrays.asList(
                new ContentItem(ContentModality.TEXT, "url", Map.of())
        );
        MultimodalContent content = MultimodalContent.create(items, USER_ID);
        content.addEmbedding(ContentModality.TEXT, embedding);
        content.generateFusedEmbedding();
        return content;
    }
}
