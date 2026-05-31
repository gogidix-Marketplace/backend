package com.gogidix.aiservices.aicontentgenerationservice.application.service;

import com.gogidix.aiservices.aicontentgenerationservice.application.dto.request.GenerateContentRequest;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.request.OptimizeContentRequest;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.response.ContentResponse;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.response.OptimizationResponse;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.*;
import com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.ContentGenerationPort;
import com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.ContentRepository;
import com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.EventPublisherPort;
import com.gogidix.aiservices.aicontentgenerationservice.domain.policy.ContentGenerationPolicy;
import com.gogidix.aiservices.aicontentgenerationservice.shared.exception.ContentGenerationException;
import com.gogidix.aiservices.aicontentgenerationservice.shared.exception.ContentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Content Generation Service Application Tests")
class ContentGenerationServiceTest {

    @Mock
    private ContentGenerationPort contentGenerationPort;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private EventPublisherPort eventPublisher;

    @Mock
    private ContentGenerationPolicy policy;

    @InjectMocks
    private ContentGenerationService contentGenerationService;

    private static final String USER_ID = "user-123";
    private static final String CONTENT_ID = "content-456";
    private static final String PROMPT = "Generate a compelling product description for a premium wireless headphone";

    @Nested
    @DisplayName("Content Generation Tests")
    class GenerationTests {

