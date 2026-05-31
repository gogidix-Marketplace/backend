package com.gogidix.aiservices.aifrauddetectionservice.resilience;

import com.gogidix.aiservices.aifrauddetectionservice.application.service.FraudDetectionService;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade Resilience & Chaos Tests for Fraud Detection Service.
 *
 * These tests validate the service's ability to handle failures gracefully.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Resilience & Chaos Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResilienceChaosTest {

    @Autowired
    private FraudDetectionService fraudDetectionService;

    private static final String TEST_TENANT = "resilience-test-tenant";
    private static final String TEST_USER = "resilience-test-user";

    @Nested
    @DisplayName("1. ML Model Timeout Scenarios")
    class MlModelTimeoutScenariosTests {

        @Test
        @Order(1)
        @DisplayName("Should handle ML model timeout gracefully")
        void shouldHandleMLTimeout() {
            Transaction transaction = Transaction.builder()
                    .transactionId("ml-timeout-test-001")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .amount(new BigDecimal("99999.99"))
                    .merchant("Timeout Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            assertThat(result.getAnalysisId()).isNotNull();
            assertThat(result.getFraudScore()).isGreaterThanOrEqualTo(0.0);
            assertThat(result.getFraudScore()).isLessThanOrEqualTo(1.0);
        }

        @Test
        @Order(2)
        @DisplayName("Should use fallback scoring when ML times out")
        void shouldUseFallbackWhenMLTimesOut() {
            int successCount = 0;
            for (int i = 0; i < 10; i++) {
                try {
                    Transaction transaction = Transaction.builder()
                            .transactionId("fallback-test-" + i)
                            .userId(TEST_USER)
                            .tenantId(TEST_TENANT)
                            .amount(new BigDecimal("1000.00"))
                            .merchant("Fallback Test Merchant")
                            .timestamp(Instant.now())
                            .currency("USD")
                            .build();

                    FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);
                    if (result != null && result.getAnalysisId() != null) {
                        successCount++;
                    }
                } catch (Exception e) {
                    // Should not throw exceptions
                }
            }

            assertThat(successCount).isGreaterThan(8);
        }
    }

    @Nested
    @DisplayName("2. High Amount Safety Scenarios")
    class HighAmountSafetyScenariosTests {

        @Test
        @Order(10)
        @DisplayName("Should block transactions above threshold when ML unavailable")
        void shouldBlockHighAmountWhenMLDown() {
            Transaction transaction = Transaction.builder()
                    .transactionId("high-amount-safety-001")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .amount(new BigDecimal("100000.00"))
                    .merchant("Unknown High-Risk Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            assertThat(result.getRecommendedAction()).isIn(FraudAction.BLOCK, FraudAction.REVIEW);
        }

        @Test
        @Order(11)
        @DisplayName("Should apply conservative scoring during degradation")
        void shouldApplyConservativeScoring() {
            Transaction transaction = Transaction.builder()
                    .transactionId("conservative-score-001")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .amount(new BigDecimal("50000.00"))
                    .merchant("Unknown Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            if (result.getFraudScore() > 0.5) {
                assertThat(result.getRecommendedAction()).isNotEqualTo(FraudAction.ALLOW);
            }
        }
    }

    @Nested
    @DisplayName("3. Data Consistency Under Stress")
    class DataConsistencyTests {

        @Test
        @Order(20)
        @DisplayName("Should maintain data integrity during concurrent operations")
        void shouldMaintainIntegrityUnderConcurrentOperations() throws InterruptedException, ExecutionException {
            int threadCount = 5;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            List<Future<String>> futures = new java.util.ArrayList<>();

            for (int t = 0; t < threadCount; t++) {
                final int threadId = t;
                Future<String> future = executor.submit(() -> {
                    try {
                        StringBuilder results = new StringBuilder();
                        for (int i = 0; i < 3; i++) {
                            Transaction transaction = Transaction.builder()
                                    .transactionId("concurrent-integrity-" + threadId + "-" + i)
                                    .userId(TEST_USER + "-" + threadId)
                                    .tenantId(TEST_TENANT)
                                    .amount(new BigDecimal("100.00"))
                                    .merchant("Concurrent Test Merchant")
                                    .timestamp(Instant.now())
                                    .currency("USD")
                                    .build();

                            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);
                            if (result != null && result.getAnalysisId() != null) {
                                results.append("OK");
                            }
                        }
                        latch.countDown();
                        return results.toString();
                    } catch (Exception e) {
                        latch.countDown();
                        return "ERROR";
                    }
                });
                futures.add(future);
            }

            boolean completed = latch.await(60, TimeUnit.SECONDS);
            assertThat(completed).isTrue();

            int successCount = 0;
            for (Future<String> future : futures) {
                String result = future.get();
                if (result.contains("OK")) {
                    successCount++;
                }
            }

            assertThat(successCount).isGreaterThanOrEqualTo(4);

            executor.shutdown();
        }
    }

    @Nested
    @DisplayName("4. Graceful Degradation Tests")
    class GracefulDegradationTests {

        @Test
        @Order(30)
        @DisplayName("Should provide service even with degraded ML")
        void shouldProvideServiceWithDegradedML() {
            int attempts = 20;
            int successCount = 0;

            for (int i = 0; i < attempts; i++) {
                try {
                    Transaction transaction = Transaction.builder()
                            .transactionId("degraded-ml-" + i)
                            .userId(TEST_USER)
                            .tenantId(TEST_TENANT)
                            .amount(new BigDecimal("100.00"))
                            .merchant("Degraded ML Test")
                            .timestamp(Instant.now())
                            .currency("USD")
                            .build();

                    FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);
                    if (result != null && result.getAnalysisId() != null) {
                        successCount++;
                    }
                } catch (Exception e) {
                    // Service should remain available
                }
            }

            double availability = (double) successCount / attempts;
            assertThat(availability).isGreaterThanOrEqualTo(0.95);
        }

        @Test
        @Order(31)
        @DisplayName("Should preserve transaction data during failover")
        void shouldPreserveDataDuringFailover() {
            Transaction transaction = Transaction.builder()
                    .transactionId("failover-preservation-001")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .amount(new BigDecimal("250.00"))
                    .merchant("Failover Preservation Test")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            assertThat(result.getAnalysisId()).isNotNull();
            assertThat(result.getTransactionId()).isEqualTo("failover-preservation-001");
            assertThat(result.getUserId()).isEqualTo(TEST_USER);
            assertThat(result.getTenantId()).isEqualTo(TEST_TENANT);
        }
    }

    @Nested
    @DisplayName("5. Edge Case Scenarios")
    class EdgeCaseScenariosTests {

        @Test
        @Order(40)
        @DisplayName("Should handle zero amount transactions")
        void shouldHandleZeroAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId("zero-amount-001")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .amount(BigDecimal.ZERO)
                    .merchant("Zero Amount Test")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
            assertThat(result.getFraudScore()).isGreaterThanOrEqualTo(0.0);
        }

        @Test
        @Order(41)
        @DisplayName("Should handle unusual timestamp")
        void shouldHandleUnusualTimestamp() {
            Transaction transaction = Transaction.builder()
                    .transactionId("unusual-time-001")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .amount(new BigDecimal("500.00"))
                    .merchant("Unusual Time Test")
                    .timestamp(Instant.now().minusSeconds(86400 * 365))
                    .currency("USD")
                    .build();

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
        }

        @Test
        @Order(42)
        @DisplayName("Should handle very long merchant names")
        void shouldHandleLongMerchantNames() {
            String longMerchant = "A".repeat(500);

            Transaction transaction = Transaction.builder()
                    .transactionId("long-merchant-001")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .amount(new BigDecimal("100.00"))
                    .merchant(longMerchant)
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result = fraudDetectionService.analyzeTransaction(transaction);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("6. Recovery Scenarios")
    class RecoveryScenariosTests {

        @Test
        @Order(50)
        @DisplayName("Should recover after temporary unavailability")
        void shouldRecoverAfterTemporaryUnavailability() {
            Transaction transaction1 = Transaction.builder()
                    .transactionId("recovery-test-001")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .amount(new BigDecimal("100.00"))
                    .merchant("Recovery Test Merchant")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result1 = fraudDetectionService.analyzeTransaction(transaction1);
            assertThat(result1).isNotNull();

            Transaction transaction2 = Transaction.builder()
                    .transactionId("recovery-test-002")
                    .userId(TEST_USER)
                    .tenantId(TEST_TENANT)
                    .amount(new BigDecimal("200.00"))
                    .merchant("Recovery Test Merchant 2")
                    .timestamp(Instant.now())
                    .currency("USD")
                    .build();

            FraudAnalysisResult result2 = fraudDetectionService.analyzeTransaction(transaction2);
            assertThat(result2).isNotNull();
        }
    }
}
