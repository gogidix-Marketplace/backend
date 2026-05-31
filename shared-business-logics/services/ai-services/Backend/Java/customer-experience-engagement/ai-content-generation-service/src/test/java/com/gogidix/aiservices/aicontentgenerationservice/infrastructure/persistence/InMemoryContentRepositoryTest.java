package com.gogidix.aiservices.aicontentgenerationservice.infrastructure.persistence;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentTone;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentType;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GeneratedContent;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GenerationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("In-Memory Content Repository Infrastructure Tests")
class InMemoryContentRepositoryTest {

    private InMemoryContentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryContentRepository();
    }

    @Nested
    @DisplayName("Content Storage Tests")
    class StorageTests {

        @Test
        @DisplayName("Should save content successfully")
        void shouldSaveContent() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId("content-1")
                    .prompt("Generate a product description")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .tone(ContentTone.PROFESSIONAL)
                    .language("en")
                    .build();

            GeneratedContent saved = repository.save(content);

            assertThat(saved).isNotNull();
            assertThat(saved.getContentId()).isEqualTo("content-1");
        }

        @Test
        @DisplayName("Should retrieve saved content by ID")
        void shouldRetrieveContentById() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId("content-1")
                    .prompt("Generate content")
                    .contentType(ContentType.BLOG_POST)
                    .status(GenerationStatus.COMPLETED)
                    .content("Generated blog post")
                    .build();

            repository.save(content);

            Optional<GeneratedContent> found = repository.findById("content-1");

            assertThat(found).isPresent();
            assertThat(found.get().getContentId()).isEqualTo("content-1");
            assertThat(found.get().getContent()).isEqualTo("Generated blog post");
        }

        @Test
        @DisplayName("Should return empty for non-existent content")
        void shouldReturnEmptyForNonExistent() {
            Optional<GeneratedContent> found = repository.findById("non-existent");

            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Should update existing content")
        void shouldUpdateContent() {
            GeneratedContent original = GeneratedContent.builder()
                    .contentId("content-1")
                    .prompt("Original prompt")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .status(GenerationStatus.PENDING)
                    .build();

            repository.save(original);

            GeneratedContent updated = GeneratedContent.builder()
                    .contentId("content-1")
                    .prompt("Updated prompt")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .status(GenerationStatus.COMPLETED)
                    .content("Updated content")
                    .build();

            repository.save(updated);

            Optional<GeneratedContent> found = repository.findById("content-1");
            assertThat(found).isPresent();
            assertThat(found.get().getPrompt()).isEqualTo("Updated prompt");
            assertThat(found.get().getStatus()).isEqualTo(GenerationStatus.COMPLETED);
        }
    }

    @Nested
    @DisplayName("Content Query Tests")
    class QueryTests {

        @Test
        @DisplayName("Should find content by status")
        void shouldFindByStatus() {
            repository.save(createContent("c1", GenerationStatus.PENDING));
            repository.save(createContent("c2", GenerationStatus.COMPLETED));
            repository.save(createContent("c3", GenerationStatus.COMPLETED));
            repository.save(createContent("c4", GenerationStatus.FAILED));

            List<GeneratedContent> completed = repository.findByStatus(GenerationStatus.COMPLETED);

            assertThat(completed).hasSize(2);
            assertThat(completed).allMatch(c -> c.getStatus() == GenerationStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should return empty list when no content matches status")
        void shouldReturnEmptyForNoMatchingStatus() {
            repository.save(createContent("c1", GenerationStatus.PENDING));
            repository.save(createContent("c2", GenerationStatus.COMPLETED));

            List<GeneratedContent> failed = repository.findByStatus(GenerationStatus.FAILED);

            assertThat(failed).isEmpty();
        }
    }

    @Nested
    @DisplayName("Content Deletion Tests")
    class DeletionTests {

        @Test
        @DisplayName("Should delete content successfully")
        void shouldDeleteContent() {
            GeneratedContent content = createContent("c1", GenerationStatus.COMPLETED);
            repository.save(content);

            repository.delete("c1");

            Optional<GeneratedContent> found = repository.findById("c1");
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Should handle deletion of non-existent content")
        void shouldHandleDeletionOfNonExistent() {
            assertThatCode(() -> repository.delete("non-existent"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should preserve other content after deletion")
        void shouldPreserveOtherContent() {
            repository.save(createContent("c1", GenerationStatus.COMPLETED));
            repository.save(createContent("c2", GenerationStatus.COMPLETED));
            repository.save(createContent("c3", GenerationStatus.COMPLETED));

            repository.delete("c2");

            assertThat(repository.findById("c1")).isPresent();
            assertThat(repository.findById("c2")).isEmpty();
            assertThat(repository.findById("c3")).isPresent();
        }
    }

    @Nested
    @DisplayName("Recent Content Tests")
    class RecentContentTests {

        @Test
        @DisplayName("Should retrieve recent content for user")
        void shouldRetrieveRecentContent() {
            repository.save(createContent("c1", GenerationStatus.COMPLETED));
            repository.save(createContent("c2", GenerationStatus.COMPLETED));
            repository.save(createContent("c3", GenerationStatus.COMPLETED));

            List<GeneratedContent> recent = repository.findRecentContent("user-1", 2);

            assertThat(recent).hasSizeLessThanOrEqualTo(2);
        }

        @Test
        @DisplayName("Should return empty list for user with no content")
        void shouldReturnEmptyForNoUserContent() {
            List<GeneratedContent> recent = repository.findRecentContent("non-existent-user", 10);

            assertThat(recent).isEmpty();
        }
    }

    private GeneratedContent createContent(String id, GenerationStatus status) {
        return GeneratedContent.builder()
                .contentId(id)
                .prompt("Generate content for " + id)
                .contentType(ContentType.PRODUCT_DESCRIPTION)
                .status(status)
                .language("en")
                .build();
    }
}
