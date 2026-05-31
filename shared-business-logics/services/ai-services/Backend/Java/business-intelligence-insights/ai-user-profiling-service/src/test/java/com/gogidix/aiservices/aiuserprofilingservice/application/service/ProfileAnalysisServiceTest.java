package com.gogidix.aiservices.aiuserprofilingservice.application.service;

import com.gogidix.aiservices.aiuserprofilingservice.application.dto.ProfileAnalysisResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.UserProfile;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileStatus;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;
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
@DisplayName("ProfileAnalysisService Tests")
class ProfileAnalysisServiceTest {

    @InjectMocks
    private ProfileAnalysisService analysisService;

    private UserProfile testProfile;
    private ProfileCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = ProfileCriteria.builder()
                .type(ProfileCriteria.CriteriaType.CUSTOM)
                .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testProfile = UserProfile.builder()
                .segmentId("segment-123")
                .tenantId("tenant-456")
                .name("Premium Users")
                .description("High value customers")
                .criteria(testCriteria)
                .status(ProfileStatus.ACTIVE)
                .segmentType(ProfileType.BEHAVIORAL)
                .customerCount(500L)
                .lastAnalyzedAt(Instant.now().minusSeconds(3600))
                .build();
    }

    @Nested
    @DisplayName("analyzeProfile() Tests")
    class AnalyzeProfileTests {

        @Test
        @DisplayName("Should analyze segment with high confidence")
        void shouldAnalyzeWithHighConfidence() {
            Map<String, Object> options = Map.of("includeDemographics", true);

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(testProfile, options);

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
        void shouldAnalyzeMediumProfile() {
            UserProfile mediumProfile = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Medium Profile")
                    .criteria(testCriteria)
                    .customerCount(150L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(mediumProfile, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should analyze segment with low customer count")
        void shouldAnalyzeSmallProfile() {
            UserProfile smallProfile = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Profile")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(smallProfile, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should analyze segment with very low customer count")
        void shouldAnalyzeVerySmallProfile() {
            UserProfile tinyProfile = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Tiny Profile")
                    .criteria(testCriteria)
                    .customerCount(5L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(tinyProfile, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should analyze segment with empty options")
        void shouldAnalyzeWithEmptyOptions() {
            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(testProfile, Map.of());

            assertThat(result).isNotNull();
            assertThat(result.customerCount()).isEqualTo(500);
        }

        @Test
        @DisplayName("Should analyze segment with null options")
        void shouldAnalyzeWithNullOptions() {
            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(testProfile, null);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should include demographic analysis")
        void shouldIncludeDemographicAnalysis() {
            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(testProfile, Map.of());

            assertThat(result.demographics()).isNotNull();
            assertThat(result.demographics()).containsKey("field");
            assertThat(result.demographics()).containsKey("operator");
            assertThat(result.demographics()).containsKey("customerCount");
            assertThat(result.demographics().get("field")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include behavioral analysis")
        void shouldIncludeBehavioralAnalysis() {
            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(testProfile, Map.of());

            assertThat(result.behaviors()).isNotNull();
            assertThat(result.behaviors()).containsKey("criteriaField");
            assertThat(result.behaviors()).containsKey("criteriaValue");
            assertThat(result.behaviors().get("criteriaField")).isEqualTo("lifetimeValue");
        }

        @Test
        @DisplayName("Should include transaction analysis")
        void shouldIncludeTransactionAnalysis() {
            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(testProfile, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions()).containsKey("customerCount");
            assertThat(result.transactions()).containsKey("lastAnalyzed");
        }

        @Test
        @DisplayName("Should include predictions")
        void shouldIncludePredictions() {
            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(testProfile, Map.of());

            assertThat(result.predictions()).isNotNull();
            assertThat(result.predictions()).containsKey("churnRisk");
            assertThat(result.predictions()).containsKey("upsellPotential");
            assertThat(result.predictions()).containsKey("predictedGrowthRate");
        }

        @Test
        @DisplayName("Should include recommendations")
        void shouldIncludeRecommendations() {
            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(testProfile, Map.of());

            assertThat(result.recommendations()).isNotNull();
            assertThat(result.recommendations()).containsKey("optimalContactFrequency");
            assertThat(result.recommendations()).containsKey("recommendedOfferType");
        }

        @Test
        @DisplayName("Should recommend expansion for small segments")
        void shouldRecommendExpansionForSmallProfiles() {
            UserProfile smallProfile = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Profile")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(smallProfile, Map.of());

            assertThat(result.recommendations()).containsKey("expansionSuggestion");
        }

        @Test
        @DisplayName("Should recommend refinement for very large segments")
        void shouldRecommendRefinementForLargeProfiles() {
            UserProfile largeProfile = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Profile")
                    .criteria(testCriteria)
                    .customerCount(150000L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(largeProfile, Map.of());

            assertThat(result.recommendations()).containsKey("refinementSuggestion");
        }

        @Test
        @DisplayName("Should predict low churn risk for large segments")
        void shouldPredictLowChurnForLargeProfiles() {
            UserProfile largeProfile = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Large Profile")
                    .criteria(testCriteria)
                    .customerCount(500L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(largeProfile, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("low");
        }

        @Test
        @DisplayName("Should predict variable churn risk for small segments")
        void shouldPredictVariableChurnForSmallProfiles() {
            UserProfile smallProfile = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Small Profile")
                    .criteria(testCriteria)
                    .customerCount(50L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(smallProfile, Map.of());

            assertThat(result.predictions().get("churnRisk")).isEqualTo("variable");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle segment with zero customers")
        void shouldHandleZeroUsers() {
            UserProfile emptyProfile = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Empty Profile")
                    .criteria(testCriteria)
                    .customerCount(0L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(emptyProfile, Map.of());

            assertThat(result.customerCount()).isEqualTo(0);
            assertThat(result.confidenceScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should handle segment with null last analyzed date")
        void shouldHandleNullLastAnalyzed() {
            UserProfile segment = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Profile")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .lastAnalyzedAt(null)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(segment, Map.of());

            assertThat(result.transactions()).isNotNull();
            assertThat(result.transactions().get("lastAnalyzed")).isEqualTo("Never");
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 10 customers")
        void shouldCalculateConfidenceAtBoundary10() {
            UserProfile segment = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Profile")
                    .criteria(testCriteria)
                    .customerCount(10L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 100 customers")
        void shouldCalculateConfidenceAtBoundary100() {
            UserProfile segment = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Profile")
                    .criteria(testCriteria)
                    .customerCount(100L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }

        @Test
        @DisplayName("Should calculate confidence score boundary at 1000 customers")
        void shouldCalculateConfidenceAtBoundary1000() {
            UserProfile segment = UserProfile.builder()
                    .segmentId("segment-123")
                    .tenantId("tenant-456")
                    .name("Test Profile")
                    .criteria(testCriteria)
                    .customerCount(1000L)
                    .build();

            ProfileAnalysisResponseDto result = analysisService.analyzeProfile(segment, Map.of());

            assertThat(result.confidenceScore()).isEqualTo(0.8);
        }
    }
}
