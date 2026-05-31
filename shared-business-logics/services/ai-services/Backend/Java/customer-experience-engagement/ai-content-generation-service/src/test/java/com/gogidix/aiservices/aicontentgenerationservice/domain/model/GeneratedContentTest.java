package com.gogidix.aiservices.aicontentgenerationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("GeneratedContent Domain Model Tests")
class GeneratedContentTest {

    private static final String VALID_CONTENT_ID = "content-123";
    private static final String VALID_PROMPT = "Generate a product description for a premium wireless headphone";
    private static final ContentType VALID_TYPE = ContentType.PRODUCT_DESCRIPTION;
    private static final ContentTone VALID_TONE = ContentTone.PROFESSIONAL;

    @Nested
    @DisplayName("Content Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create content with valid parameters")
        void shouldCreateWithValidParameters() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .tone(VALID_TONE)
                    .language("en")
                    .build();

            assertThat(content).isNotNull();
            assertThat(content.getContentId()).isEqualTo(VALID_CONTENT_ID);
            assertThat(content.getPrompt()).isEqualTo(VALID_PROMPT);
            assertThat(content.getContentType()).isEqualTo(VALID_TYPE);
            assertThat(content.getTone()).isEqualTo(VALID_TONE);
            assertThat(content.getLanguage()).isEqualTo("en");
        }

        @Test
        @DisplayName("Should reject null content ID")
        void shouldRejectNullContentId() {
            assertThatThrownBy(() -> GeneratedContent.builder()
                    .contentId(null)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Content ID cannot be null");
        }

        @Test
        @DisplayName("Should reject empty content ID")
        void shouldRejectEmptyContentId() {
            assertThatThrownBy(() -> GeneratedContent.builder()
                    .contentId("")
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Content ID cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject null content type")
        void shouldRejectNullContentType() {
            assertThatThrownBy(() -> GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(null)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Content type cannot be null");
        }

        @Test
        @DisplayName("Should reject null prompt")
        void shouldRejectNullPrompt() {
            assertThatThrownBy(() -> GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(null)
                    .contentType(VALID_TYPE)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Prompt cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject empty prompt")
        void shouldRejectEmptyPrompt() {
            assertThatThrownBy(() -> GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt("")
                    .contentType(VALID_TYPE)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Prompt cannot be null or empty");
        }

        @Test
        @DisplayName("Should set default status to PENDING")
        void shouldSetDefaultStatus() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            assertThat(content.getStatus()).isEqualTo(GenerationStatus.PENDING);
        }

        @Test
        @DisplayName("Should set default quality to MEDIUM")
        void shouldSetDefaultQuality() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            assertThat(content.getQuality()).isEqualTo(ContentQuality.MEDIUM);
        }
    }

    @Nested
    @DisplayName("Status Management Tests")
    class StatusTests {

        @Test
        @DisplayName("Should mark content as processing")
        void shouldMarkAsProcessing() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            content.markAsProcessing();

            assertThat(content.getStatus()).isEqualTo(GenerationStatus.PROCESSING);
            assertThat(content.isProcessing()).isTrue();
            assertThat(content.isPending()).isFalse();
        }

        @Test
        @DisplayName("Should mark content as completed")
        void shouldMarkAsCompleted() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            String generatedText = "This is a generated product description with great details.";
            content.markAsCompleted(generatedText);

            assertThat(content.getStatus()).isEqualTo(GenerationStatus.COMPLETED);
            assertThat(content.isCompleted()).isTrue();
            assertThat(content.getContent()).isEqualTo(generatedText);
            assertThat(content.getCompletedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should mark content as failed")
        void shouldMarkAsFailed() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            String error = "AI service unavailable";
            content.markAsFailed(error);

            assertThat(content.getStatus()).isEqualTo(GenerationStatus.FAILED);
            assertThat(content.isFailed()).isTrue();
            assertThat(content.getErrorMessage()).isEqualTo(error);
            assertThat(content.getCompletedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should identify pending status correctly")
        void shouldIdentifyPendingStatus() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            assertThat(content.isPending()).isTrue();
            assertThat(content.isProcessing()).isFalse();
            assertThat(content.isCompleted()).isFalse();
            assertThat(content.isFailed()).isFalse();
        }
    }

    @Nested
    @DisplayName("Quality Assessment Tests")
    class QualityTests {

        @ParameterizedTest
        @EnumSource(ContentQuality.class)
        @DisplayName("Should check quality threshold correctly")
        void shouldCheckQualityThreshold(ContentQuality threshold) {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .quality(ContentQuality.HIGH)
                    .build();

            boolean meetsThreshold = content.meetsQualityThreshold(threshold);

            assertThat(meetsThreshold).isEqualTo(ContentQuality.HIGH.ordinal() >= threshold.ordinal());
        }

        @Test
        @DisplayName("Should check relevance score existence")
        void shouldCheckRelevanceScore() {
            GeneratedContent contentWithScore = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .relevanceScore(0.85)
                    .build();

            GeneratedContent contentWithoutScore = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID + "2")
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            assertThat(contentWithScore.hasRelevanceScore()).isTrue();
            assertThat(contentWithoutScore.hasRelevanceScore()).isFalse();
        }

        @Test
        @DisplayName("Should check relevance threshold correctly")
        void shouldCheckRelevanceThreshold() {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .relevanceScore(0.75)
                    .build();

            assertThat(content.meetsRelevanceThreshold(0.7)).isTrue();
            assertThat(content.meetsRelevanceThreshold(0.8)).isFalse();
        }
    }

    @Nested
    @DisplayName("Content Transformation Tests")
    class TransformationTests {

        @Test
        @DisplayName("Should create new instance with updated content")
        void shouldCreateWithUpdatedContent() {
            GeneratedContent original = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .content("Original content")
                    .build();

            String newContent = "Updated content";
            GeneratedContent updated = original.withContent(newContent);

            assertThat(original).isNotSameAs(updated);
            assertThat(original.getContent()).isEqualTo("Original content");
            assertThat(updated.getContent()).isEqualTo(newContent);
            assertThat(updated.getContentId()).isEqualTo(original.getContentId());
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal based on content ID")
        void shouldBeEqualBasedOnId() {
            GeneratedContent content1 = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            GeneratedContent content2 = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt("Different prompt")
                    .contentType(ContentType.BLOG_POST)
                    .build();

            assertThat(content1).isEqualTo(content2);
            assertThat(content1.hashCode()).isEqualTo(content2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal with different IDs")
        void shouldNotBeEqualWithDifferentIds() {
            GeneratedContent content1 = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            GeneratedContent content2 = GeneratedContent.builder()
                    .contentId("different-id")
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .build();

            assertThat(content1).isNotEqualTo(content2);
        }
    }

    @Nested
    @DisplayName("ContentType Tests")
    class ContentTypeTests {

        @ParameterizedTest
        @EnumSource(ContentType.class)
        @DisplayName("Should accept all content types")
        void shouldAcceptAllContentTypes(ContentType type) {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(type)
                    .build();

            assertThat(content.getContentType()).isEqualTo(type);
        }

        @Test
        @DisplayName("Should parse content type from string")
        void shouldParseContentTypeFromString() {
            assertThat(ContentType.fromString("product_description")).isEqualTo(ContentType.PRODUCT_DESCRIPTION);
            assertThat(ContentType.fromString("blog_post")).isEqualTo(ContentType.BLOG_POST);
            assertThat(ContentType.fromString("social_media")).isEqualTo(ContentType.SOCIAL_MEDIA);
        }

        @Test
        @DisplayName("Should throw exception for invalid content type")
        void shouldThrowForInvalidContentType() {
            assertThatThrownBy(() -> ContentType.fromString("invalid_type"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Unknown content type");
        }
    }

    @Nested
    @DisplayName("ContentTone Tests")
    class ContentToneTests {

        @ParameterizedTest
        @EnumSource(ContentTone.class)
        @DisplayName("Should accept all content tones")
        void shouldAcceptAllContentTones(ContentTone tone) {
            GeneratedContent content = GeneratedContent.builder()
                    .contentId(VALID_CONTENT_ID)
                    .prompt(VALID_PROMPT)
                    .contentType(VALID_TYPE)
                    .tone(tone)
                    .build();

            assertThat(content.getTone()).isEqualTo(tone);
        }

        @Test
        @DisplayName("Should parse content tone from string")
        void shouldParseContentToneFromString() {
            assertThat(ContentTone.fromString("professional")).isEqualTo(ContentTone.PROFESSIONAL);
            assertThat(ContentTone.fromString("casual")).isEqualTo(ContentTone.CASUAL);
            assertThat(ContentTone.fromString("neutral")).isEqualTo(ContentTone.NEUTRAL);
        }
    }

    @Nested
    @DisplayName("ContentTemplate Tests")
    class ContentTemplateTests {

        @Test
        @DisplayName("Should apply template parameters")
        void shouldApplyTemplateParameters() {
            String template = "Dear {{name}}, thank you for your interest in {{product}}.";
            ContentTemplate contentTemplate = ContentTemplate.builder()
                    .templateId("template-1")
                    .name("Email Template")
                    .contentType(ContentType.EMAIL)
                    .template(template)
                    .defaultParameters(Map.of("name", "Customer", "product", "our service"))
                    .isActive(true)
                    .build();

            String result = contentTemplate.applyParameters(Map.of("name", "John", "product", "Premium Plan"));

            assertThat(result).contains("John");
            assertThat(result).contains("Premium Plan");
        }

        @Test
        @DisplayName("Should use default parameters when not provided")
        void shouldUseDefaultParameters() {
            String template = "Welcome {{name}}!";
            ContentTemplate contentTemplate = ContentTemplate.builder()
                    .templateId("template-1")
                    .name("Greeting Template")
                    .contentType(ContentType.SOCIAL_MEDIA)
                    .template(template)
                    .defaultParameters(Map.of("name", "User"))
                    .isActive(true)
                    .build();

            String result = contentTemplate.applyParameters(Map.of());

            assertThat(result).isEqualTo("Welcome User!");
        }

        @Test
        @DisplayName("Should reject template with null ID")
        void shouldRejectNullTemplateId() {
            assertThatThrownBy(() -> ContentTemplate.builder()
                    .templateId(null)
                    .name("Template")
                    .contentType(ContentType.EMAIL)
                    .template("Some template")
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Template ID cannot be null");
        }
    }
}
