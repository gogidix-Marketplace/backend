package com.gogidix.aiservices.aicustomersegmentationservice.application.service;

import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.SegmentAnalysisResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.CustomerSegment;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentStatus;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentType;
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
@DisplayName("SegmentAnalysisService Tests")
class SegmentAnalysisServiceTest {

    @InjectMocks
    private SegmentAnalysisService analysisService;

    private CustomerSegment testSegment;
    private SegmentCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = SegmentCriteria.builder()
                .type(SegmentCriteria.CriteriaType.CUSTOM)
                .operator(SegmentCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testSegment = CustomerSegment.builder()
                .segmentId("segment-123")
                .tenantId("tenant-456")
                .name("Premium Customers")
                .description("High value customers")
                .criteria(testCriteria)
                .status(SegmentStatus.ACTIVE)
                .segmentType(SegmentType.BEHAVIORAL)
                .customerCount(500L)
                .lastAnalyzedAt(Instant.now().minusSeconds(3600))
                .build();
    }

    @Nested
    @DisplayName("analyzeSegment() Tests")
    class AnalyzeSegmentTests {

        @Test
        @DisplayName("Should analyze segment with high confidence")
        void shouldAnalyzeWithHighConfidence() {
            Map<String, Object> options = Map.of("includeDemographics", true);

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(testSegment, options);

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
        void shouldAnalyzeMediumSegment() {
            CustomerSegment mediumSegment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Medium Segment")
                    .criteria(testCriteria)
                    .customerCount(150L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(mediumSegment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should analyze segment with low customer count")
        void shouldAnalyzeSmallSegment() {
            CustomerSegment smallSegment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Segment")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(smallSegment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should analyze segment with very low customer count")
        void shouldAnalyzeVerySmallSegment() {
            CustomerSegment tinySegment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Tiny Segment")
                    .criteria(testCriteria)
                    .customerCount(5L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(tinySegment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should analyze segment with empty options")
        void shouldAnalyzeWithEmptyOptions() {
            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(testSegment, Map.of());

            assertThat(result).isNotNull();
            assertThat(result.customerCount()).isEqualTo(500);
        }

        @Test
        @DisplayName("Should analyze segment with null options")
        void shouldAnalyzeWithNullOptions() {
            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(testSegment, null);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should include demographic analysis")
        void shouldIncludeDemographicAnalysis() {
            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(testSegment, Map.of());

            assertThat(result.demographics()).isNotNull();
            assertThat(result.demographics()).containsKey("field");
            assertThat(result.demographics()).containsKey("operator");
            assertThat(result.demographics()).containsKey("customerCount");
            assertThat(result.demographics().get("field")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include behavioral analysis")
        void shouldIncludeBehavioralAnalysis() {
            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(testSegment, Map.of());

            assertThat(result.behaviors()).isNotNull();
            assertThat(result.behaviors()).containsKey("criteriaField");
            assertThat(result.behaviors()).containsKey("criteriaValue");
            assertThat(result.behaviors().get("criteriaField")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include transaction analysis")
        void shouldIncludeTransactionAnalysis() {
            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(testSegment, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions()).containsKey("customerCount");
            assertThat(result.transactions()).containsKey("lastAnalyzed");
        }

        @Test
        @DisplayName("Should include predictions")
        void shouldIncludePredictions() {
            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(testSegment, Map.of());

            assertThat(result.predictions()).isNotNull();
            assertThat(result.predictions()).containsKey("churnRisk");
            assertThat(result.predictions()).containsKey("upsellPotential");
            assertThat(result.predictions()).containsKey("predictedGrowthRate");
        }

        @Test
        @DisplayName("Should include recommendations")
        void shouldIncludeRecommendations() {
            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(testSegment, Map.of());

            assertThat(result.recommendations()).isNotNull();
            assertThat(result.recommendations()).containsKey("optimalContactFrequency");
            assertThat(result.recommendations()).containsKey("recommendedOfferType");
        }

        @Test
        @DisplayName("Should recommend expansion for small segments")
        void shouldRecommendExpansionForSmallSegments() {
            CustomerSegment smallSegment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Segment")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(smallSegment, Map.of());

            assertThat(result.recommendations()).containsKey("expansionSuggestion");
        }

        @Test
        @DisplayName("Should recommend refinement for very large segments")
        void shouldRecommendRefinementForLargeSegments() {
            CustomerSegment largeSegment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Segment")
                    .criteria(testCriteria)
                    .customerCount(150000L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(largeSegment, Map.of());

            assertThat(result.recommendations()).containsKey("refinementSuggestion");
        }

        @Test
        @DisplayName("Should predict low churn risk for large segments")
        void shouldPredictLowChurnForLargeSegments() {
            CustomerSegment largeSegment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Segment")
                    .criteria(testCriteria)
                    .customerCount(500L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(largeSegment, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("low");
        }

        @Test
        @DisplayName("Should predict variable churn risk for small segments")
        void shouldPredictVariableChurnForSmallSegments() {
            CustomerSegment smallSegment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Segment")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(smallSegment, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("variable");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle segment with zero customers")
        void shouldHandleZeroCustomers() {
            CustomerSegment emptySegment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Empty Segment")
                    .criteria(testCriteria)
                    .customerCount(0L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(emptySegment, Map.of());

            assertThat(result.customerCount()).isEqualTo(0);
            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should handle segment with null last analyzed date")
        void shouldHandleNullLastAnalyzed() {
            CustomerSegment segment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Segment")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .lastAnalyzedAt(null)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(segment, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions().get("lastAnalyzed")).isEqualTo("Never");
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 10 customers")
        void shouldCalculateConfidenceAtBoundary10() {
            CustomerSegment segment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Segment")
                    .criteria(testCriteria)
                    .customerCount(10L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 100 customers")
        void shouldCalculateConfidenceAtBoundary100() {
            CustomerSegment segment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Segment")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 1000 customers")
        void shouldCalculateConfidenceAtBoundary1000() {
            CustomerSegment segment = CustomerSegment.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Segment")
                    .criteria(testCriteria)
                    .customerCount(1000L)
                    .build();

            SegmentAnalysisResponseDto result = analysisService.analyzeSegment(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.95);
        }
    }
}
