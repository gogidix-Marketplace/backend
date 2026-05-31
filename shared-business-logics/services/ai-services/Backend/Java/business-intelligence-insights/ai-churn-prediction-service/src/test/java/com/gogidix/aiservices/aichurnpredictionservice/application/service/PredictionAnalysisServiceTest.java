package com.gogidix.aiservices.aichurnpredictionservice.application.service;

import com.gogidix.aiservices.aichurnpredictionservice.application.dto.PredictionAnalysisResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.ChurnPrediction;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionStatus;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
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
@DisplayName("PredictionAnalysisService Tests")
class PredictionAnalysisServiceTest {

    @InjectMocks
    private PredictionAnalysisService analysisService;

    private ChurnPrediction testPrediction;
    private PredictionCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = PredictionCriteria.builder()
                .type(PredictionCriteria.CriteriaType.CUSTOM)
                .operator(PredictionCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testPrediction = ChurnPrediction.builder()
                .segmentId("segment-123")
                .tenantId("tenant-456")
                .name("Premium Customers")
                .description("High value customers")
                .criteria(testCriteria)
                .status(PredictionStatus.ACTIVE)
                .segmentType(PredictionType.BEHAVIORAL)
                .customerCount(500L)
                .lastAnalyzedAt(Instant.now().minusSeconds(3600))
                .build();
    }

    @Nested
    @DisplayName("analyzePrediction() Tests")
    class AnalyzePredictionTests {

        @Test
        @DisplayName("Should analyze segment with high confidence")
        void shouldAnalyzeWithHighConfidence() {
            Map<String, Object> options = Map.of("includeDemographics", true);

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(testPrediction, options);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo("segment-123");
            assertThat(result.customerCount()).isEqualTo(500);
            assertThat(result.confidenceScore()).isEqualTo(0.95);
            assertThat(result.demographics()).isNotNull();
            assertThat(result.behaviors()).isNotNull();
            assertThat(result.transactions()).isNotNull();
            assertThat(result.predictions()).isNotNull();
            assertThat(result.recommendations()).isNotNull();
        }

        @Test
        @DisplayName("Should analyze segment with medium customer count")
        void shouldAnalyzeMediumPrediction() {
            ChurnPrediction mediumPrediction = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Medium Prediction")
                    .criteria(testCriteria)
                    .customerCount(150L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(mediumPrediction, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should analyze segment with low customer count")
        void shouldAnalyzeSmallPrediction() {
            ChurnPrediction smallPrediction = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Prediction")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(smallPrediction, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should analyze segment with very low customer count")
        void shouldAnalyzeVerySmallPrediction() {
            ChurnPrediction tinyPrediction = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Tiny Prediction")
                    .criteria(testCriteria)
                    .customerCount(5L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(tinyPrediction, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should analyze segment with empty options")
        void shouldAnalyzeWithEmptyOptions() {
            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(testPrediction, Map.of());

            assertThat(result).isNotNull();
            assertThat(result.customerCount()).isEqualTo(500);
        }

        @Test
        @DisplayName("Should analyze segment with null options")
        void shouldAnalyzeWithNullOptions() {
            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(testPrediction, null);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should include demographic analysis")
        void shouldIncludeDemographicAnalysis() {
            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(testPrediction, Map.of());

            assertThat(result.demographics()).isNotNull();
            assertThat(result.demographics()).containsKey("field");
            assertThat(result.demographics()).containsKey("operator");
            assertThat(result.demographics()).containsKey("customerCount");
            assertThat(result.demographics().get("field")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include behavioral analysis")
        void shouldIncludeBehavioralAnalysis() {
            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(testPrediction, Map.of());

            assertThat(result.behaviors()).isNotNull();
            assertThat(result.behaviors()).containsKey("criteriaField");
            assertThat(result.behaviors()).containsKey("criteriaValue");
            assertThat(result.behaviors().get("criteriaField")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include transaction analysis")
        void shouldIncludeTransactionAnalysis() {
            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(testPrediction, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions()).containsKey("customerCount");
            assertThat(result.transactions()).containsKey("lastAnalyzed");
        }

        @Test
        @DisplayName("Should include predictions")
        void shouldIncludePredictions() {
            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(testPrediction, Map.of());

            assertThat(result.predictions()).isNotNull();
            assertThat(result.predictions()).containsKey("churnRisk");
            assertThat(result.predictions()).containsKey("upsellPotential");
            assertThat(result.predictions()).containsKey("predictedGrowthRate");
        }

        @Test
        @DisplayName("Should include recommendations")
        void shouldIncludeRecommendations() {
            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(testPrediction, Map.of());

            assertThat(result.recommendations()).isNotNull();
            assertThat(result.recommendations()).containsKey("optimalContactFrequency");
            assertThat(result.recommendations()).containsKey("recommendedOfferType");
        }

        @Test
        @DisplayName("Should recommend expansion for small segments")
        void shouldRecommendExpansionForSmallPredictions() {
            ChurnPrediction smallPrediction = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Prediction")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(smallPrediction, Map.of());

            assertThat(result.recommendations()).containsKey("expansionSuggestion");
        }

        @Test
        @DisplayName("Should recommend refinement for very large segments")
        void shouldRecommendRefinementForLargePredictions() {
            ChurnPrediction largePrediction = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Prediction")
                    .criteria(testCriteria)
                    .customerCount(150000L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(largePrediction, Map.of());

            assertThat(result.recommendations()).containsKey("refinementSuggestion");
        }

        @Test
        @DisplayName("Should predict low churn risk for large segments")
        void shouldPredictLowChurnForLargePredictions() {
            ChurnPrediction largePrediction = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Prediction")
                    .criteria(testCriteria)
                    .customerCount(500L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(largePrediction, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("low");
        }

        @Test
        @DisplayName("Should predict variable churn risk for small segments")
        void shouldPredictVariableChurnForSmallPredictions() {
            ChurnPrediction smallPrediction = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Prediction")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(smallPrediction, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("variable");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle segment with zero customers")
        void shouldHandleZeroCustomers() {
            ChurnPrediction emptyPrediction = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Empty Prediction")
                    .criteria(testCriteria)
                    .customerCount(0L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(emptyPrediction, Map.of());

            assertThat(result.customerCount()).isEqualTo(0);
            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should handle segment with null last analyzed date")
        void shouldHandleNullLastAnalyzed() {
            ChurnPrediction segment = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Prediction")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .lastAnalyzedAt(null)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(segment, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions().get("lastAnalyzed")).isEqualTo("Never");
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 10 customers")
        void shouldCalculateConfidenceAtBoundary10() {
            ChurnPrediction segment = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Prediction")
                    .criteria(testCriteria)
                    .customerCount(10L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 100 customers")
        void shouldCalculateConfidenceAtBoundary100() {
            ChurnPrediction segment = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Prediction")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 1000 customers")
        void shouldCalculateConfidenceAtBoundary1000() {
            ChurnPrediction segment = ChurnPrediction.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Prediction")
                    .criteria(testCriteria)
                    .customerCount(1000L)
                    .build();

            PredictionAnalysisResponseDto result = analysisService.analyzePrediction(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.95);
        }
    }
}
