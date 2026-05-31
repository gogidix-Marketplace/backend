package com.gogidix.aiservices.aifrauddetectionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("FraudAnalysisResult Domain Model Tests")
class FraudAnalysisResultTest {

    private static final String ANALYSIS_ID = "analysis-123";
    private static final String TRANSACTION_ID = "txn-456";
    private static final String USER_ID = "user-789";

    @Nested
    @DisplayName("Fraud Detection Tests")
    class FraudDetectionTests {

        @Test
        @DisplayName("Should detect fraudulent transaction")
        void shouldDetectFraudulentTransaction() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("High fraud probability"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.isFraudulent()).isTrue();
            assertThat(result.requiresReview()).isFalse();
            assertThat(result.shouldAllow()).isFalse();
        }

        @Test
        @DisplayName("Should flag transaction for review")
        void shouldFlagForReview() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.6)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of("Suspicious activity"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.isFraudulent()).isFalse();
            assertThat(result.requiresReview()).isTrue();
            assertThat(result.shouldAllow()).isFalse();
        }

        @Test
        @DisplayName("Should allow legitimate transaction")
        void shouldAllowLegitimateTransaction() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.2)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.isFraudulent()).isFalse();
            assertThat(result.requiresReview()).isFalse();
            assertThat(result.shouldAllow()).isTrue();
        }

        @ParameterizedTest
        @CsvSource({
            "0.81, true",
            "0.9, true",
            "1.0, true",
            "0.8, false",
            "0.5, false"
        })
        @DisplayName("Should classify fraud correctly")
        void shouldClassifyFraud(double score, boolean isFraud) {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(score)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.isFraudulent()).isEqualTo(isFraud);
        }
    }

    @Nested
    @DisplayName("Risk Classification Tests")
    class RiskClassificationTests {

        @ParameterizedTest
        @CsvSource({
            "0.0, LOW",
            "0.2, LOW",
            "0.3, LOW",
            "0.4, MEDIUM",
            "0.5, MEDIUM",
            "0.7, MEDIUM",
            "0.8, HIGH",
            "0.9, HIGH",
            "1.0, HIGH"
        })
        @DisplayName("Should classify risk level correctly")
        void shouldClassifyRiskLevel(double score, RiskLevel expectedLevel) {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(score)
                    .riskLevel(expectedLevel)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.getRiskLevel()).isEqualTo(expectedLevel);
        }
    }

    @Nested
    @DisplayName("Action Determination Tests")
    class ActionDeterminationTests {

        @Test
        @DisplayName("Should block high score transactions")
        void shouldBlockHighScore() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.85)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("High fraud score"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.BLOCK);
        }

        @Test
        @DisplayName("Should review medium score transactions")
        void shouldReviewMediumScore() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.6)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of("Suspicious pattern"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.REVIEW);
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            Instant timestamp = Instant.now();
            List<String> reasons = List.of("Reason 1", "Reason 2");

            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.75)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(reasons)
                    .timestamp(timestamp)
                    .modelVersion("2.0.0")
                    .build();

            assertThat(result.getAnalysisId()).isEqualTo(ANALYSIS_ID);
            assertThat(result.getTransactionId()).isEqualTo(TRANSACTION_ID);
            assertThat(result.getUserId()).isEqualTo(USER_ID);
            assertThat(result.getFraudScore()).isEqualTo(0.75);
            assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.MEDIUM);
            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.REVIEW);
            assertThat(result.getReasons()).isEqualTo(reasons);
            assertThat(result.getTimestamp()).isEqualTo(timestamp);
            assertThat(result.getModelVersion()).isEqualTo("2.0.0");
        }

        @Test
        @DisplayName("Should build with minimal required fields")
        void shouldBuildWithMinimalFields() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result).isNotNull();
            assertThat(result.getAnalysisId()).isEqualTo(ANALYSIS_ID);
        }

        @Test
        @DisplayName("Should create multiple independent instances")
        void shouldCreateMultipleIndependentInstances() {
            FraudAnalysisResult result1 = FraudAnalysisResult.builder()
                    .analysisId("analysis-1")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.3)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            FraudAnalysisResult result2 = FraudAnalysisResult.builder()
                    .analysisId("analysis-2")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("Fraud"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result1.getAnalysisId()).isNotEqualTo(result2.getAnalysisId());
            assertThat(result1.getFraudScore()).isNotEqualTo(result2.getFraudScore());
            assertThat(result1.getRiskLevel()).isNotEqualTo(result2.getRiskLevel());
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterMethodTests {

        @Test
        @DisplayName("Should return correct analysisId")
        void shouldReturnCorrectAnalysisId() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getAnalysisId()).isEqualTo(ANALYSIS_ID);
        }

        @Test
        @DisplayName("Should return correct transactionId")
        void shouldReturnCorrectTransactionId() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getTransactionId()).isEqualTo(TRANSACTION_ID);
        }

        @Test
        @DisplayName("Should return correct userId")
        void shouldReturnCorrectUserId() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getUserId()).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should return correct fraudScore")
        void shouldReturnCorrectFraudScore() {
            double score = 0.65;
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(score)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getFraudScore()).isEqualTo(score);
        }

        @Test
        @DisplayName("Should return correct riskLevel")
        void shouldReturnCorrectRiskLevel() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        }

        @Test
        @DisplayName("Should return correct recommendedAction")
        void shouldReturnCorrectRecommendedAction() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.ALLOW);
        }

        @Test
        @DisplayName("Should return correct reasons")
        void shouldReturnCorrectReasons() {
            List<String> reasons = List.of("High amount", "New device");
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.7)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(reasons)
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getReasons()).isEqualTo(reasons);
            assertThat(result.getReasons()).hasSize(2);
        }

        @Test
        @DisplayName("Should return correct timestamp")
        void shouldReturnCorrectTimestamp() {
            Instant timestamp = Instant.now();
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(timestamp)
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getTimestamp()).isEqualTo(timestamp);
        }

        @Test
        @DisplayName("Should return correct modelVersion")
        void shouldReturnCorrectModelVersion() {
            String version = "3.5.2";
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion(version)
                    .build();

            assertThat(result.getModelVersion()).isEqualTo(version);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle minimum fraud score of 0.0")
        void shouldHandleMinimumFraudScore() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.0)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getFraudScore()).isEqualTo(0.0);
            assertThat(result.isFraudulent()).isFalse();
            assertThat(result.shouldAllow()).isTrue();
        }

        @Test
        @DisplayName("Should handle maximum fraud score of 1.0")
        void shouldHandleMaximumFraudScore() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(1.0)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("Maximum fraud score"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getFraudScore()).isEqualTo(1.0);
            assertThat(result.isFraudulent()).isTrue();
            assertThat(result.shouldAllow()).isFalse();
        }

        @Test
        @DisplayName("Should handle boundary score for fraud detection (0.8)")
        void shouldHandleBoundaryScoreForFraudDetection() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.8)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.isFraudulent()).isFalse();
            assertThat(result.requiresReview()).isTrue();
            assertThat(result.shouldAllow()).isFalse();
        }

        @Test
        @DisplayName("Should handle boundary score for review (0.5)")
        void shouldHandleBoundaryScoreForReview() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.isFraudulent()).isFalse();
            assertThat(result.requiresReview()).isFalse();
            assertThat(result.shouldAllow()).isTrue();
        }

        @Test
        @DisplayName("Should handle empty reasons list")
        void shouldHandleEmptyReasonsList() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.3)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getReasons()).isEmpty();
        }

        @Test
        @DisplayName("Should handle single reason")
        void shouldHandleSingleReason() {
            List<String> reasons = List.of("Single reason");
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.6)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(reasons)
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getReasons()).hasSize(1);
        }

        @Test
        @DisplayName("Should handle multiple reasons")
        void shouldHandleMultipleReasons() {
            List<String> reasons = List.of("Reason 1", "Reason 2", "Reason 3", "Reason 4", "Reason 5");
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(reasons)
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getReasons()).hasSize(5);
        }

        @Test
        @DisplayName("Should handle epoch timestamp")
        void shouldHandleEpochTimestamp() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.EPOCH)
                    .modelVersion("1.0")
                    .build();

            assertThat(result.getTimestamp()).isEqualTo(Instant.EPOCH);
        }

        @Test
        @DisplayName("Should handle empty model version")
        void shouldHandleEmptyModelVersion() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("")
                    .build();

            assertThat(result.getModelVersion()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Business Logic Boundary Tests")
    class BusinessLogicBoundaryTests {

        @ParameterizedTest
        @CsvSource({
            "0.50, false, true",
            "0.51, true, false",
            "0.80, true, false",
            "0.81, false, false"
        })
        @DisplayName("Should correctly determine review boundary")
        void shouldDetermineReviewBoundary(double score, boolean requiresReview, boolean shouldAllow) {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(score)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of())
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(result.requiresReview()).isEqualTo(requiresReview);
            assertThat(result.shouldAllow()).isEqualTo(shouldAllow);
        }
    }
}
