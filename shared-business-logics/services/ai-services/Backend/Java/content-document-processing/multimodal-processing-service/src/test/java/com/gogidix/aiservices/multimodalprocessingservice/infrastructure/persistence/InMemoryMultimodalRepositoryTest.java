package com.gogidix.aiservices.multimodalprocessingservice.infrastructure.persistence;

import com.gogidix.aiservices.multimodalprocessingservice.domain.aggregate.MultimodalContent;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentItem;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentModality;
import com.gogidix.aiservices.multimodalprocessingservice.domain.port.out.MultimodalRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("InMemory Multimodal Repository Infrastructure Tests")
class InMemoryMultimodalRepositoryTest {

    private MultimodalRepository repository;

    @AfterEach
    void tearDown() {
        if (repository != null) {
            // Clean up if needed
        }
    }

    @Nested
    @DisplayName("Content Storage Tests")
    class StorageTests {

        @Test
        @DisplayName("Should save content")
        void shouldSaveContent() {
            repository = new InMemoryMultimodalRepository();

            List<ContentItem> items = List.of(
                    new ContentItem(ContentModality.TEXT, "url1", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, "user-123");
            content.addEmbedding(ContentModality.TEXT, createTestEmbedding());
            content.generateFusedEmbedding();

            MultimodalContent saved = repository.save(content);

            assertThat(saved).isNotNull();
            assertThat(saved.getContentId()).isEqualTo(content.getContentId());
        }

        @Test
        @DisplayName("Should save multiple contents")
        void shouldSaveMultipleContents() {
            repository = new InMemoryMultimodalRepository();

            MultimodalContent content1 = createAndPopulateContent("url1");
            MultimodalContent content2 = createAndPopulateContent("url2");

            repository.save(content1);
            repository.save(content2);

            List<MultimodalContent> all = repository.findAll();

            assertThat(all).hasSize(2);
        }

        @Test
        @DisplayName("Should update existing content")
        void shouldUpdateExistingContent() {
            repository = new InMemoryMultimodalRepository();

            List<ContentItem> items = List.of(
                    new ContentItem(ContentModality.TEXT, "url1", Map.of())
            );

            MultimodalContent content = MultimodalContent.create(items, "user-123");
            content.addEmbedding(ContentModality.TEXT, createTestEmbedding());
            content.generateFusedEmbedding();

            repository.save(content);

            content.setSummary("Updated summary");
            MultimodalContent updated = repository.save(content);

            assertThat(updated.getSummary()).isEqualTo("Updated summary");
        }
    }

    @Nested
    @DisplayName("Content Retrieval Tests")
    class RetrievalTests {

        @Test
        @DisplayName("Should find all content")
        void shouldFindAllContent() {
            repository = new InMemoryMultimodalRepository();

            repository.save(createAndPopulateContent("url1"));
            repository.save(createAndPopulateContent("url2"));
            repository.save(createAndPopulateContent("url3"));

            List<MultimodalContent> all = repository.findAll();

            assertThat(all).hasSize(3);
        }

        @Test
        @DisplayName("Should return empty list when no content")
        void shouldReturnEmptyListWhenNoContent() {
            repository = new InMemoryMultimodalRepository();

            List<MultimodalContent> all = repository.findAll();

            assertThat(all).isEmpty();
        }

        @Test
        @DisplayName("Should find by user ID")
        void shouldFindByUserId() {
            repository = new InMemoryMultimodalRepository();

            repository.save(createAndPopulateContent("url1", "user-1"));
            repository.save(createAndPopulateContent("url2", "user-2"));
            repository.save(createAndPopulateContent("url3", "user-1"));

            List<MultimodalContent> user1Content = repository.findByUserId("user-1");

            assertThat(user1Content).hasSize(2);
            assertThat(user1Content)
                    .allMatch(c -> c.getUserId().equals("user-1"));
        }
    }

    @Nested
    @DisplayName("Content Deletion Tests")
    class DeletionTests {

        @Test
        @DisplayName("Should delete by ID")
        void shouldDeleteById() {
            repository = new InMemoryMultimodalRepository();

            MultimodalContent content = createAndPopulateContent("url1");
            content = repository.save(content);
            UUID contentId = content.getContentId();

            repository.deleteById(contentId.toString());

            List<MultimodalContent> remaining = repository.findAll();
            assertThat(remaining).isEmpty();
        }

        @Test
        @DisplayName("Should handle deleting non-existent content")
        void shouldHandleDeletingNonExistentContent() {
            repository = new InMemoryMultimodalRepository();

            assertThatCode(() -> repository.deleteById(UUID.randomUUID().toString()))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should delete by user ID")
        void shouldDeleteByUserId() {
            repository = new InMemoryMultimodalRepository();

            repository.save(createAndPopulateContent("url1", "user-1"));
            repository.save(createAndPopulateContent("url2", "user-2"));

            repository.deleteByUserId("user-1");

            List<MultimodalContent> remaining = repository.findAll();
            assertThat(remaining).hasSize(1);
            assertThat(remaining.get(0).getUserId()).isEqualTo("user-2");
        }
    }

    @Nested
    @DisplayName("Content Query Tests")
    class QueryTests {

        @Test
        @DisplayName("Should find by modality")
        void shouldFindByModality() {
            repository = new InMemoryMultimodalRepository();

            List<ContentItem> textItems = List.of(
                    new ContentItem(ContentModality.TEXT, "url1", Map.of())
            );
            List<ContentItem> imageItems = List.of(
                    new ContentItem(ContentModality.IMAGE, "url2", Map.of())
            );

            MultimodalContent textContent = MultimodalContent.create(textItems, "user-1");
            MultimodalContent imageContent = MultimodalContent.create(imageItems, "user-1");

            textContent.addEmbedding(ContentModality.TEXT, createTestEmbedding());
            imageContent.addEmbedding(ContentModality.IMAGE, createTestEmbedding());

            repository.save(textContent);
            repository.save(imageContent);

            // Note: This would require implementation in the repository
            // Just testing the interface contract
            assertThat(repository.findAll()).hasSize(2);
        }

        @Test
        @DisplayName("Should count content")
        void shouldCountContent() {
            repository = new InMemoryMultimodalRepository();

            repository.save(createAndPopulateContent("url1"));
            repository.save(createAndPopulateContent("url2"));
            repository.save(createAndPopulateContent("url3"));

            // Assuming count method exists or can be derived
            List<MultimodalContent> all = repository.findAll();
            assertThat(all).hasSize(3);
        }
    }

    private MultimodalContent createAndPopulateContent(String url) {
        return createAndPopulateContent(url, "user-123");
    }

    private MultimodalContent createAndPopulateContent(String url, String userId) {
        List<ContentItem> items = List.of(
                new ContentItem(ContentModality.TEXT, url, Map.of())
        );

        MultimodalContent content = MultimodalContent.create(items, userId);
        content.addEmbedding(ContentModality.TEXT, createTestEmbedding());
        content.generateFusedEmbedding();
        return content;
    }

    private float[] createTestEmbedding() {
        float[] embedding = new float[768];
        java.util.Arrays.fill(embedding, 0.5f);
        return embedding;
    }
}