        @Test
        @DisplayName("Should generate content successfully")
        void shouldGenerateContent() {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .tone(ContentTone.PROFESSIONAL)
                    .language("en")
                    .build();

            // Port returns content with generated ID (simulating AI service response)
            GeneratedContent portResponse = GeneratedContent.builder()
                    .contentId("port-generated-id-123")
                    .content("Premium wireless headphones with exceptional sound quality.")
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .tone(ContentTone.PROFESSIONAL)
                    .language("en")
                    .status(GenerationStatus.COMPLETED)
                    .build();

            when(contentGenerationPort.generateContent(eq(PROMPT), eq(ContentType.PRODUCT_DESCRIPTION),
                    eq(ContentTone.PROFESSIONAL), eq("en"))).thenReturn(portResponse);
            when(contentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            ContentResponse response = contentGenerationService.generateContent(request);

            assertThat(response).isNotNull();
            assertThat(response.getContentId()).isNotNull();
            assertThat(response.getContentId()).matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
            assertThat(response.getContentType()).isEqualTo(ContentType.PRODUCT_DESCRIPTION);
            assertThat(response.getContent()).isEqualTo("Premium wireless headphones with exceptional sound quality.");
            verify(contentRepository, atLeastOnce()).save(any());
            verify(eventPublisher).publishContentGenerated(any(), eq(USER_ID), eq("product_description"));
        }

        @Test
        @DisplayName("Should use default tone when not provided")
        void shouldUseDefaultTone() {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            GeneratedContent mockContent = GeneratedContent.builder()
                    .contentId(CONTENT_ID)
                    .content("Generated content")
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .tone(ContentTone.NEUTRAL)
                    .language("en")
                    .status(GenerationStatus.COMPLETED)
                    .build();

            when(contentGenerationPort.generateContent(eq(PROMPT), eq(ContentType.PRODUCT_DESCRIPTION),
                    eq(ContentTone.NEUTRAL), eq("en"))).thenReturn(mockContent);
            when(contentRepository.save(any())).thenReturn(mockContent);

            contentGenerationService.generateContent(request);

            verify(contentGenerationPort).generateContent(eq(PROMPT), eq(ContentType.PRODUCT_DESCRIPTION),
                    eq(ContentTone.NEUTRAL), eq("en"));
        }

        @Test
        @DisplayName("Should handle generation failure")
        void shouldHandleGenerationFailure() {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            when(contentGenerationPort.generateContent(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException("AI service unavailable"));
            when(contentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            assertThatThrownBy(() -> contentGenerationService.generateContent(request))
                    .isInstanceOf(ContentGenerationException.class)
                    .hasMessageContaining("Failed to generate content");

            verify(eventPublisher).publishContentFailed(any(), eq(USER_ID), any());
        }

        @Test
        @DisplayName("Should validate prompt before generation")
        void shouldValidatePrompt() {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt("Short")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            doThrow(new IllegalArgumentException("Prompt must be at least 10 characters"))
                    .when(policy).validatePrompt("Short");

            assertThatThrownBy(() -> contentGenerationService.generateContent(request))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(contentGenerationPort, never()).generateContent(any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("Content Retrieval Tests")
    class RetrievalTests {

        @Test
        @DisplayName("Should get content by ID")
        void shouldGetContentById() {
            GeneratedContent mockContent = GeneratedContent.builder()
                    .contentId(CONTENT_ID)
                    .content("Generated content")
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .status(GenerationStatus.COMPLETED)
                    .build();

            when(contentRepository.findById(CONTENT_ID)).thenReturn(Optional.of(mockContent));

            ContentResponse response = contentGenerationService.getContent(CONTENT_ID);

            assertThat(response).isNotNull();
            assertThat(response.getContentId()).isEqualTo(CONTENT_ID);
            verify(contentRepository).findById(CONTENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when content not found")
        void shouldThrowWhenContentNotFound() {
            when(contentRepository.findById(CONTENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> contentGenerationService.getContent(CONTENT_ID))
                    .isInstanceOf(ContentNotFoundException.class)
                    .hasMessageContaining("Content not found");
        }

        @Test
        @DisplayName("Should get user content")
        void shouldGetUserContent() {
            List<GeneratedContent> mockContents = List.of(
                    GeneratedContent.builder()
                            .contentId(CONTENT_ID)
                            .content("Content 1")
                            .prompt(PROMPT)
                            .contentType(ContentType.PRODUCT_DESCRIPTION)
                            .status(GenerationStatus.COMPLETED)
                            .build(),
                    GeneratedContent.builder()
                            .contentId(CONTENT_ID + "2")
                            .content("Content 2")
                            .prompt("Another prompt")
                            .contentType(ContentType.BLOG_POST)
                            .status(GenerationStatus.COMPLETED)
                            .build()
            );

            when(contentRepository.findByUserId(USER_ID)).thenReturn(mockContents);

            List<ContentResponse> responses = contentGenerationService.getUserContent(USER_ID);

            assertThat(responses).hasSize(2);
            verify(contentRepository).findByUserId(USER_ID);
        }
    }

    @Nested
    @DisplayName("Content Optimization Tests")
    class OptimizationTests {

        @Test
        @DisplayName("Should optimize content successfully")
        void shouldOptimizeContent() {
            String originalContent = "This is the original content that needs optimization.";
            String optimizedContent = "This is the optimized version of your content.";

            OptimizeContentRequest request = OptimizeContentRequest.builder()
                    .content(originalContent)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .optimizationGoal("seo")
                    .build();

            when(contentGenerationPort.optimizeContent(eq(originalContent),
                    eq(ContentType.PRODUCT_DESCRIPTION), eq("seo"))).thenReturn(optimizedContent);

            OptimizationResponse response = contentGenerationService.optimizeContent(request);

            assertThat(response).isNotNull();
            assertThat(response.getOriginalContent()).isEqualTo(originalContent);
            assertThat(response.getOptimizedContent()).isEqualTo(optimizedContent);
            assertThat(response.getImprovements()).isNotEmpty();
        }

        @Test
        @DisplayName("Should reject empty content for optimization")
        void shouldRejectEmptyContent() {
            OptimizeContentRequest request = OptimizeContentRequest.builder()
                    .content("")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            assertThatThrownBy(() -> contentGenerationService.optimizeContent(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Content to optimize cannot be empty");
        }

        @Test
        @DisplayName("Should calculate word counts correctly")
        void shouldCalculateWordCounts() {
            String content = "This is a test content with seven words.";

            OptimizeContentRequest request = OptimizeContentRequest.builder()
                    .content(content)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .optimizationGoal("readability")
                    .build();

            String optimized = "This is optimized content with fewer words.";
            when(contentGenerationPort.optimizeContent(any(), any(), any())).thenReturn(optimized);

            OptimizationResponse response = contentGenerationService.optimizeContent(request);

            assertThat(response.getOriginalWordCount()).isEqualTo(8);
            assertThat(response.getOptimizedWordCount()).isEqualTo(7);
        }
    }

    @Nested
    @DisplayName("Content Deletion Tests")
    class DeletionTests {

        @Test
        @DisplayName("Should delete content successfully")
        void shouldDeleteContent() {
            GeneratedContent mockContent = GeneratedContent.builder()
                    .contentId(CONTENT_ID)
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            when(contentRepository.findById(CONTENT_ID)).thenReturn(Optional.of(mockContent));
            doNothing().when(contentRepository).delete(CONTENT_ID);

            contentGenerationService.deleteContent(CONTENT_ID);

            verify(contentRepository).delete(CONTENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent content")
        void shouldThrowWhenDeletingNonExistent() {
            when(contentRepository.findById(CONTENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> contentGenerationService.deleteContent(CONTENT_ID))
                    .isInstanceOf(ContentNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Variation Generation Tests")
    class VariationTests {

        @Test
        @DisplayName("Should generate content variations")
        void shouldGenerateVariations() {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .tone(ContentTone.PROFESSIONAL)
                    .variations(3)
                    .build();

            List<GeneratedContent> mockVariations = List.of(
                    GeneratedContent.builder()
                            .contentId(CONTENT_ID + "-1")
                            .content("Variation 1")
                            .prompt(PROMPT)
                            .contentType(ContentType.PRODUCT_DESCRIPTION)
                            .build(),
                    GeneratedContent.builder()
                            .contentId(CONTENT_ID + "-2")
                            .content("Variation 2")
                            .prompt(PROMPT)
                            .contentType(ContentType.PRODUCT_DESCRIPTION)
                            .build(),
                    GeneratedContent.builder()
                            .contentId(CONTENT_ID + "-3")
                            .content("Variation 3")
                            .prompt(PROMPT)
                            .contentType(ContentType.PRODUCT_DESCRIPTION)
                            .build()
            );

            when(contentGenerationPort.generateVariations(eq(PROMPT), eq(ContentType.PRODUCT_DESCRIPTION),
                    eq(ContentTone.PROFESSIONAL), eq("en"), eq(3))).thenReturn(mockVariations);
            when(contentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            ContentResponse response = contentGenerationService.generateVariations(request);

            assertThat(response).isNotNull();
            verify(contentGenerationPort).generateVariations(eq(PROMPT), eq(ContentType.PRODUCT_DESCRIPTION),
                    eq(ContentTone.PROFESSIONAL), eq("en"), eq(3));
        }

        @Test
        @DisplayName("Should limit variations to maximum")
        void shouldLimitVariations() {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .variations(10)
                    .build();

            when(contentGenerationPort.generateVariations(any(), any(), any(), any(), eq(5)))
                    .thenReturn(List.of(
                            GeneratedContent.builder().contentId("c1").content("V1").prompt(PROMPT).contentType(ContentType.PRODUCT_DESCRIPTION).build()
                    ));
            when(contentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            contentGenerationService.generateVariations(request);

            verify(contentGenerationPort).generateVariations(any(), any(), any(), any(), eq(5));
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle full workflow: generate, retrieve, optimize")
        void shouldHandleFullWorkflow() {
            GenerateContentRequest generateRequest = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .tone(ContentTone.PROFESSIONAL)
                    .build();

            GeneratedContent mockContent = GeneratedContent.builder()
                    .contentId(CONTENT_ID)
                    .content("Premium wireless headphones")
                    .prompt(PROMPT)
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .tone(ContentTone.PROFESSIONAL)
                    .language("en")
                    .status(GenerationStatus.COMPLETED)
                    .build();

            when(contentGenerationPort.generateContent(any(), any(), any(), any())).thenReturn(mockContent);
            when(contentRepository.save(any())).thenReturn(mockContent);
            when(contentRepository.findById(CONTENT_ID)).thenReturn(Optional.of(mockContent));

            ContentResponse generated = contentGenerationService.generateContent(generateRequest);
            assertThat(generated).isNotNull();

            ContentResponse retrieved = contentGenerationService.getContent(CONTENT_ID);
            assertThat(retrieved.getContentId()).isEqualTo(CONTENT_ID);

            OptimizeContentRequest optimizeRequest = OptimizeContentRequest.builder()
                    .content("Premium wireless headphones")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .optimizationGoal("seo")
                    .build();

            when(contentGenerationPort.optimizeContent(any(), any(), any())).thenReturn("Optimized premium wireless headphones");

            OptimizationResponse optimized = contentGenerationService.optimizeContent(optimizeRequest);
            assertThat(optimized).isNotNull();
        }
    }
}
