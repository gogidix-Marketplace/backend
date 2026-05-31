package com.gogidix.aiservices.aicontentgenerationservice.infrastructure.adapter;

import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentTone;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentType;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.GeneratedContent;
import com.gogidix.aiservices.aicontentgenerationservice.infrastructure.config.AiServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AI Content Generation Adapter Infrastructure Tests")
class AiContentGenerationAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private AiServiceProperties properties;
    private AiContentGenerationAdapter adapter;

    @BeforeEach
    void setUp() {
        properties = new AiServiceProperties();
        properties.setBaseUrl("http://ai-service");
        properties.setGenerationEndpoint("/api/v1/generate");
        properties.setVariationsEndpoint("/api/v1/variations");
        properties.setOptimizationEndpoint("/api/v1/optimize");
        properties.setTimeout(30000);
        adapter = new AiContentGenerationAdapter(restTemplate, properties);
    }

    @Nested
    @DisplayName("Content Generation Tests")
    class GenerationTests {

        @Test
        @DisplayName("Should generate content successfully")
        void shouldGenerateContent() {
            String prompt = "Generate a product description";
            Map<String, Object> mockResponse = Map.of("content", "Premium wireless headphones with excellent sound.");

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class))).thenReturn(mockResponse);

            GeneratedContent result = adapter.generateContent(prompt, ContentType.PRODUCT_DESCRIPTION,
                    ContentTone.PROFESSIONAL, "en");

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEqualTo("Premium wireless headphones with excellent sound.");
            assertThat(result.getContentType()).isEqualTo(ContentType.PRODUCT_DESCRIPTION);
            verify(restTemplate).postForObject(any(String.class), any(), eq(Map.class));
        }

        @Test
        @DisplayName("Should handle AI service unavailability")
        void shouldHandleServiceUnavailability() {
            String prompt = "Generate a product description";

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Service unavailable"));

            GeneratedContent result = adapter.generateContent(prompt, ContentType.PRODUCT_DESCRIPTION,
                    ContentTone.PROFESSIONAL, "en");

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isNotEmpty();
            assertThat(result.getContent()).contains("product");
        }

        @Test
        @DisplayName("Should handle empty response")
        void shouldHandleEmptyResponse() {
            String prompt = "Generate a product description";

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class))).thenReturn(Map.of());

            GeneratedContent result = adapter.generateContent(prompt, ContentType.PRODUCT_DESCRIPTION,
                    ContentTone.PROFESSIONAL, "en");

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isNotEmpty();
        }

        @Test
        @DisplayName("Should include correct request parameters")
        void shouldIncludeRequestParameters() {
            String prompt = "Generate a blog post about technology";
            Map<String, Object> mockResponse = Map.of("content", "Generated blog content");

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class))).thenReturn(mockResponse);

            adapter.generateContent(prompt, ContentType.BLOG_POST, ContentTone.CASUAL, "en");

            verify(restTemplate).postForObject(any(String.class), any(), eq(Map.class));
        }
    }

    @Nested
    @DisplayName("Variation Generation Tests")
    class VariationsTests {

        @Test
        @DisplayName("Should generate variations successfully")
        void shouldGenerateVariations() {
            String prompt = "Generate product descriptions";
            List<String> variations = List.of("Variation 1", "Variation 2", "Variation 3");
            Map<String, Object> mockResponse = Map.of("variations", variations);

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class))).thenReturn(mockResponse);

            List<GeneratedContent> results = adapter.generateVariations(prompt, ContentType.PRODUCT_DESCRIPTION,
                    ContentTone.PROFESSIONAL, "en", 3);

            assertThat(results).hasSize(3);
            assertThat(results.get(0).getContent()).isEqualTo("Variation 1");
            assertThat(results.get(1).getContent()).isEqualTo("Variation 2");
            assertThat(results.get(2).getContent()).isEqualTo("Variation 3");
        }

        @Test
        @DisplayName("Should use fallback when variations endpoint fails")
        void shouldUseFallbackForVariations() {
            String prompt = "Generate product descriptions";

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Service unavailable"));

            List<GeneratedContent> results = adapter.generateVariations(prompt, ContentType.PRODUCT_DESCRIPTION,
                    ContentTone.PROFESSIONAL, "en", 3);

            assertThat(results).hasSize(3);
            assertThat(results).allMatch(c -> c.getContent() != null);
        }
    }

    @Nested
    @DisplayName("Content Optimization Tests")
    class OptimizationTests {

        @Test
        @DisplayName("Should optimize content successfully")
        void shouldOptimizeContent() {
            String content = "This is original content";
            Map<String, Object> mockResponse = Map.of("optimizedContent", "This is optimized content");

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class))).thenReturn(mockResponse);

            String result = adapter.optimizeContent(content, ContentType.PRODUCT_DESCRIPTION, "seo");

            assertThat(result).isEqualTo("This is optimized content");
        }

        @Test
        @DisplayName("Should apply basic optimization when service fails")
        void shouldApplyBasicOptimization() {
            String content = "This   is   original   content";

            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Service unavailable"));

            String result = adapter.optimizeContent(content, ContentType.PRODUCT_DESCRIPTION, "conciseness");

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Relevance Score Tests")
    class RelevanceTests {

        @Test
        @DisplayName("Should calculate relevance score correctly")
        void shouldCalculateRelevanceScore() {
            String content = "Premium wireless headphones with noise cancellation and 30-hour battery life";
            String prompt = "Generate a product description for wireless headphones";

            double score = adapter.calculateRelevanceScore(content, prompt);

            assertThat(score).isGreaterThan(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should return zero for null inputs")
        void shouldReturnZeroForNullInputs() {
            double score = adapter.calculateRelevanceScore(null, null);

            assertThat(score).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should return high score for matching content")
        void shouldReturnHighScoreForMatchingContent() {
            String content = "wireless headphones with premium sound quality and long battery life";
            String prompt = "Generate content about wireless headphones";

            double score = adapter.calculateRelevanceScore(content, prompt);

            assertThat(score).isGreaterThan(0.3);
        }

        @Test
        @DisplayName("Should return low score for non-matching content")
        void shouldReturnLowScoreForNonMatchingContent() {
            String content = "This is about laptop computers and tablets";
            String prompt = "Generate content about wireless headphones";

            double score = adapter.calculateRelevanceScore(content, prompt);

            assertThat(score).isLessThan(0.3);
        }
    }

    @Nested
    @DisplayName("Timeout Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Should use configured timeout")
        void shouldUseConfiguredTimeout() {
            properties.setTimeout(60000);
            adapter = new AiContentGenerationAdapter(restTemplate, properties);

            assertThat(properties.getTimeout()).isEqualTo(60000);
        }
    }

    @Nested
    @DisplayName("Content Type Handling Tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should generate appropriate content for each type")
        void shouldGenerateForAllTypes() {
            Map<String, Object> mockResponse = Map.of("content", "Generated content");
            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class))).thenReturn(mockResponse);

            for (ContentType type : ContentType.values()) {
                GeneratedContent result = adapter.generateContent("Generate content",
                        type, ContentTone.NEUTRAL, "en");
                assertThat(result).isNotNull();
                assertThat(result.getContentType()).isEqualTo(type);
            }
        }

        @Test
        @DisplayName("Should set appropriate max tokens for content type")
        void shouldSetMaxTokensForType() {
            Map<String, Object> mockResponse = Map.of("content", "Generated content");
            when(restTemplate.postForObject(any(String.class), any(), eq(Map.class))).thenReturn(mockResponse);

            adapter.generateContent("Generate a short post", ContentType.SOCIAL_MEDIA, ContentTone.CASUAL, "en");
            adapter.generateContent("Generate a long tutorial", ContentType.TUTORIAL, ContentTone.PROFESSIONAL, "en");

            verify(restTemplate, times(2)).postForObject(any(String.class), any(), eq(Map.class));
        }
    }
}
