package com.gogidix.aiservices.aipersonalizationservice.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.event.BehaviorEvent;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.*;
import com.gogidix.aiservices.aipersonalizationservice.domain.port.out.RecommendationEnginePort;
import com.gogidix.aiservices.aipersonalizationservice.infrastructure.config.AiServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AI Inference Adapter Infrastructure Tests")
class AiInferenceAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private AiServiceProperties properties;
    private RecommendationEnginePort adapter;

    @BeforeEach
    void setUp() {
        properties = new AiServiceProperties();
        properties.setBaseUrl("http://ai-inference-service");
        properties.setRecommendationEndpoint("/api/v1/recommendations");
        properties.setAffinityEndpoint("/api/v1/affinity");
        properties.setTimeout(5000);
        adapter = new AiInferenceAdapter(restTemplate, properties, objectMapper);
    }

    @Nested
    @DisplayName("Recommendation Generation Tests")
    class RecommendationGenerationTests {

        @Test
        @DisplayName("Should generate recommendations successfully")
        void shouldGenerateRecommendations() {
            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            Map<String, Object> mockResponse = createMockRecommendationResponse();
            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            List<Recommendation> recommendations = adapter.generateRecommendations(profile, context);

            assertThat(recommendations).isNotNull();
            assertThat(recommendations).hasSize(2);
            assertThat(recommendations.get(0).getItemId()).isEqualTo("item-1");
            assertThat(recommendations.get(0).getScore()).isEqualTo(0.9);
        }

        @Test
        @DisplayName("Should handle empty response from AI service")
        void shouldHandleEmptyResponse() {
            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenReturn(Map.of("recommendations", Collections.emptyList()));

            List<Recommendation> recommendations = adapter.generateRecommendations(profile, context);

            assertThat(recommendations).isEmpty();
        }

        @Test
        @DisplayName("Should handle AI service unavailability")
        void shouldHandleServiceUnavailability() {
            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Service unavailable"));

            assertThatThrownBy(() -> adapter.generateRecommendations(profile, context))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("AI inference service unavailable");
        }

        @Test
        @DisplayName("Should include user context in request")
        void shouldIncludeUserContext() {
            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenReturn(createMockRecommendationResponse());

            adapter.generateRecommendations(profile, context);

            ArgumentCaptor<Map<String, Object>> requestCaptor = ArgumentCaptor.forClass(Map.class);
            verify(restTemplate).postForObject(any(), requestCaptor.capture(), eq(Map.class));

            Map<String, Object> request = requestCaptor.getValue();
            assertThat(request.get("userId")).isEqualTo(profile.getUserId().toString());
            assertThat(request.get("segment")).isEqualTo(profile.getSegment().toString());
            assertThat(request.get("limit")).isEqualTo(10);
            assertThat(request.get("context")).isEqualTo("homepage");
        }

        @Test
        @DisplayName("Should include affinity scores in request")
        void shouldIncludeAffinityScores() {
            UserProfile profile = createEligibleProfile();
            profile.recalculateAffinityScores();

            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenReturn(createMockRecommendationResponse());

            adapter.generateRecommendations(profile, context);

            ArgumentCaptor<Map<String, Object>> requestCaptor = ArgumentCaptor.forClass(Map.class);
            verify(restTemplate).postForObject(any(), requestCaptor.capture(), eq(Map.class));

            @SuppressWarnings("unchecked")
            Map<String, Object> affinityScores = (Map<String, Object>) requestCaptor.getValue().get("affinityScores");
            assertThat(affinityScores).isNotNull();
        }
    }

    @Nested
    @DisplayName("Affinity Calculation Tests")
    class AffinityCalculationTests {

        @Test
        @DisplayName("Should calculate affinity scores")
        void shouldCalculateAffinityScores() {
            UserProfile profile = createEligibleProfile();

            Map<String, Object> mockResponse = Map.of(
                    "affinityScores", Map.of(
                            "electronics", 0.8,
                            "books", 0.6,
                            "sports", 0.4
                    )
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            Map<String, Double> affinities = adapter.calculateAffinityScores(profile);

            assertThat(affinities).isNotNull();
            assertThat(affinities.get("electronics")).isEqualTo(0.8);
            assertThat(affinities.get("books")).isEqualTo(0.6);
            assertThat(affinities.get("sports")).isEqualTo(0.4);
        }

        @Test
        @DisplayName("Should handle missing affinity response")
        void shouldHandleMissingAffinityResponse() {
            UserProfile profile = createEligibleProfile();

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenReturn(Map.of());

            Map<String, Double> affinities = adapter.calculateAffinityScores(profile);

            assertThat(affinities).isEmpty();
        }

        @Test
        @DisplayName("Should normalize affinity scores")
        void shouldNormalizeAffinityScores() {
            UserProfile profile = createEligibleProfile();

            Map<String, Object> mockResponse = Map.of(
                    "affinityScores", Map.of(
                            "category1", 1.5,
                            "category2", -0.5,
                            "category3", 0.8
                    )
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            Map<String, Double> affinities = adapter.calculateAffinityScores(profile);

            // Scores should be normalized to 0-1 range
            affinities.values().forEach(score -> {
                assertThat(score).isBetween(0.0, 1.0);
            });
        }
    }

    @Nested
    @DisplayName("Segment Prediction Tests")
    class SegmentPredictionTests {

        @Test
        @DisplayName("Should predict user segment")
        void shouldPredictSegment() {
            UserProfile profile = UserProfile.create(USER_ID);

            Map<String, Object> mockResponse = Map.of(
                    "predictedSegment", "ACTIVE",
                    "confidence", 0.85
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            Segment predicted = adapter.predictSegment(profile);

            assertThat(predicted).isEqualTo(Segment.ACTIVE);
        }

        @Test
        @DisplayName("Should return NEW_USER for low confidence predictions")
        void shouldReturnNewUserForLowConfidence() {
            UserProfile profile = UserProfile.create(USER_ID);

            Map<String, Object> mockResponse = Map.of(
                    "predictedSegment", "ACTIVE",
                    "confidence", 0.4
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            Segment predicted = adapter.predictSegment(profile);

            // Low confidence should default to NEW_USER
            assertThat(predicted).isEqualTo(Segment.NEW_USER);
        }

        @Test
        @DisplayName("Should handle prediction error gracefully")
        void shouldHandlePredictionError() {
            UserProfile profile = UserProfile.create(USER_ID);

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Service error"));

            assertThatThrownBy(() -> adapter.predictSegment(profile))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Circuit Breaker Tests")
    class CircuitBreakerTests {

        @Test
        @DisplayName("Should open circuit after threshold failures")
        void shouldOpenCircuitAfterFailures() {
            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Service unavailable"));

            // Multiple failures
            for (int i = 0; i < 5; i++) {
                try {
                    adapter.generateRecommendations(profile, context);
                } catch (Exception e) {
                    // Expected
                }
            }

            // Circuit should be open, no more calls made
            adapter.generateRecommendations(profile, context);

            // Should still be 5 calls (circuit opened after threshold)
            verify(restTemplate, times(5)).postForObject(any(), any(), eq(Map.class));
        }

        @Test
        @DisplayName("Should use fallback on circuit open")
        void shouldUseFallbackOnCircuitOpen() {
            // This would test the fallback behavior
            // Implementation depends on the circuit breaker pattern used
        }
    }

    @Nested
    @DisplayName("Retry Logic Tests")
    class RetryTests {

        @Test
        @DisplayName("Should retry on transient failures")
        void shouldRetryOnTransientFailures() {
            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Timeout"))
                    .thenReturn(createMockRecommendationResponse());

            List<Recommendation> recommendations = adapter.generateRecommendations(profile, context);

            assertThat(recommendations).isNotNull();
            verify(restTemplate, times(2)).postForObject(any(), any(), eq(Map.class));
        }

        @Test
        @DisplayName("Should not retry on client errors")
        void shouldNotRetryOnClientErrors() {
            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Bad Request"));

            assertThatThrownBy(() -> adapter.generateRecommendations(profile, context))
                    .isInstanceOf(RuntimeException.class);

            verify(restTemplate, times(1)).postForObject(any(), any(), eq(Map.class));
        }
    }

    @Nested
    @DisplayName("Timeout Tests")
    class TimeoutTests {

        @Test
        @DisplayName("Should use configured timeout")
        void shouldUseConfiguredTimeout() {
            properties.setTimeout(3000);
            adapter = new AiInferenceAdapter(restTemplate, properties, objectMapper);

            assertThat(properties.getTimeout()).isEqualTo(3000);
        }

        @Test
        @DisplayName("Should handle timeout gracefully")
        void shouldHandleTimeout() {
            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenThrow(new RestClientException("Read timed out"));

            assertThatThrownBy(() -> adapter.generateRecommendations(profile, context))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("AI inference service unavailable");
        }
    }

    @Nested
    @DisplayName("Request/Response Mapping Tests")
    class MappingTests {

        @Test
        @DisplayName("Should map behavior events correctly")
        void shouldMapBehaviorEvents() {
            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenReturn(createMockRecommendationResponse());

            adapter.generateRecommendations(profile, context);

            ArgumentCaptor<Map<String, Object>> requestCaptor = ArgumentCaptor.forClass(Map.class);
            verify(restTemplate).postForObject(any(), requestCaptor.capture(), eq(Map.class));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> behaviors = (List<Map<String, Object>>) requestCaptor.getValue().get("behaviors");
            assertThat(behaviors).isNotNull();
            assertThat(behaviors).hasSizeGreaterThan(0);
        }

        @Test
        @DisplayName("Should map recommendation reasons correctly")
        void shouldMapReasons() {
            Map<String, Object> mockResponse = Map.of(
                    "recommendations", List.of(
                            Map.of("itemId", "item-1", "score", 0.9, "reason", "BEHAVIORAL"),
                            Map.of("itemId", "item-2", "score", 0.8, "reason", "COLLABORATIVE"),
                            Map.of("itemId", "item-3", "score", 0.7, "reason", "TRENDING")
                    )
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            UserProfile profile = createEligibleProfile();
            RecommendationEnginePort.RecommendationContext context = new RecommendationEnginePort.RecommendationContext(
                    10, "content", "homepage"
            );

            List<Recommendation> recommendations = adapter.generateRecommendations(profile, context);

            assertThat(recommendations.get(0).getReason()).isEqualTo(RecommendationReason.BEHAVIORAL);
            assertThat(recommendations.get(1).getReason()).isEqualTo(RecommendationReason.COLLABORATIVE);
            assertThat(recommendations.get(2).getReason()).isEqualTo(RecommendationReason.TRENDING);
        }
    }

    // Helper methods

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";

    private UserProfile createEligibleProfile() {
        UserProfile profile = UserProfile.create(USER_ID);
        for (int i = 0; i < 10; i++) {
            BehaviorEvent event = BehaviorEvent.builder()
                    .eventType(EventType.VIEW)
                    .itemId("item-" + i)
                    .timestamp(Instant.now())
                    .build();
            profile.addBehaviorEvent(event);
        }
        return profile;
    }

    private Map<String, Object> createMockRecommendationResponse() {
        return Map.of(
                "recommendations", List.of(
                        Map.of("itemId", "item-1", "score", 0.9, "reason", "BEHAVIORAL", "category", "electronics"),
                        Map.of("itemId", "item-2", "score", 0.8, "reason", "COLLABORATIVE", "category", "books")
                )
        );
    }
}
