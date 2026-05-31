package com.gogidix.aiservices.aifrauddetectionservice.application.service;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.request.AnalyzeTransactionRequest;
import com.gogidix.aiservices.aifrauddetectionservice.application.dto.response.AnalysisResponse;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.*;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.FraudRepository;
import com.gogidix.aiservices.aifrauddetectionservice.domain.port.out.MlModelPort;
import com.gogidix.aiservices.aifrauddetectionservice.domain.policy.FraudDetectionPolicy;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.metrics.FraudDetectionMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Fraud Detection Service Application Tests")
class FraudDetectionServiceTest {

    @Mock
    private FraudRepository fraudRepository;

    @Mock
    private MlModelPort mlModelPort;

    @Mock
    private FraudDetectionPolicy policy;

    @Mock
    private FraudDetectionMetrics metrics;

    @InjectMocks
    private FraudDetectionService fraudDetectionService;

    private static final String TRANSACTION_ID = "txn-123";
    private static final String USER_ID = "user-456";

    @BeforeEach
    void setUp() {
        // Setup default mock behaviors for metrics
        lenient().doNothing().when(metrics).incrementAnalysisTotal();
        lenient().doNothing().when(metrics).incrementAnalysisSuccess();
        lenient().doNothing().when(metrics).incrementAnalysisFailure();
        lenient().doNothing().when(metrics).incrementFraudDetected();
        lenient().doNothing().when(metrics).incrementHighRisk();
        lenient().doNothing().when(metrics).incrementBlocked();
        lenient().doNothing().when(metrics).stopAnalysisTimer(any());
        lenient().doNothing().when(metrics).stopMlPredictionTimer(any());
        lenient().doNothing().when(metrics).stopDatabaseSaveTimer(any());
        lenient().when(metrics.startAnalysisTimer()).thenReturn(mock(io.micrometer.core.instrument.Timer.Sample.class));
        lenient().when(metrics.startMlPredictionTimer()).thenReturn(mock(io.micrometer.core.instrument.Timer.Sample.class));
        lenient().when(metrics.startDatabaseSaveTimer()).thenReturn(mock(io.micrometer.core.instrument.Timer.Sample.class));
    }

    @Nested
    @DisplayName("Transaction Analysis Tests")
    class AnalysisTests {

