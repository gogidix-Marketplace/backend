package com.gogidix.aiservices.aiproductrecommendationservice.application.service;

import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.RecommendationAnalysisResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductRecommendation;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationStatus;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecommendationAnalysisService Tests")
class RecommendationAnalysisServiceTest {

    @InjectMocks
    private RecommendationAnalysisService analysisService;

    private ProductRecommendation testRecommendation;
    private RecommendationCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = RecommendationCriteria.builder()
                .type(RecommendationCriteria.CriteriaType.CUSTOM)
                .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testRecommendation = ProductRecommendation.builder()
                .segmentId("segment-123")
                .tenantId("tenant-456")
                .name("Premium Products")
                .description("High value customers")
                .criteria(testCriteria)
                .status(RecommendationStatus.ACTIVE)
                .segmentType(RecommendationType.BEHAVIORAL)
                .customerCount(500L)
                .lastAnalyzedAt(Instant.now().minusSeconds(3600))
                .build();
    }

    @Nested
    @DisplayName("analyzeRecommendation() Tests")
    class AnalyzeRecommendationTests {

        @Test
        @DisplayName("Should analyze segment with high confidence")
        void shouldAnalyzeWithHighConfidence() {
            Map<String, Object> options = Map.of("includeDemographics", true);

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(testRecommendation, options);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo("segment-123");
            assertThat(result.customerCount()).isEqualTo(500);
            assertThat(result.confidenceScore()).isEqualTo(0.8);
            assertThat(result.demographics()).isNotNull();
            assertThat(result.behaviors()).isNotNull();
            assertThat(result.transactions()).isNotNull();
            assertThat(result.predictions()).isNotNull();
            assertThat(result.recommendations()).isNotNull();
        }

        @Test
        @DisplayName("Should analyze segment with medium customer count")
        void shouldAnalyzeMediumRecommendation() {
            ProductRecommendation mediumRecommendation = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Medium Recommendation")
                    .criteria(testCriteria)
                    .customerCount(150L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(mediumRecommendation, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should analyze segment with low customer count")
        void shouldAnalyzeSmallRecommendation() {
            ProductRecommendation smallRecommendation = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Recommendation")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(smallRecommendation, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should analyze segment with very low customer count")
        void shouldAnalyzeVerySmallRecommendation() {
            ProductRecommendation tinyRecommendation = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Tiny Recommendation")
                    .criteria(testCriteria)
                    .customerCount(5L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(tinyRecommendation, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should analyze segment with empty options")
        void shouldAnalyzeWithEmptyOptions() {
            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(testRecommendation, Map.of());

            assertThat(result).isNotNull();
            assertThat(result.customerCount()).isEqualTo(500);
        }

        @Test
        @DisplayName("Should analyze segment with null options")
        void shouldAnalyzeWithNullOptions() {
            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(testRecommendation, null);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should include demographic analysis")
        void shouldIncludeDemographicAnalysis() {
            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(testRecommendation, Map.of());

            assertThat(result.demographics()).isNotNull();
            assertThat(result.demographics()).containsKey("field");
            assertThat(result.demographics()).containsKey("operator");
            assertThat(result.demographics()).containsKey("customerCount");
            assertThat(result.demographics().get("field")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include behavioral analysis")
        void shouldIncludeBehavioralAnalysis() {
            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(testRecommendation, Map.of());

            assertThat(result.behaviors()).isNotNull();
            assertThat(result.behaviors()).containsKey("criteriaField");
            assertThat(result.behaviors()).containsKey("criteriaValue");
            assertThat(result.behaviors().get("criteriaField")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include transaction analysis")
        void shouldIncludeTransactionAnalysis() {
            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(testRecommendation, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions()).containsKey("customerCount");
            assertThat(result.transactions()).containsKey("lastAnalyzed");
        }

        @Test
        @DisplayName("Should include predictions")
        void shouldIncludePredictions() {
            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(testRecommendation, Map.of());

            assertThat(result.predictions()).isNotNull();
            assertThat(result.predictions()).containsKey("churnRisk");
            assertThat(result.predictions()).containsKey("upsellPotential");
            assertThat(result.predictions()).containsKey("predictedGrowthRate");
        }

        @Test
        @DisplayName("Should include recommendations")
        void shouldIncludeRecommendations() {
            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(testRecommendation, Map.of());

            assertThat(result.recommendations()).isNotNull();
            assertThat(result.recommendations()).containsKey("optimalContactFrequency");
            assertThat(result.recommendations()).containsKey("recommendedOfferType");
        }

        @Test
        @DisplayName("Should recommend expansion for small segments")
        void shouldRecommendExpansionForSmallRecommendations() {
            ProductRecommendation smallRecommendation = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Recommendation")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(smallRecommendation, Map.of());

            assertThat(result.recommendations()).containsKey("expansionSuggestion");
        }

        @Test
        @DisplayName("Should recommend refinement for very large segments")
        void shouldRecommendRefinementForLargeRecommendations() {
            ProductRecommendation largeRecommendation = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Recommendation")
                    .criteria(testCriteria)
                    .customerCount(150000L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(largeRecommendation, Map.of());

            assertThat(result.recommendations()).containsKey("refinementSuggestion");
        }

        @Test
        @DisplayName("Should predict low churn risk for large segments")
        void shouldPredictLowChurnForLargeRecommendations() {
            ProductRecommendation largeRecommendation = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Recommendation")
                    .criteria(testCriteria)
                    .customerCount(500L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(largeRecommendation, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("low");
        }

        @Test
        @DisplayName("Should predict variable churn risk for small segments")
        void shouldPredictVariableChurnForSmallRecommendations() {
            ProductRecommendation smallRecommendation = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Recommendation")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(smallRecommendation, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("variable");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle segment with zero customers")
        void shouldHandleZeroProducts() {
            ProductRecommendation emptyRecommendation = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Empty Recommendation")
                    .criteria(testCriteria)
                    .customerCount(0L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(emptyRecommendation, Map.of());

            assertThat(result.customerCount()).isEqualTo(0);
            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should handle segment with null last analyzed date")
        void shouldHandleNullLastAnalyzed() {
            ProductRecommendation segment = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Recommendation")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .lastAnalyzedAt(null)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(segment, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions().get("lastAnalyzed")).isEqualTo("Never");
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 10 customers")
        void shouldCalculateConfidenceAtBoundary10() {
            ProductRecommendation segment = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Recommendation")
                    .criteria(testCriteria)
                    .customerCount(10L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 100 customers")
        void shouldCalculateConfidenceAtBoundary100() {
            ProductRecommendation segment = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Recommendation")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 1000 customers")
        void shouldCalculateConfidenceAtBoundary1000() {
            ProductRecommendation segment = ProductRecommendation.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Recommendation")
                    .criteria(testCriteria)
                    .customerCount(1000L)
                    .build();

            RecommendationAnalysisResponseDto result = analysisService.analyzeRecommendation(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.95);
        }
    }
}
