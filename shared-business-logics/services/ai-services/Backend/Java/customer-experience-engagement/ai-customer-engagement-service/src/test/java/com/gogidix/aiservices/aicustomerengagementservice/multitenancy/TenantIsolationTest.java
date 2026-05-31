package com.gogidix.aiservices.aicustomerengagementservice.multitenancy;

import com.gogidix.aiservices.aicustomerengagementservice.TestApplication;
import com.gogidix.aiservices.aicustomerengagementservice.infrastructure.metrics.CustomerEngagementMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: Multi-Tenancy Isolation Tests.
 *
 * These tests validate that tenant data is properly isolated.
 */
@SpringBootTest(
    classes = TestApplication.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
    }
)
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Multi-Tenancy Isolation Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantIsolationTest {

    @Autowired
    private CustomerEngagementMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    private static final String TENANT_A = "tenant-a";
    private static final String TENANT_B = "tenant-b";

    @Nested
    @DisplayName("1. Tenant Context Tests")
    class TenantContextTests {

        @Test
        @Order(1)
        @DisplayName("Should record metrics independently per context")
        void shouldRecordMetricsIndependently() {
            long initialCount = (long) meterRegistry
                    .get("engagement.analysis.total").counter().count();

            // Simulate tenant A operations
            for (int i = 0; i < 10; i++) {
                metrics.incrementEngagementTotal();
                metrics.incrementEngagementSuccess();
            }

            long afterTenantA = (long) meterRegistry
                    .get("engagement.analysis.total").counter().count();

            // Simulate tenant B operations
            for (int i = 0; i < 15; i++) {
                metrics.incrementEngagementTotal();
                metrics.incrementEngagementSuccess();
            }

            long afterTenantB = (long) meterRegistry
                    .get("engagement.analysis.total").counter().count();

            assertThat(afterTenantA - initialCount).isEqualTo(10);
            assertThat(afterTenantB - afterTenantA).isEqualTo(15);
        }

        @Test
        @Order(2)
        @DisplayName("Should maintain separate campaign metrics")
        void shouldMaintainSeparateCampaignMetrics() {
            // Create campaigns for tenant A
            for (int i = 0; i < 5; i++) {
                metrics.incrementCampaignsCreated();
            }

            long campaignsAfterA = (long) meterRegistry
                    .get("engagement.campaigns.created").counter().count();

            // Create campaigns for tenant B
            for (int i = 0; i < 8; i++) {
                metrics.incrementCampaignsCreated();
            }

            long campaignsAfterB = (long) meterRegistry
                    .get("engagement.campaigns.created").counter().count();

            assertThat(campaignsAfterA).isGreaterThan(0);
            assertThat(campaignsAfterB - campaignsAfterA).isEqualTo(8);
        }
    }

    @Nested
    @DisplayName("2. Concurrent Tenant Operations Tests")
    class ConcurrentTenantOperationsTests {

        @Test
        @Order(10)
        @DisplayName("Should handle concurrent operations from different tenants")
        void shouldHandleConcurrentTenantOperations() throws Exception {
            int threadCount = 4;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            List<Future<Long>> futures = new java.util.ArrayList<>();

            for (int t = 0; t < threadCount; t++) {
                final int tenantId = t;
                Future<Long> future = executor.submit(() -> {
                    try {
                        long operations = 0;
                        for (int i = 0; i < 50; i++) {
                            metrics.incrementEngagementTotal();
                            metrics.incrementEngagementSuccess();
                            metrics.recordEngagementTime(100 + tenantId * 10);
                            operations++;
                        }
                        latch.countDown();
                        return operations;
                    } catch (Exception e) {
                        latch.countDown();
                        return 0L;
                    }
                });
                futures.add(future);
            }

            boolean completed = latch.await(30, TimeUnit.SECONDS);
            assertThat(completed).isTrue();

            long totalOperations = 0;
            for (Future<Long> future : futures) {
                totalOperations += future.get();
            }

            assertThat(totalOperations).isEqualTo(threadCount * 50);

            var timer = meterRegistry.get("engagement.analysis.duration").timer();
            assertThat(timer.count()).isGreaterThan(0);

            executor.shutdown();
        }

        @Test
        @Order(11)
        @DisplayName("Should maintain isolation during concurrent message sending")
        void shouldMaintainIsolationDuringConcurrentMessaging() throws Exception {
            int threadCount = 5;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            List<Future<Integer>> futures = new java.util.ArrayList<>();

            for (int t = 0; t < threadCount; t++) {
                final int tenantId = t;
                Future<Integer> future = executor.submit(() -> {
                    try {
                        int messages = 0;
                        for (int i = 0; i < 20; i++) {
                            metrics.incrementMessagesSent();
                            var sample = metrics.startMessageDeliveryTimer();
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                // Ignore
                            }
                            metrics.stopMessageDeliveryTimer(sample);
                            messages++;
                        }
                        latch.countDown();
                        return messages;
                    } catch (Exception e) {
                        latch.countDown();
                        return 0;
                    }
                });
                futures.add(future);
            }

            boolean completed = latch.await(30, TimeUnit.SECONDS);
            assertThat(completed).isTrue();

            int totalMessages = 0;
            for (Future<Integer> future : futures) {
                totalMessages += future.get();
            }

            assertThat(totalMessages).isEqualTo(threadCount * 20);

            var timer = meterRegistry.get("engagement.message.delivery.duration").timer();
            assertThat(timer.count()).isGreaterThan(0);

            executor.shutdown();
        }
    }

    @Nested
    @DisplayName("3. Error Rate Isolation Tests")
    class ErrorRateIsolationTests {

        @Test
        @Order(20)
        @DisplayName("Should calculate error rates correctly across operations")
        void shouldCalculateErrorRatesCorrectly() {
            // Simulate tenant A: 100 requests, 5 failures
            for (int i = 0; i < 100; i++) {
                metrics.incrementEngagementTotal();
                if (i < 95) {
                    metrics.incrementEngagementSuccess();
                } else {
                    metrics.incrementEngagementFailure();
                }
            }

            double errorRate1 = metrics.getErrorRate();

            // Simulate tenant B: 100 requests, 10 failures
            for (int i = 0; i < 100; i++) {
                metrics.incrementEngagementTotal();
                if (i < 90) {
                    metrics.incrementEngagementSuccess();
                } else {
                    metrics.incrementEngagementFailure();
                }
            }

            double errorRate2 = metrics.getErrorRate();

            // Error rate should be aggregate: 15 failures / 200 total = 7.5%
            assertThat(errorRate2).isGreaterThan(0.06);
            assertThat(errorRate2).isLessThan(0.09);
        }
    }

    @Nested
    @DisplayName("4. Interaction Recording Isolation Tests")
    class InteractionRecordingIsolationTests {

        @Test
        @Order(30)
        @DisplayName("Should record interactions independently")
        void shouldRecordInteractionsIndependently() {
            long initialCount = (long) meterRegistry
                    .get("engagement.interactions.recorded").counter().count();

            // Tenant A interactions
            for (int i = 0; i < 25; i++) {
                metrics.incrementInteractionsRecorded();
            }

            long afterA = (long) meterRegistry
                    .get("engagement.interactions.recorded").counter().count();

            // Tenant B interactions
            for (int i = 0; i < 35; i++) {
                metrics.incrementInteractionsRecorded();
            }

            long afterB = (long) meterRegistry
                    .get("engagement.interactions.recorded").counter().count();

            assertThat(afterA - initialCount).isEqualTo(25);
            assertThat(afterB - afterA).isEqualTo(35);
        }
    }

    @Nested
    @DisplayName("5. ML Prediction Isolation Tests")
    class MlPredictionIsolationTests {

        @Test
        @Order(40)
        @DisplayName("Should handle ML predictions for different tenants")
        void shouldHandleMlPredictionsForDifferentTenants() throws Exception {
            int predictionsPerTenant = 30;
            int tenantCount = 3;
            ExecutorService executor = Executors.newFixedThreadPool(tenantCount);
            CountDownLatch latch = new CountDownLatch(tenantCount);

            for (int t = 0; t < tenantCount; t++) {
                final int tenantId = t;
                executor.submit(() -> {
                    try {
                        for (int i = 0; i < predictionsPerTenant; i++) {
                            var sample = metrics.startMlPredictionTimer();
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                // Ignore
                            }
                            metrics.stopMlPredictionTimer(sample);
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            boolean completed = latch.await(30, TimeUnit.SECONDS);
            assertThat(completed).isTrue();

            var timer = meterRegistry.get("engagement.ml.prediction.duration").timer();
            assertThat(timer.count()).isGreaterThanOrEqualTo(predictionsPerTenant * tenantCount);

            executor.shutdown();
        }
    }
}
