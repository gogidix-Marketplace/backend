package com.gogidix.aiservices.aicontentgenerationservice.domain.policy;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ContentGenerationPolicy Domain Policy Tests")
class ContentGenerationPolicyTest {

    private final ContentGenerationPolicy policy = new ContentGenerationPolicy();

    @Nested
    @DisplayName("Prompt Validation Tests")
    class PromptValidationTests {

        @Test
        @DisplayName("Should accept valid prompt")
        void shouldAcceptValidPrompt() {
            String validPrompt = "Generate a compelling product description for a premium wireless headphone that highlights noise cancellation and 30-hour battery life.";

            assertThatCode(() -> policy.validatePrompt(validPrompt))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should reject null prompt")
        void shouldRejectNullPrompt() {
            assertThatThrownBy(() -> policy.validatePrompt(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Prompt cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject empty prompt")
        void shouldRejectEmptyPrompt() {
            assertThatThrownBy(() -> policy.validatePrompt(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Prompt cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject short prompt")
        void shouldRejectShortPrompt() {
            assertThatThrownBy(() -> policy.validatePrompt("Short"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Prompt must be at least");
        }

        @Test
        @DisplayName("Should reject overly long prompt")
        void shouldRejectLongPrompt() {
            String longPrompt = "A".repeat(5001);
            assertThatThrownBy(() -> policy.validatePrompt(longPrompt))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Prompt cannot exceed");
        }
    }

    @Nested
    @DisplayName("ContentType Validation Tests")
    class ContentTypeValidationTests {

        @ParameterizedTest
        @EnumSource(ContentType.class)
        @DisplayName("Should accept all content types")
        void shouldAcceptAllContentTypes(ContentType type) {
            assertThatCode(() -> policy.validateContentType(type))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should reject null content type")
        void shouldRejectNullContentType() {
            assertThatThrownBy(() -> policy.validateContentType(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Content type cannot be null");
        }
    }

    @Nested
    @DisplayName("ContentTone Validation Tests")
    class ContentToneValidationTests {

        @ParameterizedTest
        @EnumSource(ContentTone.class)
        @DisplayName("Should accept all content tones")
        void shouldAcceptAllContentTones(ContentTone tone) {
            assertThatCode(() -> policy.validateContentTone(tone))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should reject null content tone")
        void shouldRejectNullContentTone() {
            assertThatThrownBy(() -> policy.validateContentTone(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Content tone cannot be null");
        }
    }

    @Nested
    @DisplayName("Generated Content Validation Tests")
    class GeneratedContentValidationTests {

        @Test
        @DisplayName("Should accept valid generated content")
        void shouldAcceptValidContent() {
            String validContent = "This is a well-written product description with sufficient details.";

            assertThatCode(() -> policy.validateGeneratedContent(validContent))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should reject null generated content")
        void shouldRejectNullContent() {
            assertThatThrownBy(() -> policy.validateGeneratedContent(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Generated content cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject empty generated content")
        void shouldRejectEmptyContent() {
            assertThatThrownBy(() -> policy.validateGeneratedContent(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Generated content cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Quality Assessment Tests")
    class QualityAssessmentTests {

        @Test
        @DisplayName("Should assess high quality content positively")
        void shouldAssessHighQualityContent() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId("content-1")
                    .prompt("Generate a product description")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .content("This is a comprehensive product description with excellent quality and sufficient detail.")
                    .status(GenerationStatus.COMPLETED)
                    .wordCount(15)
                    .relevanceScore(0.85)
                    .build();

            boolean meetsStandard = policy.meetsQualityStandards(content);

            assertThat(meetsStandard).isTrue();
        }

        @Test
        @DisplayName("Should fail low quality content")
        void shouldFailLowQualityContent() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId("content-1")
                    .prompt("Generate a product description")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .content("Short")
                    .status(GenerationStatus.COMPLETED)
                    .wordCount(1)
                    .relevanceScore(0.2)
                    .build();

            boolean meetsStandard = policy.meetsQualityStandards(content);

            assertThat(meetsStandard).isFalse();
        }

        @Test
        @DisplayName("Should fail incomplete content")
        void shouldFailIncompleteContent() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId("content-1")
                    .prompt("Generate a product description")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .status(GenerationStatus.PROCESSING)
                    .build();

            boolean meetsStandard = policy.meetsQualityStandards(content);

            assertThat(meetsStandard).isFalse();
        }

        @Test
        @DisplayName("Should filter contents by quality")
        void shouldFilterByQuality() {
            List<GeneratedContent> contents = List.of(
                    createContent("c1", 0.9, 100, GenerationStatus.COMPLETED),
                    createContent("c2", 0.3, 5, GenerationStatus.COMPLETED),
                    createContent("c3", 0.8, 50, GenerationStatus.COMPLETED)
            );

            List<GeneratedContent> filtered = policy.filterByQuality(contents);

            assertThat(filtered).hasSize(2);
            assertThat(filtered).allMatch(c -> c.getContentId().equals("c1") || c.getContentId().equals("c3"));
        }

        @Test
        @DisplayName("Should select best content")
        void shouldSelectBestContent() {
            List<GeneratedContent> contents = List.of(
                    createContent("c1", 0.7, 100, GenerationStatus.COMPLETED),
                    createContent("c2", 0.9, 150, GenerationStatus.COMPLETED),
                    createContent("c3", 0.8, 120, GenerationStatus.COMPLETED)
            );

            GeneratedContent best = policy.selectBestContent(contents);

            assertThat(best).isNotNull();
            assertThat(best.getContentId()).isEqualTo("c2");
        }
    }

    @Nested
    @DisplayName("Moderation Requirement Tests")
    class ModerationTests {

        @ParameterizedTest
        @ValueSource(strings = {"AD_COPY", "SOCIAL_MEDIA", "NEWS"})
        @DisplayName("Should require moderation for sensitive content types")
        void shouldRequireModeration(ContentType type) {
            assertThat(policy.requiresModeration(type)).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"PRODUCT_DESCRIPTION", "BLOG_POST", "EMAIL", "LANDING_PAGE", "FAQ", "REVIEW", "TUTORIAL"})
        @DisplayName("Should not require moderation for standard content types")
        void shouldNotRequireModeration(ContentType type) {
            assertThat(policy.requiresModeration(type)).isFalse();
        }
    }

    @Nested
    @DisplayName("Target Word Count Tests")
    class TargetWordCountTests {

        @Test
        @DisplayName("Should return correct target word count for each content type")
        void shouldReturnTargetWordCounts() {
            assertThat(policy.getTargetWordCount(ContentType.PRODUCT_DESCRIPTION)).isEqualTo(150);
            assertThat(policy.getTargetWordCount(ContentType.BLOG_POST)).isEqualTo(1000);
            assertThat(policy.getTargetWordCount(ContentType.SOCIAL_MEDIA)).isEqualTo(50);
            assertThat(policy.getTargetWordCount(ContentType.EMAIL)).isEqualTo(300);
            assertThat(policy.getTargetWordCount(ContentType.AD_COPY)).isEqualTo(100);
            assertThat(policy.getTargetWordCount(ContentType.LANDING_PAGE)).isEqualTo(500);
            assertThat(policy.getTargetWordCount(ContentType.FAQ)).isEqualTo(100);
            assertThat(policy.getTargetWordCount(ContentType.REVIEW)).isEqualTo(200);
            assertThat(policy.getTargetWordCount(ContentType.NEWS)).isEqualTo(500);
            assertThat(policy.getTargetWordCount(ContentType.TUTORIAL)).isEqualTo(1500);
        }
    }

    private GeneratedContent createContent(String id, double relevanceScore, int wordCount,
                                          GenerationStatus status) {
        return GeneratedContent.builder()
                .contentId(id)
                .prompt("Generate content")
                .contentType(ContentType.PRODUCT_DESCRIPTION)
                .content("Generated content text")
                .status(status)
                .wordCount(wordCount)
                .relevanceScore(relevanceScore)
                .createdAt(Instant.now())
                .build();
    }
}
