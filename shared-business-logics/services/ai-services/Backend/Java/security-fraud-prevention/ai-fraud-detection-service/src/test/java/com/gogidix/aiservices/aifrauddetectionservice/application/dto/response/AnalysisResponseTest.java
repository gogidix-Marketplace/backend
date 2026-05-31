package com.gogidix.aiservices.aifrauddetectionservice.application.dto.response;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AnalysisResponse DTO Tests")
class AnalysisResponseTest {

    private static final String ANALYSIS_ID = "analysis-123";
    private static final String TRANSACTION_ID = "txn-456";
    private static final String USER_ID = "user-789";
    private static final double FRAUD_SCORE = 0.75;
    private static final RiskLevel RISK_LEVEL = RiskLevel.MEDIUM;
    private static final FraudAction ACTION = FraudAction.REVIEW;
    private static final Instant TIMESTAMP = Instant.now();
    private static final String MODEL_VERSION = "1.0.0";
    private static final List<String> REASONS = List.of("Reason 1", "Reason 2");

    @Nested
    @DisplayName("fromDomain Tests")
    class FromDomainTests {

        @Test
        @DisplayName("Should map all fields from domain model")
        void shouldMapAllFieldsFromDomainModel() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(FRAUD_SCORE)
                    .riskLevel(RISK_LEVEL)
                    .recommendedAction(ACTION)
                    .reasons(REASONS)
                    .timestamp(TIMESTAMP)
                    .modelVersion(MODEL_VERSION)
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getAnalysisId()).isEqualTo(ANALYSIS_ID);
            assertThat(response.getTransactionId()).isEqualTo(TRANSACTION_ID);
            assertThat(response.getUserId()).isEqualTo(USER_ID);
            assertThat(response.getFraudScore()).isEqualTo(FRAUD_SCORE);
            assertThat(response.getRiskLevel()).isEqualTo(RISK_LEVEL);
            assertThat(response.getAction()).isEqualTo(ACTION);
            assertThat(response.getReasons()).isEqualTo(REASONS);
            assertThat(response.getTimestamp()).isEqualTo(TIMESTAMP);
            assertThat(response.getModelVersion()).isEqualTo(MODEL_VERSION);
        }

        @Test
        @DisplayName("Should map domain with low risk and allow action")
        void shouldMapDomainWithLowRiskAndAllowAction() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.2)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(TIMESTAMP)
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getRiskLevel()).isEqualTo(RiskLevel.LOW);
            assertThat(response.getAction()).isEqualTo(FraudAction.ALLOW);
            assertThat(response.getFraudScore()).isEqualTo(0.2);
        }

        @Test
        @DisplayName("Should map domain with high risk and block action")
        void shouldMapDomainWithHighRiskAndBlockAction() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("High fraud probability"))
                    .timestamp(TIMESTAMP)
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
            assertThat(response.getAction()).isEqualTo(FraudAction.BLOCK);
            assertThat(response.getFraudScore()).isEqualTo(0.9);
        }

        @Test
        @DisplayName("Should map domain with single reason")
        void shouldMapDomainWithSingleReason() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.6)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of("Suspicious activity"))
                    .timestamp(TIMESTAMP)
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getReasons()).hasSize(1);
            assertThat(response.getReasons().get(0)).isEqualTo("Suspicious activity");
        }

        @Test
        @DisplayName("Should map domain with multiple reasons")
        void shouldMapDomainWithMultipleReasons() {
            List<String> multipleReasons = List.of("Reason 1", "Reason 2", "Reason 3", "Reason 4");

            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.85)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(multipleReasons)
                    .timestamp(TIMESTAMP)
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getReasons()).hasSize(4);
            assertThat(response.getReasons()).isEqualTo(multipleReasons);
        }

        @Test
        @DisplayName("Should map domain with empty reasons")
        void shouldMapDomainWithEmptyReasons() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.1)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(TIMESTAMP)
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getReasons()).isEmpty();
        }

        @Test
        @DisplayName("Should create new instance on each call")
        void shouldCreateNewInstanceOnEachCall() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.MEDIUM)
                    .recommendedAction(FraudAction.REVIEW)
                    .reasons(List.of("Review needed"))
                    .timestamp(TIMESTAMP)
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response1 = AnalysisResponse.fromDomain(domainResult);
            AnalysisResponse response2 = AnalysisResponse.fromDomain(domainResult);

            assertThat(response1).isNotSameAs(response2);
            assertThat(response1).usingRecursiveComparison().isEqualTo(response2);
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build response with all fields")
        void shouldBuildResponseWithAllFields() {
            AnalysisResponse response = AnalysisResponse.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(FRAUD_SCORE)
                    .riskLevel(RISK_LEVEL)
                    .action(ACTION)
                    .reasons(REASONS)
                    .timestamp(TIMESTAMP)
                    .modelVersion(MODEL_VERSION)
                    .build();

            assertThat(response.getAnalysisId()).isEqualTo(ANALYSIS_ID);
            assertThat(response.getTransactionId()).isEqualTo(TRANSACTION_ID);
            assertThat(response.getUserId()).isEqualTo(USER_ID);
            assertThat(response.getFraudScore()).isEqualTo(FRAUD_SCORE);
            assertThat(response.getRiskLevel()).isEqualTo(RISK_LEVEL);
            assertThat(response.getAction()).isEqualTo(ACTION);
            assertThat(response.getReasons()).isEqualTo(REASONS);
            assertThat(response.getTimestamp()).isEqualTo(TIMESTAMP);
            assertThat(response.getModelVersion()).isEqualTo(MODEL_VERSION);
        }

        @Test
        @DisplayName("Should build response with required fields only")
        void shouldBuildResponseWithRequiredFieldsOnly() {
            AnalysisResponse response = AnalysisResponse.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(FRAUD_SCORE)
                    .riskLevel(RISK_LEVEL)
                    .action(ACTION)
                    .reasons(REASONS)
                    .timestamp(TIMESTAMP)
                    .modelVersion(MODEL_VERSION)
                    .build();

            assertThat(response).isNotNull();
            assertThat(response.getAnalysisId()).isEqualTo(ANALYSIS_ID);
        }

        @Test
        @DisplayName("Should create multiple independent instances")
        void shouldCreateMultipleIndependentInstances() {
            AnalysisResponse response1 = AnalysisResponse.builder()
                    .analysisId("analysis-1")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.3)
                    .riskLevel(RiskLevel.LOW)
                    .action(FraudAction.ALLOW)
                    .reasons(List.of("Normal"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            AnalysisResponse response2 = AnalysisResponse.builder()
                    .analysisId("analysis-2")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.9)
                    .riskLevel(RiskLevel.HIGH)
                    .action(FraudAction.BLOCK)
                    .reasons(List.of("Fraud"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0")
                    .build();

            assertThat(response1.getAnalysisId()).isNotEqualTo(response2.getAnalysisId());
            assertThat(response1.getFraudScore()).isNotEqualTo(response2.getFraudScore());
            assertThat(response1.getRiskLevel()).isNotEqualTo(response2.getRiskLevel());
            assertThat(response1.getAction()).isNotEqualTo(response2.getAction());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get analysisId")
        void shouldSetAndGetAnalysisId() {
            AnalysisResponse response = new AnalysisResponse();
            response.setAnalysisId(ANALYSIS_ID);

            assertThat(response.getAnalysisId()).isEqualTo(ANALYSIS_ID);
        }

        @Test
        @DisplayName("Should set and get transactionId")
        void shouldSetAndGetTransactionId() {
            AnalysisResponse response = new AnalysisResponse();
            response.setTransactionId(TRANSACTION_ID);

            assertThat(response.getTransactionId()).isEqualTo(TRANSACTION_ID);
        }

        @Test
        @DisplayName("Should set and get userId")
        void shouldSetAndGetUserId() {
            AnalysisResponse response = new AnalysisResponse();
            response.setUserId(USER_ID);

            assertThat(response.getUserId()).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should set and get fraudScore")
        void shouldSetAndGetFraudScore() {
            AnalysisResponse response = new AnalysisResponse();
            response.setFraudScore(FRAUD_SCORE);

            assertThat(response.getFraudScore()).isEqualTo(FRAUD_SCORE);
        }

        @Test
        @DisplayName("Should set and get riskLevel")
        void shouldSetAndGetRiskLevel() {
            AnalysisResponse response = new AnalysisResponse();
            response.setRiskLevel(RISK_LEVEL);

            assertThat(response.getRiskLevel()).isEqualTo(RISK_LEVEL);
        }

        @Test
        @DisplayName("Should set and get action")
        void shouldSetAndGetAction() {
            AnalysisResponse response = new AnalysisResponse();
            response.setAction(ACTION);

            assertThat(response.getAction()).isEqualTo(ACTION);
        }

        @Test
        @DisplayName("Should set and get reasons")
        void shouldSetAndGetReasons() {
            AnalysisResponse response = new AnalysisResponse();
            response.setReasons(REASONS);

            assertThat(response.getReasons()).isEqualTo(REASONS);
        }

        @Test
        @DisplayName("Should set and get timestamp")
        void shouldSetAndGetTimestamp() {
            AnalysisResponse response = new AnalysisResponse();
            response.setTimestamp(TIMESTAMP);

            assertThat(response.getTimestamp()).isEqualTo(TIMESTAMP);
        }

        @Test
        @DisplayName("Should set and get modelVersion")
        void shouldSetAndGetModelVersion() {
            AnalysisResponse response = new AnalysisResponse();
            response.setModelVersion(MODEL_VERSION);

            assertThat(response.getModelVersion()).isEqualTo(MODEL_VERSION);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle minimum fraud score")
        void shouldHandleMinimumFraudScore() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.0)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(TIMESTAMP)
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getFraudScore()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should handle maximum fraud score")
        void shouldHandleMaximumFraudScore() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(1.0)
                    .riskLevel(RiskLevel.HIGH)
                    .recommendedAction(FraudAction.BLOCK)
                    .reasons(List.of("Maximum fraud"))
                    .timestamp(TIMESTAMP)
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getFraudScore()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should handle epoch timestamp")
        void shouldHandleEpochTimestamp() {
            FraudAnalysisResult domainResult = FraudAnalysisResult.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(Instant.EPOCH)
                    .modelVersion("1.0.0")
                    .build();

            AnalysisResponse response = AnalysisResponse.fromDomain(domainResult);

            assertThat(response.getTimestamp()).isEqualTo(Instant.EPOCH);
        }

        @Test
        @DisplayName("Should handle null timestamp via builder")
        void shouldHandleNullTimestampViaBuilder() {
            AnalysisResponse response = AnalysisResponse.builder()
                    .analysisId(ANALYSIS_ID)
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.5)
                    .riskLevel(RiskLevel.LOW)
                    .action(FraudAction.ALLOW)
                    .reasons(List.of())
                    .timestamp(null)
                    .modelVersion("1.0.0")
                    .build();

            assertThat(response.getTimestamp()).isNull();
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor Tests")
    class NoArgsConstructorTests {

        @Test
        @DisplayName("Should create instance with no-args constructor")
        void shouldCreateInstanceWithNoArgsConstructor() {
            AnalysisResponse response = new AnalysisResponse();

            assertThat(response).isNotNull();
            assertThat(response.getAnalysisId()).isNull();
            assertThat(response.getTransactionId()).isNull();
            assertThat(response.getUserId()).isNull();
            assertThat(response.getFraudScore()).isEqualTo(0.0);
            assertThat(response.getRiskLevel()).isNull();
            assertThat(response.getAction()).isNull();
            assertThat(response.getReasons()).isNull();
            assertThat(response.getTimestamp()).isNull();
            assertThat(response.getModelVersion()).isNull();
        }
    }
}
