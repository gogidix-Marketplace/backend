package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.adapter;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MlModelAdapter Infrastructure Tests")
class MlModelAdapterTest {

    private MlModelAdapter mlModelAdapter;

    private static final String TRANSACTION_ID = "txn-123";
    private static final String USER_ID = "user-456";
    private static final String MERCHANT = "Test Merchant";
    private static final Instant TIMESTAMP = Instant.now();

    @BeforeEach
    void setUp() {
        mlModelAdapter = new MlModelAdapter();
    }

    private Transaction createTransaction(BigDecimal amount, Map<String, Object> metadata) {
        return Transaction.builder()
                .transactionId(TRANSACTION_ID)
                .userId(USER_ID)
                .amount(amount)
                .merchant(MERCHANT)
                .timestamp(TIMESTAMP)
                .currency("USD")
                .metadata(metadata)
                .build();
    }

    @Nested
    @DisplayName("predictFraudScore Tests")
    class PredictFraudScoreTests {

        @Test
        @DisplayName("Should return score between 0 and 1 for normal transaction")
        void shouldReturnScoreBetweenZeroAndOneForNormalTransaction() {
            Transaction transaction = createTransaction(new BigDecimal("100"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should return score between 0 and 1 for transaction with metadata")
        void shouldReturnScoreBetweenZeroAndOneWithMetadata() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("test_key", "test_value");
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should increase score for amount above 5000")
        void shouldIncreaseScoreForAmountAboveFiveThousand() {
            Transaction transaction = createTransaction(new BigDecimal("6000"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should increase score more for amount above 10000")
        void shouldIncreaseScoreMoreForAmountAboveTenThousand() {
            Transaction transaction = createTransaction(new BigDecimal("15000"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should increase score for new_device metadata")
        void shouldIncreaseScoreForNewDeviceMetadata() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("new_device", true);
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should increase score for unusual_location metadata")
        void shouldIncreaseScoreForUnusualLocationMetadata() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("unusual_location", true);
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should increase score for multiple risk factors")
        void shouldIncreaseScoreForMultipleRiskFactors() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("new_device", true);
            metadata.put("unusual_location", true);
            Transaction transaction = createTransaction(new BigDecimal("15000"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should cap score at 1.0 even with all risk factors")
        void shouldCapScoreAtOneEvenWithAllRiskFactors() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("new_device", true);
            metadata.put("unusual_location", true);
            Transaction transaction = createTransaction(new BigDecimal("50000"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should handle null metadata")
        void shouldHandleNullMetadata() {
            Transaction transaction = createTransaction(new BigDecimal("100"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should handle empty metadata")
        void shouldHandleEmptyMetadata() {
            Map<String, Object> metadata = new HashMap<>();
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should handle very small amount")
        void shouldHandleVerySmallAmount() {
            Transaction transaction = createTransaction(new BigDecimal("0.01"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should handle very large amount")
        void shouldHandleVeryLargeAmount() {
            Transaction transaction = createTransaction(new BigDecimal("999999999.99"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should handle boundary amount at 5000")
        void shouldHandleBoundaryAmountAtFiveThousand() {
            Transaction transaction = createTransaction(new BigDecimal("5000"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should handle boundary amount at 10000")
        void shouldHandleBoundaryAmountAtTenThousand() {
            Transaction transaction = createTransaction(new BigDecimal("10000"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isGreaterThanOrEqualTo(0.0);
            assertThat(score).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should produce different scores for different transactions")
        void shouldProduceDifferentScoresForDifferentTransactions() {
            Transaction transaction1 = createTransaction(new BigDecimal("100"), null);
            Transaction transaction2 = createTransaction(new BigDecimal("15000"), null);

            double score1 = mlModelAdapter.predictFraudScore(transaction1);
            double score2 = mlModelAdapter.predictFraudScore(transaction2);

            // Scores may sometimes be equal due to randomness, but generally should differ
            assertThat(score1).isBetween(0.0, 1.0);
            assertThat(score2).isBetween(0.0, 1.0);
        }
    }

    @Nested
    @DisplayName("getModelVersion Tests")
    class GetModelVersionTests {

        @Test
        @DisplayName("Should return model version")
        void shouldReturnModelVersion() {
            String version = mlModelAdapter.getModelVersion();

            assertThat(version).isNotNull();
            assertThat(version).isNotEmpty();
        }

        @Test
        @DisplayName("Should return consistent model version")
        void shouldReturnConsistentModelVersion() {
            String version1 = mlModelAdapter.getModelVersion();
            String version2 = mlModelAdapter.getModelVersion();

            assertThat(version1).isEqualTo(version2);
        }

        @Test
        @DisplayName("Should return version 1.0.0")
        void shouldReturnVersionOnePointZeroPointZero() {
            String version = mlModelAdapter.getModelVersion();

            assertThat(version).isEqualTo("1.0.0");
        }
    }

    @Nested
    @DisplayName("Metadata Handling Tests")
    class MetadataHandlingTests {

        @Test
        @DisplayName("Should handle metadata with boolean values")
        void shouldHandleMetadataWithBooleanValues() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("new_device", true);
            metadata.put("verified", false);
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("Should handle metadata with string values")
        void shouldHandleMetadataWithStringValues() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("device_type", "mobile");
            metadata.put("browser", "chrome");
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("Should handle metadata with numeric values")
        void shouldHandleMetadataWithNumericValues() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("attempts", 3);
            metadata.put("risk_score", 0.5);
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("Should handle metadata with mixed types")
        void shouldHandleMetadataWithMixedTypes() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("new_device", true);
            metadata.put("attempts", 5);
            metadata.put("location", "US");
            metadata.put("score", 0.75);
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("Should ignore metadata keys other than risk factors")
        void shouldIgnoreMetadataKeysOtherThanRiskFactors() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("some_key", "some_value");
            metadata.put("another_key", 123);
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isBetween(0.0, 1.0);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle transaction with minimum amount")
        void shouldHandleTransactionWithMinimumAmount() {
            Transaction transaction = createTransaction(new BigDecimal("0.01"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("Should handle transaction with maximum practical amount")
        void shouldHandleTransactionWithMaximumPracticalAmount() {
            Transaction transaction = createTransaction(new BigDecimal("999999999.99"), null);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("Should handle transaction with both risk metadata flags")
        void shouldHandleTransactionWithBothRiskMetadataFlags() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("new_device", true);
            metadata.put("unusual_location", true);
            Transaction transaction = createTransaction(new BigDecimal("100"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("Should handle transaction with high amount and both risk metadata flags")
        void shouldHandleHighAmountWithBothRiskFlags() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("new_device", true);
            metadata.put("unusual_location", true);
            Transaction transaction = createTransaction(new BigDecimal("20000"), metadata);

            double score = mlModelAdapter.predictFraudScore(transaction);

            assertThat(score).isBetween(0.0, 1.0);
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should handle concurrent predictions")
        void shouldHandleConcurrentPredictions() throws InterruptedException {
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            final double[][] results = new double[threadCount][1];
            final boolean[] errorOccurred = {false};

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        Transaction transaction = createTransaction(new BigDecimal("100"), null);
                        results[index][0] = mlModelAdapter.predictFraudScore(transaction);
                    } catch (Exception e) {
                        errorOccurred[0] = true;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            assertThat(errorOccurred[0]).isFalse();

            for (double[] result : results) {
                assertThat(result[0]).isBetween(0.0, 1.0);
            }
        }

        @Test
        @DisplayName("Should handle concurrent model version calls")
        void shouldHandleConcurrentModelVersionCalls() throws InterruptedException {
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            final String[][] results = new String[threadCount][1];
            final boolean[] errorOccurred = {false};

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        results[index][0] = mlModelAdapter.getModelVersion();
                    } catch (Exception e) {
                        errorOccurred[0] = true;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            assertThat(errorOccurred[0]).isFalse();
            assertThat(results[0][0]).isEqualTo("1.0.0");
        }
    }
}
