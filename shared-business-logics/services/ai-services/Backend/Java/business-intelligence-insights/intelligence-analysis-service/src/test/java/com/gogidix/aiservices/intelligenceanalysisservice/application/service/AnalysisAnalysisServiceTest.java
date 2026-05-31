package com.gogidix.aiservices.intelligenceanalysisservice.application.service;

import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.AnalysisAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.IntelligenceAnalysis;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisStatus;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;
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
@DisplayName("AnalysisAnalysisService Tests")
class AnalysisAnalysisServiceTest {

    @InjectMocks
    private AnalysisAnalysisService analysisService;

    private IntelligenceAnalysis testAnalysis;
    private AnalysisCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = AnalysisCriteria.builder()
                .type(AnalysisCriteria.CriteriaType.CUSTOM)
                .operator(AnalysisCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testAnalysis = IntelligenceAnalysis.builder()
                .segmentId("segment-123")
                .tenantId("tenant-456")
                .name("Premium IntelligenceReports")
                .description("High value customers")
                .criteria(testCriteria)
                .status(AnalysisStatus.ACTIVE)
                .segmentType(AnalysisType.BEHAVIORAL)
                .customerCount(500L)
                .lastAnalyzedAt(Instant.now().minusSeconds(3600))
                .build();
    }

    @Nested
    @DisplayName("analyzeAnalysis() Tests")
    class AnalyzeAnalysisTests {

        @Test
        @DisplayName("Should analyze segment with high confidence")
        void shouldAnalyzeWithHighConfidence() {
            Map<String, Object> options = Map.of("includeDemographics", true);

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(testAnalysis, options);

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
        void shouldAnalyzeMediumAnalysis() {
            IntelligenceAnalysis mediumAnalysis = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Medium Analysis")
                    .criteria(testCriteria)
                    .customerCount(150L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(mediumAnalysis, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should analyze segment with low customer count")
        void shouldAnalyzeSmallAnalysis() {
            IntelligenceAnalysis smallAnalysis = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Analysis")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(smallAnalysis, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should analyze segment with very low customer count")
        void shouldAnalyzeVerySmallAnalysis() {
            IntelligenceAnalysis tinyAnalysis = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Tiny Analysis")
                    .criteria(testCriteria)
                    .customerCount(5L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(tinyAnalysis, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should analyze segment with empty options")
        void shouldAnalyzeWithEmptyOptions() {
            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(testAnalysis, Map.of());

            assertThat(result).isNotNull();
            assertThat(result.customerCount()).isEqualTo(500);
        }

        @Test
        @DisplayName("Should analyze segment with null options")
        void shouldAnalyzeWithNullOptions() {
            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(testAnalysis, null);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should include demographic analysis")
        void shouldIncludeDemographicAnalysis() {
            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(testAnalysis, Map.of());

            assertThat(result.demographics()).isNotNull();
            assertThat(result.demographics()).containsKey("field");
            assertThat(result.demographics()).containsKey("operator");
            assertThat(result.demographics()).containsKey("customerCount");
            assertThat(result.demographics().get("field")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include behavioral analysis")
        void shouldIncludeBehavioralAnalysis() {
            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(testAnalysis, Map.of());

            assertThat(result.behaviors()).isNotNull();
            assertThat(result.behaviors()).containsKey("criteriaField");
            assertThat(result.behaviors()).containsKey("criteriaValue");
            assertThat(result.behaviors().get("criteriaField")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include transaction analysis")
        void shouldIncludeTransactionAnalysis() {
            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(testAnalysis, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions()).containsKey("customerCount");
            assertThat(result.transactions()).containsKey("lastAnalyzed");
        }

        @Test
        @DisplayName("Should include predictions")
        void shouldIncludePredictions() {
            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(testAnalysis, Map.of());

            assertThat(result.predictions()).isNotNull();
            assertThat(result.predictions()).containsKey("churnRisk");
            assertThat(result.predictions()).containsKey("upsellPotential");
            assertThat(result.predictions()).containsKey("predictedGrowthRate");
        }

        @Test
        @DisplayName("Should include recommendations")
        void shouldIncludeRecommendations() {
            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(testAnalysis, Map.of());

            assertThat(result.recommendations()).isNotNull();
            assertThat(result.recommendations()).containsKey("optimalContactFrequency");
            assertThat(result.recommendations()).containsKey("recommendedOfferType");
        }

        @Test
        @DisplayName("Should recommend expansion for small segments")
        void shouldRecommendExpansionForSmallAnalysiss() {
            IntelligenceAnalysis smallAnalysis = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Analysis")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(smallAnalysis, Map.of());

            assertThat(result.recommendations()).containsKey("expansionSuggestion");
        }

        @Test
        @DisplayName("Should recommend refinement for very large segments")
        void shouldRecommendRefinementForLargeAnalysiss() {
            IntelligenceAnalysis largeAnalysis = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Analysis")
                    .criteria(testCriteria)
                    .customerCount(150000L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(largeAnalysis, Map.of());

            assertThat(result.recommendations()).containsKey("refinementSuggestion");
        }

        @Test
        @DisplayName("Should predict low churn risk for large segments")
        void shouldPredictLowChurnForLargeAnalysiss() {
            IntelligenceAnalysis largeAnalysis = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Analysis")
                    .criteria(testCriteria)
                    .customerCount(500L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(largeAnalysis, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("low");
        }

        @Test
        @DisplayName("Should predict variable churn risk for small segments")
        void shouldPredictVariableChurnForSmallAnalysiss() {
            IntelligenceAnalysis smallAnalysis = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Analysis")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(smallAnalysis, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("variable");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle segment with zero customers")
        void shouldHandleZeroIntelligenceReports() {
            IntelligenceAnalysis emptyAnalysis = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Empty Analysis")
                    .criteria(testCriteria)
                    .customerCount(0L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(emptyAnalysis, Map.of());

            assertThat(result.customerCount()).isEqualTo(0);
            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should handle segment with null last analyzed date")
        void shouldHandleNullLastAnalyzed() {
            IntelligenceAnalysis segment = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Analysis")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .lastAnalyzedAt(null)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(segment, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions().get("lastAnalyzed")).isEqualTo("Never");
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 10 customers")
        void shouldCalculateConfidenceAtBoundary10() {
            IntelligenceAnalysis segment = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Analysis")
                    .criteria(testCriteria)
                    .customerCount(10L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 100 customers")
        void shouldCalculateConfidenceAtBoundary100() {
            IntelligenceAnalysis segment = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Analysis")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 1000 customers")
        void shouldCalculateConfidenceAtBoundary1000() {
            IntelligenceAnalysis segment = IntelligenceAnalysis.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Analysis")
                    .criteria(testCriteria)
                    .customerCount(1000L)
                    .build();

            AnalysisAnalysisResponseDto result = analysisService.analyzeAnalysis(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }
    }
}
