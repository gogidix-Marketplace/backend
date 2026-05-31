package com.gogidix.aiservices.aimarketbasketanalysisservice.application.service;

import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.BasketAnalysisResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.MarketBasket;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketStatus;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
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
@DisplayName("BasketAnalysisService Tests")
class BasketAnalysisServiceTest {

    @InjectMocks
    private BasketAnalysisService analysisService;

    private MarketBasket testBasket;
    private BasketCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = BasketCriteria.builder()
                .type(BasketCriteria.CriteriaType.CUSTOM)
                .operator(BasketCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testBasket = MarketBasket.builder()
                .segmentId("segment-123")
                .tenantId("tenant-456")
                .name("Premium Customers")
                .description("High value customers")
                .criteria(testCriteria)
                .status(BasketStatus.ACTIVE)
                .segmentType(BasketType.BEHAVIORAL)
                .customerCount(500L)
                .lastAnalyzedAt(Instant.now().minusSeconds(3600))
                .build();
    }

    @Nested
    @DisplayName("analyzeBasket() Tests")
    class AnalyzeBasketTests {

        @Test
        @DisplayName("Should analyze segment with high confidence")
        void shouldAnalyzeWithHighConfidence() {
            Map<String, Object> options = Map.of("includeDemographics", true);

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(testBasket, options);

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
        void shouldAnalyzeMediumBasket() {
            MarketBasket mediumBasket = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Medium Basket")
                    .criteria(testCriteria)
                    .customerCount(150L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(mediumBasket, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should analyze segment with low customer count")
        void shouldAnalyzeSmallBasket() {
            MarketBasket smallBasket = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Basket")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(smallBasket, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should analyze segment with very low customer count")
        void shouldAnalyzeVerySmallBasket() {
            MarketBasket tinyBasket = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Tiny Basket")
                    .criteria(testCriteria)
                    .customerCount(5L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(tinyBasket, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should analyze segment with empty options")
        void shouldAnalyzeWithEmptyOptions() {
            BasketAnalysisResponseDto result = analysisService.analyzeBasket(testBasket, Map.of());

            assertThat(result).isNotNull();
            assertThat(result.customerCount()).isEqualTo(500);
        }

        @Test
        @DisplayName("Should analyze segment with null options")
        void shouldAnalyzeWithNullOptions() {
            BasketAnalysisResponseDto result = analysisService.analyzeBasket(testBasket, null);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should include demographic analysis")
        void shouldIncludeDemographicAnalysis() {
            BasketAnalysisResponseDto result = analysisService.analyzeBasket(testBasket, Map.of());

            assertThat(result.demographics()).isNotNull();
            assertThat(result.demographics()).containsKey("field");
            assertThat(result.demographics()).containsKey("operator");
            assertThat(result.demographics()).containsKey("customerCount");
            assertThat(result.demographics().get("field")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include behavioral analysis")
        void shouldIncludeBehavioralAnalysis() {
            BasketAnalysisResponseDto result = analysisService.analyzeBasket(testBasket, Map.of());

            assertThat(result.behaviors()).isNotNull();
            assertThat(result.behaviors()).containsKey("criteriaField");
            assertThat(result.behaviors()).containsKey("criteriaValue");
            assertThat(result.behaviors().get("criteriaField")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include transaction analysis")
        void shouldIncludeTransactionAnalysis() {
            BasketAnalysisResponseDto result = analysisService.analyzeBasket(testBasket, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions()).containsKey("customerCount");
            assertThat(result.transactions()).containsKey("lastAnalyzed");
        }

        @Test
        @DisplayName("Should include predictions")
        void shouldIncludePredictions() {
            BasketAnalysisResponseDto result = analysisService.analyzeBasket(testBasket, Map.of());

            assertThat(result.predictions()).isNotNull();
            assertThat(result.predictions()).containsKey("churnRisk");
            assertThat(result.predictions()).containsKey("upsellPotential");
            assertThat(result.predictions()).containsKey("predictedGrowthRate");
        }

        @Test
        @DisplayName("Should include recommendations")
        void shouldIncludeRecommendations() {
            BasketAnalysisResponseDto result = analysisService.analyzeBasket(testBasket, Map.of());

            assertThat(result.recommendations()).isNotNull();
            assertThat(result.recommendations()).containsKey("optimalContactFrequency");
            assertThat(result.recommendations()).containsKey("recommendedOfferType");
        }

        @Test
        @DisplayName("Should recommend expansion for small segments")
        void shouldRecommendExpansionForSmallBaskets() {
            MarketBasket smallBasket = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Basket")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(smallBasket, Map.of());

            assertThat(result.recommendations()).containsKey("expansionSuggestion");
        }

        @Test
        @DisplayName("Should recommend refinement for very large segments")
        void shouldRecommendRefinementForLargeBaskets() {
            MarketBasket largeBasket = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Basket")
                    .criteria(testCriteria)
                    .customerCount(150000L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(largeBasket, Map.of());

            assertThat(result.recommendations()).containsKey("refinementSuggestion");
        }

        @Test
        @DisplayName("Should predict low churn risk for large segments")
        void shouldPredictLowChurnForLargeBaskets() {
            MarketBasket largeBasket = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Basket")
                    .criteria(testCriteria)
                    .customerCount(500L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(largeBasket, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("low");
        }

        @Test
        @DisplayName("Should predict variable churn risk for small segments")
        void shouldPredictVariableChurnForSmallBaskets() {
            MarketBasket smallBasket = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Basket")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(smallBasket, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("variable");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle segment with zero customers")
        void shouldHandleZeroCustomers() {
            MarketBasket emptyBasket = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Empty Basket")
                    .criteria(testCriteria)
                    .customerCount(0L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(emptyBasket, Map.of());

            assertThat(result.customerCount()).isEqualTo(0);
            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should handle segment with null last analyzed date")
        void shouldHandleNullLastAnalyzed() {
            MarketBasket segment = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Basket")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .lastAnalyzedAt(null)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(segment, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions().get("lastAnalyzed")).isEqualTo("Never");
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 10 customers")
        void shouldCalculateConfidenceAtBoundary10() {
            MarketBasket segment = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Basket")
                    .criteria(testCriteria)
                    .customerCount(10L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 100 customers")
        void shouldCalculateConfidenceAtBoundary100() {
            MarketBasket segment = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Basket")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 1000 customers")
        void shouldCalculateConfidenceAtBoundary1000() {
            MarketBasket segment = MarketBasket.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Basket")
                    .criteria(testCriteria)
                    .customerCount(1000L)
                    .build();

            BasketAnalysisResponseDto result = analysisService.analyzeBasket(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.95);
        }
    }
}