        @Test
        @DisplayName("Should analyze transaction successfully")
        void shouldAnalyzeTransaction() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.2);
            when(policy.classifyRisk(0.2)).thenReturn(RiskLevel.LOW);
            when(policy.determineAction(0.2)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.2))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.2))).thenReturn(0.2);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            assertThat(result.getTransactionId()).isEqualTo(TRANSACTION_ID);
            assertThat(result.getUserId()).isEqualTo(USER_ID);
            assertThat(result.getFraudScore()).isEqualTo(0.2);
            assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.LOW);
            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.ALLOW);

            verify(fraudRepository).saveAnalysisResult(any(FraudAnalysisResult.class));
        }

        @Test
        @DisplayName("Should reject invalid transaction")
        void shouldRejectInvalidTransaction() {
            Transaction transaction = Transaction.builder()
                    .transactionId("") // Invalid
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .build();

            doThrow(new IllegalArgumentException("Transaction ID is required"))
                    .when(policy).validateTransaction(transaction);

            assertThatThrownBy(() -> fraudDetectionService.analyzeTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Transaction ID is required");

            verify(mlModelPort, never()).predictFraudScore(any());
        }

        @Test
        @DisplayName("Should detect high-risk transaction")
        void shouldDetectHighRiskTransaction() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("15000")) // High amount
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.6);
            when(policy.adjustScore(any(), eq(0.6))).thenReturn(0.85);
            when(policy.classifyRisk(0.85)).thenReturn(RiskLevel.HIGH);
            when(policy.determineAction(0.85)).thenReturn(FraudAction.BLOCK);
            when(policy.getFraudReasons(any(), eq(0.85))).thenReturn(List.of("High-value transaction", "High fraud probability"));
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.BLOCK);
            assertThat(result.getReasons()).contains("High-value transaction");
        }

        @Test
        @DisplayName("Should flag medium-risk transaction for review")
        void shouldFlagForReview() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("5000"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.6);
            when(policy.adjustScore(any(), eq(0.6))).thenReturn(0.6);
            when(policy.classifyRisk(0.6)).thenReturn(RiskLevel.MEDIUM);
            when(policy.determineAction(0.6)).thenReturn(FraudAction.REVIEW);
            when(policy.getFraudReasons(any(), eq(0.6))).thenReturn(List.of("Suspicious activity"));
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.REVIEW);
        }
    }

    @Nested
    @DisplayName("Pattern Management Tests")
    class PatternManagementTests {

        @Test
        @DisplayName("Should get fraud patterns")
        void shouldGetPatterns() {
            List<FraudPattern> patterns = List.of(
                    FraudPattern.builder()
                            .patternId("pattern-1")
                            .patternName("Velocity Check")
                            .description("Multiple transactions in short time")
                            .confidenceScore(0.9)
                            .lastSeen(Instant.now())
                            .occurrenceCount(10)
                            .build()
            );

            when(fraudRepository.getActivePatterns()).thenReturn(patterns);

            List<FraudPattern> result = fraudDetectionService.getFraudPatterns();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPatternName()).isEqualTo("Velocity Check");
        }

        @Test
        @DisplayName("Should add new fraud pattern")
        void shouldAddPattern() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("pattern-2")
                    .patternName("Location Anomaly")
                    .description("Transaction from unusual location")
                    .confidenceScore(0.85)
                    .lastSeen(Instant.now())
                    .occurrenceCount(1)
                    .build();

            fraudDetectionService.addFraudPattern(pattern);

            verify(fraudRepository).savePattern(pattern);
        }
    }

    @Nested
    @DisplayName("History Retrieval Tests")
    class HistoryTests {

        @Test
        @DisplayName("Should get analysis result by ID")
        void shouldGetAnalysisById() {
            FraudAnalysisResult result = FraudAnalysisResult.builder()
                    .analysisId("analysis-123")
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .fraudScore(0.2)
                    .riskLevel(RiskLevel.LOW)
                    .recommendedAction(FraudAction.ALLOW)
                    .reasons(List.of("Normal transaction"))
                    .timestamp(Instant.now())
                    .modelVersion("1.0.0")
                    .build();

            when(fraudRepository.findAnalysisById("analysis-123")).thenReturn(Optional.of(result));

            FraudAnalysisResult found = fraudDetectionService.getAnalysisResult("analysis-123");

            assertThat(found).isNotNull();
            assertThat(found.getAnalysisId()).isEqualTo("analysis-123");
        }

        @Test
        @DisplayName("Should throw exception when analysis not found")
        void shouldThrowWhenNotFound() {
            when(fraudRepository.findAnalysisById("non-existent")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> fraudDetectionService.getAnalysisResult("non-existent"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should get user analysis history")
        void shouldGetUserHistory() {
            List<FraudAnalysisResult> history = List.of(
                    FraudAnalysisResult.builder()
                            .analysisId("analysis-1")
                            .transactionId("txn-1")
                            .userId(USER_ID)
                            .fraudScore(0.2)
                            .riskLevel(RiskLevel.LOW)
                            .recommendedAction(FraudAction.ALLOW)
                            .reasons(List.of("Normal"))
                            .timestamp(Instant.now())
                            .modelVersion("1.0.0")
                            .build()
            );

            when(fraudRepository.findByUserId(USER_ID, 10)).thenReturn(history);

            List<FraudAnalysisResult> result = fraudDetectionService.getUserAnalysisHistory(USER_ID, 10);

            assertThat(result).hasSize(1);
            verify(fraudRepository).findByUserId(USER_ID, 10);
        }
    }

    @Nested
    @DisplayName("Edge Cases Analysis Tests")
    class EdgeCaseAnalysisTests {

        @Test
        @DisplayName("Should handle transaction with null metadata")
        void shouldHandleNullMetadata() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .metadata(null)
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.2);
            when(policy.classifyRisk(0.2)).thenReturn(RiskLevel.LOW);
            when(policy.determineAction(0.2)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.2))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.2))).thenReturn(0.2);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            verify(fraudRepository).saveAnalysisResult(any(FraudAnalysisResult.class));
        }

        @Test
        @DisplayName("Should handle transaction with empty metadata")
        void shouldHandleEmptyMetadata() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .metadata(java.util.Map.of())
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.2);
            when(policy.classifyRisk(0.2)).thenReturn(RiskLevel.LOW);
            when(policy.determineAction(0.2)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.2))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.2))).thenReturn(0.2);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should handle transaction with minimum amount")
        void shouldHandleMinimumAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("0.01"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.1);
            when(policy.classifyRisk(0.1)).thenReturn(RiskLevel.LOW);
            when(policy.determineAction(0.1)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.1))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.1))).thenReturn(0.1);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            assertThat(result.getFraudScore()).isEqualTo(0.1);
        }

        @Test
        @DisplayName("Should handle transaction with very large amount")
        void shouldHandleVeryLargeAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("999999999.99"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.7);
            when(policy.adjustScore(any(), eq(0.7))).thenReturn(0.8);
            when(policy.classifyRisk(0.8)).thenReturn(RiskLevel.HIGH);
            when(policy.determineAction(0.8)).thenReturn(FraudAction.BLOCK);
            when(policy.getFraudReasons(any(), eq(0.8))).thenReturn(List.of("High-value transaction"));
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.BLOCK);
        }

        @Test
        @DisplayName("Should handle transaction with null timestamp")
        void shouldHandleNullTimestamp() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(null)
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.2);
            when(policy.classifyRisk(0.2)).thenReturn(RiskLevel.LOW);
            when(policy.determineAction(0.2)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.2))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.2))).thenReturn(0.2);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should handle transaction with null currency")
        void shouldHandleNullCurrency() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency(null)
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.2);
            when(policy.classifyRisk(0.2)).thenReturn(RiskLevel.LOW);
            when(policy.determineAction(0.2)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.2))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.2))).thenReturn(0.2);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should generate unique analysis ID for each transaction")
        void shouldGenerateUniqueAnalysisId() {
            Transaction transaction1 = Transaction.builder()
                    .transactionId("txn-1")
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            Transaction transaction2 = Transaction.builder()
                    .transactionId("txn-2")
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.2);
            when(policy.classifyRisk(0.2)).thenReturn(RiskLevel.LOW);
            when(policy.determineAction(0.2)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.2))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.2))).thenReturn(0.2);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result1 = fraudDetectionService.analyzeTransaction(transaction1);
            FraudAnalysisResult result2 = fraudDetectionService.analyzeTransaction(transaction2);

            assertThat(result1.getAnalysisId()).isNotEqualTo(result2.getAnalysisId());
        }
    }

    @Nested
    @DisplayName("Validation Error Tests")
    class ValidationErrorTests {

        @Test
        @DisplayName("Should reject transaction with null transaction ID")
        void shouldRejectNullTransactionId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(null)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            doThrow(new IllegalArgumentException("Transaction ID is required"))
                    .when(policy).validateTransaction(transaction);

            assertThatThrownBy(() -> fraudDetectionService.analyzeTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mlModelPort, never()).predictFraudScore(any());
            verify(fraudRepository, never()).saveAnalysisResult(any());
        }

        @Test
        @DisplayName("Should reject transaction with null user ID")
        void shouldRejectNullUserId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(null)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            doThrow(new IllegalArgumentException("User ID is required"))
                    .when(policy).validateTransaction(transaction);

            assertThatThrownBy(() -> fraudDetectionService.analyzeTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mlModelPort, never()).predictFraudScore(any());
        }

        @Test
        @DisplayName("Should reject transaction with empty user ID")
        void shouldRejectEmptyUserId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId("")
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            doThrow(new IllegalArgumentException("User ID is required"))
                    .when(policy).validateTransaction(transaction);

            assertThatThrownBy(() -> fraudDetectionService.analyzeTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should flag zero amount as suspicious")
        void shouldFlagZeroAmountAsSuspicious() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(BigDecimal.ZERO)
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            doNothing().when(policy).validateTransaction(transaction);
            when(mlModelPort.predictFraudScore(transaction)).thenReturn(0.1);
            when(policy.adjustScore(transaction, 0.1)).thenReturn(0.4); // Zero amounts get +0.3
            when(policy.classifyRisk(0.4)).thenReturn(RiskLevel.MEDIUM);
            when(policy.determineAction(0.4)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(transaction, 0.4)).thenReturn(List.of("Unusual zero-amount transaction"));

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            assertThat(result.getFraudScore()).isGreaterThan(0.3); // Should be flagged
        }

        @Test
        @DisplayName("Should reject transaction with negative amount")
        void shouldRejectNegativeAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("-100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            doThrow(new IllegalArgumentException("Transaction amount cannot be negative"))
                    .when(policy).validateTransaction(transaction);

            assertThatThrownBy(() -> fraudDetectionService.analyzeTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should reject transaction with null merchant")
        void shouldRejectNullMerchant() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(null)
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            doThrow(new IllegalArgumentException("Merchant is required"))
                    .when(policy).validateTransaction(transaction);

            assertThatThrownBy(() -> fraudDetectionService.analyzeTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should reject transaction with empty merchant")
        void shouldRejectEmptyMerchant() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            doThrow(new IllegalArgumentException("Merchant is required"))
                    .when(policy).validateTransaction(transaction);

            assertThatThrownBy(() -> fraudDetectionService.analyzeTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Repository Interaction Tests")
    class RepositoryInteractionTests {

        @Test
        @DisplayName("Should call repository save with correct result")
        void shouldCallRepositorySaveWithCorrectResult() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.5);
            when(policy.classifyRisk(0.5)).thenReturn(RiskLevel.MEDIUM);
            when(policy.determineAction(0.5)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.5))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.5))).thenReturn(0.5);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            fraudDetectionService.analyzeTransaction(transaction);

            verify(fraudRepository, times(1)).saveAnalysisResult(argThat(result ->
                    result.getTransactionId().equals(TRANSACTION_ID) &&
                    result.getUserId().equals(USER_ID) &&
                    result.getFraudScore() == 0.5 &&
                    result.getRiskLevel() == RiskLevel.MEDIUM
            ));
        }

        @Test
        @DisplayName("Should handle empty pattern list")
        void shouldHandleEmptyPatternList() {
            when(fraudRepository.getActivePatterns()).thenReturn(List.of());

            List<FraudPattern> result = fraudDetectionService.getFraudPatterns();

            assertThat(result).isEmpty();
            verify(fraudRepository).getActivePatterns();
        }

        @Test
        @DisplayName("Should handle multiple patterns")
        void shouldHandleMultiplePatterns() {
            List<FraudPattern> patterns = List.of(
                    FraudPattern.builder()
                            .patternId("pattern-1")
                            .patternName("Pattern 1")
                            .description("Description 1")
                            .confidenceScore(0.8)
                            .lastSeen(Instant.now())
                            .occurrenceCount(5)
                            .build(),
                    FraudPattern.builder()
                            .patternId("pattern-2")
                            .patternName("Pattern 2")
                            .description("Description 2")
                            .confidenceScore(0.9)
                            .lastSeen(Instant.now())
                            .occurrenceCount(10)
                            .build(),
                    FraudPattern.builder()
                            .patternId("pattern-3")
                            .patternName("Pattern 3")
                            .description("Description 3")
                            .confidenceScore(0.7)
                            .lastSeen(Instant.now())
                            .occurrenceCount(3)
                            .build()
            );

            when(fraudRepository.getActivePatterns()).thenReturn(patterns);

            List<FraudPattern> result = fraudDetectionService.getFraudPatterns();

            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("Should handle empty user history")
        void shouldHandleEmptyUserHistory() {
            when(fraudRepository.findByUserId(USER_ID, 10)).thenReturn(List.of());

            List<FraudAnalysisResult> result = fraudDetectionService.getUserAnalysisHistory(USER_ID, 10);

            assertThat(result).isEmpty();
            verify(fraudRepository).findByUserId(USER_ID, 10);
        }

        @Test
        @DisplayName("Should pass limit parameter to repository")
        void shouldPassLimitParameterToRepository() {
            when(fraudRepository.findByUserId(USER_ID, 50)).thenReturn(List.of());

            fraudDetectionService.getUserAnalysisHistory(USER_ID, 50);

            verify(fraudRepository).findByUserId(USER_ID, 50);
        }
    }

    @Nested
    @DisplayName("Score Boundary Tests")
    class ScoreBoundaryTests {

        @Test
        @DisplayName("Should handle score exactly at review threshold")
        void shouldHandleScoreAtReviewThreshold() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.5);
            when(policy.classifyRisk(0.5)).thenReturn(RiskLevel.MEDIUM);
            when(policy.determineAction(0.5)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.5))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.5))).thenReturn(0.5);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result.getFraudScore()).isEqualTo(0.5);
            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.ALLOW);
        }

        @Test
        @DisplayName("Should handle score exactly at block threshold")
        void shouldHandleScoreAtBlockThreshold() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.8);
            when(policy.classifyRisk(0.8)).thenReturn(RiskLevel.HIGH);
            when(policy.determineAction(0.8)).thenReturn(FraudAction.REVIEW);
            when(policy.getFraudReasons(any(), eq(0.8))).thenReturn(List.of("Suspicious activity"));
            when(policy.adjustScore(any(), eq(0.8))).thenReturn(0.8);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result.getFraudScore()).isEqualTo(0.8);
            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.REVIEW);
        }

        @Test
        @DisplayName("Should handle minimum score of 0.0")
        void shouldHandleMinimumScore() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(0.0);
            when(policy.classifyRisk(0.0)).thenReturn(RiskLevel.LOW);
            when(policy.determineAction(0.0)).thenReturn(FraudAction.ALLOW);
            when(policy.getFraudReasons(any(), eq(0.0))).thenReturn(List.of("Normal transaction"));
            when(policy.adjustScore(any(), eq(0.0))).thenReturn(0.0);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result.getFraudScore()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should handle maximum score of 1.0")
        void shouldHandleMaximumScore() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            when(mlModelPort.predictFraudScore(any(Transaction.class))).thenReturn(1.0);
            when(policy.classifyRisk(1.0)).thenReturn(RiskLevel.HIGH);
            when(policy.determineAction(1.0)).thenReturn(FraudAction.BLOCK);
            when(policy.getFraudReasons(any(), eq(1.0))).thenReturn(List.of("High fraud probability"));
            when(policy.adjustScore(any(), eq(1.0))).thenReturn(1.0);
            when(mlModelPort.getModelVersion()).thenReturn("1.0.0");

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result.getFraudScore()).isEqualTo(1.0);
            assertThat(result.getRecommendedAction()).isEqualTo(FraudAction.BLOCK);
        }
    }

    @Nested
    @DisplayName("Error Message Tests")
    class ErrorMessageTests {

        @Test
        @DisplayName("Should include analysis ID in not found exception message")
        void shouldIncludeAnalysisIdInExceptionMessage() {
            String analysisId = "missing-analysis-123";

            when(fraudRepository.findAnalysisById(analysisId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> fraudDetectionService.getAnalysisResult(analysisId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(analysisId);
        }
    }
}
