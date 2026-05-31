package com.gogidix.aiservices.aifrauddetectionservice.domain.policy;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("FraudDetectionPolicy Domain Policy Tests")
class FraudDetectionPolicyTest {

    private final FraudDetectionPolicy policy = new FraudDetectionPolicy();

    private static final String TRANSACTION_ID = "txn-123";
    private static final String USER_ID = "user-456";
    private static final String MERCHANT = "Test Merchant";
    private static final Instant TIMESTAMP = Instant.now();

    @Nested
    @DisplayName("validateTransaction Tests")
    class ValidateTransactionTests {

        @Test
        @DisplayName("Should validate correct transaction")
        void shouldValidateCorrectTransaction() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatCode(() -> policy.validateTransaction(transaction))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should reject null transaction ID")
        void shouldRejectNullTransactionId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(null)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatThrownBy(() -> policy.validateTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Transaction ID is required");
        }

        @Test
        @DisplayName("Should reject empty transaction ID")
        void shouldRejectEmptyTransactionId() {
            Transaction transaction = Transaction.builder()
                    .transactionId("")
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatThrownBy(() -> policy.validateTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Transaction ID is required");
        }

        @Test
        @DisplayName("Should reject null user ID")
        void shouldRejectNullUserId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(null)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatThrownBy(() -> policy.validateTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User ID is required");
        }

        @Test
        @DisplayName("Should reject empty user ID")
        void shouldRejectEmptyUserId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId("")
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatThrownBy(() -> policy.validateTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User ID is required");
        }

        @Test
        @DisplayName("Should reject null amount")
        void shouldRejectNullAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(null)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatThrownBy(() -> policy.validateTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Transaction amount cannot be negative");
        }

        @Test
        @DisplayName("Should accept zero amount but flag it as unusual")
        void shouldAcceptZeroAmountButFlagIt() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(BigDecimal.ZERO)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            // Zero amounts are now allowed for resilience testing
            assertThatCode(() -> policy.validateTransaction(transaction))
                    .doesNotThrowAnyException();

            // But they should be flagged with a higher fraud score
            double adjustedScore = policy.adjustScore(transaction, 0.1);
            assertThat(adjustedScore).isGreaterThan(0.3); // Should be 0.4 with +0.3 adjustment
        }

        @Test
        @DisplayName("Should reject negative amount")
        void shouldRejectNegativeAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("-100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatThrownBy(() -> policy.validateTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Transaction amount cannot be negative");
        }

        @Test
        @DisplayName("Should reject null merchant")
        void shouldRejectNullMerchant() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(null)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatThrownBy(() -> policy.validateTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Merchant is required");
        }

