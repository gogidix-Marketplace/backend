package com.gogidix.aiservices.aisalesforecastingservice.application.service;

import com.gogidix.aiservices.aisalesforecastingservice.application.dto.ForecastAnalysisResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.SalesForecast;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastStatus;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
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
@DisplayName("ForecastAnalysisService Tests")
class ForecastAnalysisServiceTest {

    @InjectMocks
    private ForecastAnalysisService analysisService;

    private SalesForecast testForecast;
    private ForecastCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = ForecastCriteria.builder()
                .type(ForecastCriteria.CriteriaType.CUSTOM)
                .operator(ForecastCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testForecast = SalesForecast.builder()
                .segmentId("segment-123")
                .tenantId("tenant-456")
                .name("Premium ForecastModels")
                .description("High value customers")
                .criteria(testCriteria)
                .status(ForecastStatus.ACTIVE)
                .segmentType(ForecastType.BEHAVIORAL)
                .customerCount(500L)
                .lastAnalyzedAt(Instant.now().minusSeconds(3600))
                .build();
    }

    @Nested
    @DisplayName("analyzeForecast() Tests")
    class AnalyzeForecastTests {

        @Test
        @DisplayName("Should analyze segment with high confidence")
        void shouldAnalyzeWithHighConfidence() {
            Map<String, Object> options = Map.of("includeDemographics", true);

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(testForecast, options);

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
        void shouldAnalyzeMediumForecast() {
            SalesForecast mediumForecast = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Medium Forecast")
                    .criteria(testCriteria)
                    .customerCount(150L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(mediumForecast, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should analyze segment with low customer count")
        void shouldAnalyzeSmallForecast() {
            SalesForecast smallForecast = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Forecast")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(smallForecast, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should analyze segment with very low customer count")
        void shouldAnalyzeVerySmallForecast() {
            SalesForecast tinyForecast = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Tiny Forecast")
                    .criteria(testCriteria)
                    .customerCount(5L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(tinyForecast, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should analyze segment with empty options")
        void shouldAnalyzeWithEmptyOptions() {
            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(testForecast, Map.of());

            assertThat(result).isNotNull();
            assertThat(result.customerCount()).isEqualTo(500);
        }

        @Test
        @DisplayName("Should analyze segment with null options")
        void shouldAnalyzeWithNullOptions() {
            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(testForecast, null);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should include demographic analysis")
        void shouldIncludeDemographicAnalysis() {
            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(testForecast, Map.of());

            assertThat(result.demographics()).isNotNull();
            assertThat(result.demographics()).containsKey("field");
            assertThat(result.demographics()).containsKey("operator");
            assertThat(result.demographics()).containsKey("customerCount");
            assertThat(result.demographics().get("field")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include behavioral analysis")
        void shouldIncludeBehavioralAnalysis() {
            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(testForecast, Map.of());

            assertThat(result.behaviors()).isNotNull();
            assertThat(result.behaviors()).containsKey("criteriaField");
            assertThat(result.behaviors()).containsKey("criteriaValue");
            assertThat(result.behaviors().get("criteriaField")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include transaction analysis")
        void shouldIncludeTransactionAnalysis() {
            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(testForecast, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions()).containsKey("customerCount");
            assertThat(result.transactions()).containsKey("lastAnalyzed");
        }

        @Test
        @DisplayName("Should include predictions")
        void shouldIncludePredictions() {
            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(testForecast, Map.of());

            assertThat(result.predictions()).isNotNull();
            assertThat(result.predictions()).containsKey("churnRisk");
            assertThat(result.predictions()).containsKey("upsellPotential");
            assertThat(result.predictions()).containsKey("predictedGrowthRate");
        }

        @Test
        @DisplayName("Should include recommendations")
        void shouldIncludeRecommendations() {
            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(testForecast, Map.of());

            assertThat(result.recommendations()).isNotNull();
            assertThat(result.recommendations()).containsKey("optimalContactFrequency");
            assertThat(result.recommendations()).containsKey("recommendedOfferType");
        }

        @Test
        @DisplayName("Should recommend expansion for small segments")
        void shouldRecommendExpansionForSmallForecasts() {
            SalesForecast smallForecast = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Forecast")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(smallForecast, Map.of());

            assertThat(result.recommendations()).containsKey("expansionSuggestion");
        }

        @Test
        @DisplayName("Should recommend refinement for very large segments")
        void shouldRecommendRefinementForLargeForecasts() {
            SalesForecast largeForecast = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Forecast")
                    .criteria(testCriteria)
                    .customerCount(150000L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(largeForecast, Map.of());

            assertThat(result.recommendations()).containsKey("refinementSuggestion");
        }

        @Test
        @DisplayName("Should predict low churn risk for large segments")
        void shouldPredictLowChurnForLargeForecasts() {
            SalesForecast largeForecast = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Forecast")
                    .criteria(testCriteria)
                    .customerCount(500L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(largeForecast, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("low");
        }

        @Test
        @DisplayName("Should predict variable churn risk for small segments")
        void shouldPredictVariableChurnForSmallForecasts() {
            SalesForecast smallForecast = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Forecast")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(smallForecast, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("variable");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle segment with zero customers")
        void shouldHandleZeroForecastModels() {
            SalesForecast emptyForecast = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Empty Forecast")
                    .criteria(testCriteria)
                    .customerCount(0L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(emptyForecast, Map.of());

            assertThat(result.customerCount()).isEqualTo(0);
            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should handle segment with null last analyzed date")
        void shouldHandleNullLastAnalyzed() {
            SalesForecast segment = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Forecast")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .lastAnalyzedAt(null)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(segment, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions().get("lastAnalyzed")).isEqualTo("Never");
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 10 customers")
        void shouldCalculateConfidenceAtBoundary10() {
            SalesForecast segment = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Forecast")
                    .criteria(testCriteria)
                    .customerCount(10L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 100 customers")
        void shouldCalculateConfidenceAtBoundary100() {
            SalesForecast segment = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Forecast")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 1000 customers")
        void shouldCalculateConfidenceAtBoundary1000() {
            SalesForecast segment = SalesForecast.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Forecast")
                    .criteria(testCriteria)
                    .customerCount(1000L)
                    .build();

            ForecastAnalysisResponseDto result = analysisService.analyzeForecast(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.95);
        }
    }
}
