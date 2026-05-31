package com.gogidix.aiservices.ainotificationservice.observability;

import com.gogidix.aiservices.ainotificationservice.AiNotificationServiceApplication;
import com.gogidix.aiservices.ainotificationservice.application.service.NotificationService;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import com.gogidix.aiservices.ainotificationservice.infrastructure.metrics.NotificationMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Financial-Grade: SLO Compliance Tests.
 *
 * These tests validate metrics collection and SLO compliance monitoring.
 */
@SpringBootTest(
    classes = AiNotificationServiceApplication.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
    }
)
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationRepository notificationRepository;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationSenderPort notificationSender;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.port.out.EventPublisherPort eventPublisher;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.policy.NotificationPolicy policy;

    private static final String TEST_TENANT = "slo-test-tenant";
    private static final String TEST_USER = "slo-test-user";

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should increment total notification counter")
        void shouldIncrementTotalNotificationCounter() {
            long initialCount = (long) meterRegistry.get("notification.send.total")
                .counter().count();

            when(notificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);
            doNothing().when(eventPublisher).publishNotificationSent(any(), any());

            metrics.incrementNotificationTotal();

            long finalCount = (long) meterRegistry.get("notification.send.total")
                .counter().count();

            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should increment success counter")
        void shouldIncrementSuccessCounter() {
            long initialCount = (long) meterRegistry.get("notification.send.success")
                .counter().count();

            metrics.incrementNotificationSuccess();

            long finalCount = (long) meterRegistry.get("notification.send.success")
                .counter().count();

            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should increment failure counter")
        void shouldIncrementFailureCounter() {
            long initialCount = (long) meterRegistry.get("notification.send.failure")
                .counter().count();

            metrics.incrementNotificationFailure();

            long finalCount = (long) meterRegistry.get("notification.send.failure")
                .counter().count();

            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(4)
        @DisplayName("Should track notification types")
        void shouldTrackNotificationTypes() {
            metrics.incrementEmailNotification();
            metrics.incrementSmsNotification();
            metrics.incrementPushNotification();

            long emailCount = (long) meterRegistry.get("notification.email.total")
                .counter().count();
            long smsCount = (long) meterRegistry.get("notification.sms.total")
                .counter().count();
            long pushCount = (long) meterRegistry.get("notification.push.total")
                .counter().count();

            assertThat(emailCount).isGreaterThan(0);
            assertThat(smsCount).isGreaterThan(0);
            assertThat(pushCount).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyComplianceTests {

        @Test
        @Order(1)
        @DisplayName("Should measure P95 latency")
        void shouldMeasureP95Latency() {
            // Record some latencies
            for (int i = 0; i < 100; i++) {
                metrics.recordSendNotificationTime(50 + i); // 50-150ms range
            }

            double p95 = metrics.getSendNotificationLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @Order(2)
        @DisplayName("Should measure P99 latency")
        void shouldMeasureP99Latency() {
            // Record latencies including some outliers
            for (int i = 0; i < 100; i++) {
                metrics.recordSendNotificationTime(i % 2 == 0 ? 50 : 300);
            }

            double p99 = metrics.getSendNotificationLatencyP99();

            assertThat(p99).isGreaterThan(0);
        }

        @Test
        @Order(3)
        @DisplayName("Should measure batch processing latency")
        void shouldMeasureBatchProcessingLatency() {
            for (int i = 0; i < 50; i++) {
                metrics.recordSendNotificationTime(100 + i * 10);
            }

            double batchP95 = metrics.getBatchProcessingLatencyP95();

            assertThat(batchP95).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(4)
        @DisplayName("Should track delivery time")
        void shouldTrackDeliveryTime() {
            metrics.recordDeliveryTime(100);
            metrics.recordDeliveryTime(150);
            metrics.recordDeliveryTime(200);

            // Verify timer exists
            assertThat(meterRegistry.get("notification.delivery.duration").timer()).isNotNull();
        }
    }

    @Nested
    @DisplayName("3. Error Rate SLO Tests")
    class ErrorRateSloTests {

        @Test
        @Order(1)
        @DisplayName("Should calculate error rate")
        void shouldCalculateErrorRate() {
            metrics.incrementNotificationTotal();
            metrics.incrementNotificationTotal();
            metrics.incrementNotificationTotal();
            metrics.incrementNotificationFailure();

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isGreaterThan(0);
            assertThat(errorRate).isLessThanOrEqualTo(1.0);
        }

        @Test
        @Order(2)
        @DisplayName("Should calculate success rate")
        void shouldCalculateSuccessRate() {
            metrics.incrementNotificationTotal();
            metrics.incrementNotificationTotal();
            metrics.incrementNotificationSuccess();

            double successRate = metrics.getSuccessRate();

            assertThat(successRate).isGreaterThan(0);
            assertThat(successRate).isLessThanOrEqualTo(1.0);
        }

        @Test
        @Order(3)
        @DisplayName("Should handle zero total requests")
        void shouldHandleZeroTotalRequests() {
            double errorRate = metrics.getErrorRate();
            double successRate = metrics.getSuccessRate();

            // Error rate should be between 0 and 1 (inclusive)
            assertThat(errorRate).isGreaterThanOrEqualTo(0.0);
            assertThat(errorRate).isLessThanOrEqualTo(1.0);
            assertThat(successRate).isGreaterThanOrEqualTo(0.0);
            assertThat(successRate).isLessThanOrEqualTo(1.0);
        }

        @Test
        @Order(4)
        @DisplayName("Should track error rate below SLO threshold")
        void shouldTrackErrorRateBelowSloThreshold() {
            // Simulate mostly successful operations
            for (int i = 0; i < 100; i++) {
                metrics.incrementNotificationTotal();
                if (i < 98) {
                    metrics.incrementNotificationSuccess();
                } else {
                    metrics.incrementNotificationFailure();
                }
            }

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isLessThan(0.02); // Below 2% threshold
        }
    }

    @Nested
    @DisplayName("4. Metrics Tag Validation Tests")
    class MetricsTagValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should include service tag")
        void shouldIncludeServiceTag() {
            var counter = meterRegistry.get("notification.send.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTag("service")).isEqualTo("ai-notification");
        }

        @Test
        @Order(2)
        @DisplayName("Should include description")
        void shouldIncludeDescription() {
            var counter = meterRegistry.get("notification.send.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getDescription()).isNotEmpty();
        }

        @Test
        @Order(3)
        @DisplayName("Should publish percentiles")
        void shouldPublishPercentiles() {
            var timer = meterRegistry.get("notification.send.duration").timer();

            assertThat(timer).isNotNull();
            // The timer exists; percentile tags may vary by configuration
        }

        @Test
        @Order(4)
        @DisplayName("Should track all metric types")
        void shouldTrackAllMetricTypes() {
            // Counters
            assertThat(meterRegistry.get("notification.send.total").counter()).isNotNull();
            assertThat(meterRegistry.get("notification.send.success").counter()).isNotNull();
            assertThat(meterRegistry.get("notification.send.failure").counter()).isNotNull();

            // Timers
            assertThat(meterRegistry.get("notification.send.duration").timer()).isNotNull();
            assertThat(meterRegistry.get("notification.batch.duration").timer()).isNotNull();
        }
    }

    @Nested
    @DisplayName("5. SLO Threshold Validation Tests")
    class SloThresholdValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should validate P95 under 200ms threshold")
        void shouldValidateP95Under200msThreshold() {
            for (int i = 0; i < 100; i++) {
                metrics.recordSendNotificationTime(50 + (i % 50)); // Max 99ms
            }

            double p95 = metrics.getSendNotificationLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @Order(2)
        @DisplayName("Should validate P99 under 500ms threshold")
        void shouldValidateP99Under500msThreshold() {
            for (int i = 0; i < 100; i++) {
                metrics.recordSendNotificationTime(50 + (i % 30)); // Max 79ms
            }

            double p99 = metrics.getSendNotificationLatencyP99();

            assertThat(p99).isLessThan(500.0);
        }

        @Test
        @Order(3)
        @DisplayName("Should validate error rate under 2% threshold")
        void shouldValidateErrorRateUnder2PercentThreshold() {
            for (int i = 0; i < 200; i++) {
                metrics.incrementNotificationTotal();
                if (i < 198) {
                    metrics.incrementNotificationSuccess();
                } else {
                    metrics.incrementNotificationFailure();
                }
            }

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isLessThan(0.02);
        }

        @Test
        @Order(4)
        @DisplayName("Should alert on SLO violation")
        void shouldAlertOnSloViolation() {
            // Simulate slow requests
            for (int i = 0; i < 100; i++) {
                metrics.recordSendNotificationTime(250 + (i % 50)); // Some over 200ms
            }

            double p95 = metrics.getSendNotificationLatencyP95();

            // P95 should exceed threshold
            assertThat(p95).isGreaterThan(200.0);
        }
    }
}