        @Test
        @DisplayName("Should reject empty merchant")
        void shouldRejectEmptyMerchant() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant("")
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatThrownBy(() -> policy.validateTransaction(transaction))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Merchant is required");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should accept null timestamp")
        void shouldAcceptNullTimestamp(Instant nullTimestamp) {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(nullTimestamp)
                    .currency("USD")
                    .build();

            assertThatCode(() -> policy.validateTransaction(transaction))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should accept very small positive amount")
        void shouldAcceptVerySmallPositiveAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("0.01"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            assertThatCode(() -> policy.validateTransaction(transaction))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("adjustScore Tests")
    class AdjustScoreTests {

        @Test
        @DisplayName("Should not adjust score for normal transaction")
        void shouldNotAdjustScoreForNormalTransaction() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double adjustedScore = policy.adjustScore(transaction, 0.3);

            assertThat(adjustedScore).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should increase score for high amount transaction")
        void shouldIncreaseScoreForHighAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("15000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double adjustedScore = policy.adjustScore(transaction, 0.5);

            assertThat(adjustedScore).isEqualTo(0.6);
        }

        @Test
        @DisplayName("Should cap score at 1.0 for high amount transaction")
        void shouldCapScoreAtOneForHighAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("15000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double adjustedScore = policy.adjustScore(transaction, 0.95);

            assertThat(adjustedScore).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should cap score at 1.0 for very high raw score")
        void shouldCapScoreAtOneForVeryHighRawScore() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("5000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double adjustedScore = policy.adjustScore(transaction, 1.5);

            assertThat(adjustedScore).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should handle boundary amount exactly at threshold")
        void shouldHandleBoundaryAmountAtThreshold() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("10000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double adjustedScore = policy.adjustScore(transaction, 0.4);

            assertThat(adjustedScore).isEqualTo(0.4);
        }

        @Test
        @DisplayName("Should handle amount just above threshold")
        void shouldHandleAmountJustAboveThreshold() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("10000.01"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double adjustedScore = policy.adjustScore(transaction, 0.4);

            assertThat(adjustedScore).isEqualTo(0.5);
        }

        @ParameterizedTest
        @CsvSource({
            "0.0, 0.1",
            "0.1, 0.2",
            "0.5, 0.6",
            "0.9, 1.0",
            "-0.1, 0.0"
        })
        @DisplayName("Should handle various raw scores for high amount")
        void shouldHandleVariousRawScoresForHighAmount(double rawScore, double expected) {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("20000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double adjustedScore = policy.adjustScore(transaction, rawScore);

            assertThat(adjustedScore).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("classifyRisk Tests")
    class ClassifyRiskTests {

        @ParameterizedTest
        @CsvSource({
            "0.0, LOW",
            "0.1, LOW",
            "0.2, LOW",
            "0.3, LOW"
        })
        @DisplayName("Should classify low risk for scores <= 0.3")
        void shouldClassifyLowRisk(double score, RiskLevel expected) {
            RiskLevel riskLevel = policy.classifyRisk(score);

            assertThat(riskLevel).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "0.31, MEDIUM",
            "0.4, MEDIUM",
            "0.5, MEDIUM",
            "0.6, MEDIUM",
            "0.7, MEDIUM"
        })
        @DisplayName("Should classify medium risk for scores > 0.3 and <= 0.7")
        void shouldClassifyMediumRisk(double score, RiskLevel expected) {
            RiskLevel riskLevel = policy.classifyRisk(score);

            assertThat(riskLevel).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "0.71, HIGH",
            "0.8, HIGH",
            "0.9, HIGH",
            "1.0, HIGH"
        })
        @DisplayName("Should classify high risk for scores > 0.7")
        void shouldClassifyHighRisk(double score, RiskLevel expected) {
            RiskLevel riskLevel = policy.classifyRisk(score);

            assertThat(riskLevel).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle boundary at 0.3")
        void shouldHandleBoundaryAtPointThree() {
            assertThat(policy.classifyRisk(0.3)).isEqualTo(RiskLevel.LOW);
            assertThat(policy.classifyRisk(0.31)).isEqualTo(RiskLevel.MEDIUM);
        }

        @Test
        @DisplayName("Should handle boundary at 0.7")
        void shouldHandleBoundaryAtPointSeven() {
            assertThat(policy.classifyRisk(0.7)).isEqualTo(RiskLevel.MEDIUM);
            assertThat(policy.classifyRisk(0.71)).isEqualTo(RiskLevel.HIGH);
        }

        @ParameterizedTest
        @ValueSource(doubles = {-0.1, -1.0, -10.0})
        @DisplayName("Should handle negative scores")
        void shouldHandleNegativeScores(double score) {
            RiskLevel riskLevel = policy.classifyRisk(score);

            assertThat(riskLevel).isEqualTo(RiskLevel.LOW);
        }

        @ParameterizedTest
        @ValueSource(doubles = {1.1, 2.0, 10.0})
        @DisplayName("Should handle scores above 1.0")
        void shouldHandleScoresAboveOne(double score) {
            RiskLevel riskLevel = policy.classifyRisk(score);

            assertThat(riskLevel).isEqualTo(RiskLevel.HIGH);
        }
    }

    @Nested
    @DisplayName("determineAction Tests")
    class DetermineActionTests {

        @ParameterizedTest
        @CsvSource({
            "0.0, ALLOW",
            "0.3, ALLOW",
            "0.5, ALLOW"
        })
        @DisplayName("Should allow for scores <= 0.5")
        void shouldAllowForLowScores(double score, FraudAction expected) {
            FraudAction action = policy.determineAction(score);

            assertThat(action).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "0.51, REVIEW",
            "0.6, REVIEW",
            "0.7, REVIEW",
            "0.8, REVIEW"
        })
        @DisplayName("Should review for scores > 0.5 and <= 0.8")
        void shouldReviewForMediumScores(double score, FraudAction expected) {
            FraudAction action = policy.determineAction(score);

            assertThat(action).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "0.81, BLOCK",
            "0.9, BLOCK",
            "1.0, BLOCK"
        })
        @DisplayName("Should block for scores > 0.8")
        void shouldBlockForHighScores(double score, FraudAction expected) {
            FraudAction action = policy.determineAction(score);

            assertThat(action).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle boundary at 0.5")
        void shouldHandleBoundaryAtPointFive() {
            assertThat(policy.determineAction(0.5)).isEqualTo(FraudAction.ALLOW);
            assertThat(policy.determineAction(0.51)).isEqualTo(FraudAction.REVIEW);
        }

        @Test
        @DisplayName("Should handle boundary at 0.8")
        void shouldHandleBoundaryAtPointEight() {
            assertThat(policy.determineAction(0.8)).isEqualTo(FraudAction.REVIEW);
            assertThat(policy.determineAction(0.81)).isEqualTo(FraudAction.BLOCK);
        }
    }

    @Nested
    @DisplayName("getFraudReasons Tests")
    class GetFraudReasonsTests {

        @Test
        @DisplayName("Should return normal reason for low score")
        void shouldReturnNormalReasonForLowScore() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons = policy.getFraudReasons(transaction, 0.2);

            assertThat(reasons).containsExactly("Normal transaction pattern");
        }

        @Test
        @DisplayName("Should return suspicious activity reason for medium score")
        void shouldReturnSuspiciousReasonForMediumScore() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons = policy.getFraudReasons(transaction, 0.6);

            assertThat(reasons).contains("Suspicious activity pattern");
        }

        @Test
        @DisplayName("Should return high fraud probability reason for high score")
        void shouldReturnHighFraudReasonForHighScore() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons = policy.getFraudReasons(transaction, 0.9);

            assertThat(reasons).contains("High fraud probability detected");
        }

        @Test
        @DisplayName("Should return high value transaction reason")
        void shouldReturnHighValueReason() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("15000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons = policy.getFraudReasons(transaction, 0.3);

            assertThat(reasons).contains("High-value transaction");
        }

        @Test
        @DisplayName("Should return multiple reasons for high amount with high score")
        void shouldReturnMultipleReasons() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("15000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons = policy.getFraudReasons(transaction, 0.85);

            assertThat(reasons)
                    .contains("High fraud probability detected")
                    .contains("High-value transaction");
            assertThat(reasons).hasSize(2);
        }

        @Test
        @DisplayName("Should handle all three reasons combined")
        void shouldHandleAllThreeReasons() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("15000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons = policy.getFraudReasons(transaction, 0.75);

            assertThat(reasons)
                    .contains("Suspicious activity pattern")
                    .contains("High-value transaction");
            assertThat(reasons).hasSize(2);
        }

        @Test
        @DisplayName("Should handle boundary at 0.8")
        void shouldHandleBoundaryAtPointEight() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons = policy.getFraudReasons(transaction, 0.8);

            assertThat(reasons).doesNotContain("High fraud probability detected");
        }

        @Test
        @DisplayName("Should handle boundary at 0.5")
        void shouldHandleBoundaryAtPointFive() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons = policy.getFraudReasons(transaction, 0.5);

            assertThat(reasons).doesNotContain("Suspicious activity pattern");
        }

        @Test
        @DisplayName("Should handle boundary at high amount threshold")
        void shouldHandleBoundaryAtHighAmountThreshold() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("10000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons = policy.getFraudReasons(transaction, 0.3);

            assertThat(reasons).doesNotContain("High-value transaction");
        }

        @Test
        @DisplayName("Should return non-empty list for all scenarios")
        void shouldReturnNonEmptyListForAllScenarios() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            var reasons1 = policy.getFraudReasons(transaction, 0.1);
            var reasons2 = policy.getFraudReasons(transaction, 0.6);
            var reasons3 = policy.getFraudReasons(transaction, 0.9);

            assertThat(reasons1).isNotEmpty();
            assertThat(reasons2).isNotEmpty();
            assertThat(reasons3).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should process low risk transaction end to end")
        void shouldProcessLowRiskTransaction() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("50"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double rawScore = 0.2;
            double adjustedScore = policy.adjustScore(transaction, rawScore);
            RiskLevel riskLevel = policy.classifyRisk(adjustedScore);
            FraudAction action = policy.determineAction(adjustedScore);
            var reasons = policy.getFraudReasons(transaction, adjustedScore);

            assertThat(adjustedScore).isEqualTo(0.2);
            assertThat(riskLevel).isEqualTo(RiskLevel.LOW);
            assertThat(action).isEqualTo(FraudAction.ALLOW);
            assertThat(reasons).contains("Normal transaction pattern");
        }

        @Test
        @DisplayName("Should process high amount transaction end to end")
        void shouldProcessHighAmountTransaction() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("20000"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double rawScore = 0.6;
            double adjustedScore = policy.adjustScore(transaction, rawScore);
            RiskLevel riskLevel = policy.classifyRisk(adjustedScore);
            FraudAction action = policy.determineAction(adjustedScore);
            var reasons = policy.getFraudReasons(transaction, adjustedScore);

            assertThat(adjustedScore).isEqualTo(0.7);
            assertThat(riskLevel).isEqualTo(RiskLevel.MEDIUM);
            assertThat(action).isEqualTo(FraudAction.REVIEW);
            assertThat(reasons).contains("High-value transaction");
        }

        @Test
        @DisplayName("Should process high fraud score transaction end to end")
        void shouldProcessHighFraudScoreTransaction() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(new BigDecimal("100"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("USD")
                    .build();

            double rawScore = 0.85;
            double adjustedScore = policy.adjustScore(transaction, rawScore);
            RiskLevel riskLevel = policy.classifyRisk(adjustedScore);
            FraudAction action = policy.determineAction(adjustedScore);
            var reasons = policy.getFraudReasons(transaction, adjustedScore);

            assertThat(adjustedScore).isEqualTo(0.85);
            assertThat(riskLevel).isEqualTo(RiskLevel.HIGH);
            assertThat(action).isEqualTo(FraudAction.BLOCK);
            assertThat(reasons).contains("High fraud probability detected");
        }
    }
}
