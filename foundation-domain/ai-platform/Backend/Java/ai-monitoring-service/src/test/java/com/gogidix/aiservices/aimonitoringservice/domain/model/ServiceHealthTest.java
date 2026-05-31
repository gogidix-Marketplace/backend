package com.gogidix.aiservices.aimonitoringservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ServiceHealth Domain Entity Tests")
class ServiceHealthTest {

    private static final String SERVICE_NAME = "ai-model-service";
    private static final String TENANT_ID = "tenant-123";

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create with service name and tenant ID")
        void shouldCreateWithServiceNameAndTenantId() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            assertThat(health).isNotNull();
            assertThat(health.getServiceName()).isEqualTo(SERVICE_NAME);
            assertThat(health.getTenantId()).isEqualTo(TENANT_ID);
        }

        @Test
        @DisplayName("Should generate unique ID on creation")
        void shouldGenerateUniqueIdOnCreation() {
            ServiceHealth health1 = new ServiceHealth(SERVICE_NAME, TENANT_ID);
            ServiceHealth health2 = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            assertThat(health1.getId()).isNotNull();
            assertThat(health2.getId()).isNotNull();
            assertThat(health1.getId()).isNotEqualTo(health2.getId());
        }

        @Test
        @DisplayName("Should initialize status as UNKNOWN")
        void shouldInitializeStatusAsUnknown() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            assertThat(health.getStatus()).isEqualTo(ServiceHealth.HealthStatus.UNKNOWN);
        }

        @Test
        @DisplayName("Should initialize timestamps on creation")
        void shouldInitializeTimestampsOnCreation() {
            Instant before = Instant.now();
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);
            Instant after = Instant.now();

            assertThat(health.getCreatedAt()).isBetween(before, after);
            assertThat(health.getLastCheckAt()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should initialize counters to zero")
        void shouldInitializeCountersToZero() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            assertThat(health.getTotalChecks()).isEqualTo(0L);
            assertThat(health.getFailedChecks()).isEqualTo(0L);
        }

        @Test
        @DisplayName("Should have zero success rate initially")
        void shouldHaveZeroSuccessRateInitially() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            assertThat(health.getSuccessRate()).isEqualTo(0.0);
        }
    }

    @Nested
    @DisplayName("Status Update Tests")
    class StatusUpdateTests {

        @Test
        @DisplayName("Should update status to UP")
        void shouldUpdateStatusToUp() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "Service is healthy");

            assertThat(health.getStatus()).isEqualTo(ServiceHealth.HealthStatus.UP);
            assertThat(health.getMessage()).isEqualTo("Service is healthy");
        }

        @Test
        @DisplayName("Should update status to DOWN")
        void shouldUpdateStatusToDown() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.DOWN, "Service is down");

            assertThat(health.getStatus()).isEqualTo(ServiceHealth.HealthStatus.DOWN);
            assertThat(health.getMessage()).isEqualTo("Service is down");
        }

        @Test
        @DisplayName("Should update status to DEGRADED")
        void shouldUpdateStatusToDegraded() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.DEGRADED, "Service is degraded");

            assertThat(health.getStatus()).isEqualTo(ServiceHealth.HealthStatus.DEGRADED);
            assertThat(health.getMessage()).isEqualTo("Service is degraded");
        }

        @Test
        @DisplayName("Should increment total checks on status update")
        void shouldIncrementTotalChecksOnStatusUpdate() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");
            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");

            assertThat(health.getTotalChecks()).isEqualTo(2L);
        }

        @Test
        @DisplayName("Should increment failed checks for DOWN status")
        void shouldIncrementFailedChecksForDownStatus() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");
            health.updateStatus(ServiceHealth.HealthStatus.DOWN, "Failed");
            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");

            assertThat(health.getFailedChecks()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should increment failed checks for DEGRADED status")
        void shouldIncrementFailedChecksForDegradedStatus() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");
            health.updateStatus(ServiceHealth.HealthStatus.DEGRADED, "Slow");
            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");

            assertThat(health.getFailedChecks()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should update last check timestamp")
        void shouldUpdateLastCheckTimestamp() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);
            Instant originalTimestamp = health.getLastCheckAt();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");

            assertThat(health.getLastCheckAt()).isAfter(originalTimestamp);
        }
    }

    @Nested
    @DisplayName("Metrics Update Tests")
    class MetricsUpdateTests {

        @Test
        @DisplayName("Should update metrics")
        void shouldUpdateMetrics() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);
            Map<String, Object> metrics = Map.of(
                    "cpu", 50.0,
                    "memory", 75.0,
                    "requests", 1000L
            );

            health.updateMetrics(metrics);

            assertThat(health.getMetrics()).isEqualTo(metrics);
        }

        @Test
        @DisplayName("Should update last check timestamp when metrics updated")
        void shouldUpdateLastCheckTimestampWhenMetricsUpdated() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);
            Instant originalTimestamp = health.getLastCheckAt();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            health.updateMetrics(Map.of("cpu", 50.0));

            assertThat(health.getLastCheckAt()).isAfter(originalTimestamp);
        }

        @Test
        @DisplayName("Should replace existing metrics")
        void shouldReplaceExistingMetrics() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateMetrics(Map.of("cpu", 50.0));
            health.updateMetrics(Map.of("memory", 75.0));

            assertThat(health.getMetrics()).hasSize(1);
            assertThat(health.getMetrics()).containsKey("memory");
            assertThat(health.getMetrics()).doesNotContainKey("cpu");
        }

        @Test
        @DisplayName("Should accept null metrics")
        void shouldAcceptNullMetrics() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateMetrics(null);

            assertThat(health.getMetrics()).isNull();
        }
    }

    @Nested
    @DisplayName("Success Rate Calculation Tests")
    class SuccessRateCalculationTests {

        @Test
        @DisplayName("Should calculate 100% success rate with no failures")
        void shouldCalculate100PercentSuccessRateWithNoFailures() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");
            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");
            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");

            assertThat(health.getSuccessRate()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should calculate 50% success rate with half failures")
        void shouldCalculate50PercentSuccessRateWithHalfFailures() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");
            health.updateStatus(ServiceHealth.HealthStatus.DOWN, "Failed");

            assertThat(health.getSuccessRate()).isEqualTo(0.5);
        }

        @Test
        @DisplayName("Should calculate 0% success rate with all failures")
        void shouldCalculate0PercentSuccessRateWithAllFailures() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.DOWN, "Failed");
            health.updateStatus(ServiceHealth.HealthStatus.DOWN, "Failed");

            assertThat(health.getSuccessRate()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should return 0.0 when no checks performed")
        void shouldReturn0WhenNoChecksPerformed() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            assertThat(health.getSuccessRate()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should count DEGRADED as failure")
        void shouldCountDegradedAsFailure() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");
            health.updateStatus(ServiceHealth.HealthStatus.DEGRADED, "Slow");

            assertThat(health.getSuccessRate()).isEqualTo(0.5);
        }
    }

    @Nested
    @DisplayName("HealthStatus Enum Tests")
    class HealthStatusEnumTests {

        @ParameterizedTest
        @EnumSource(ServiceHealth.HealthStatus.class)
        @DisplayName("Should have all health status values")
        void shouldHaveAllHealthStatusValues(ServiceHealth.HealthStatus status) {
            assertThat(status).isNotNull();
        }

        @Test
        @DisplayName("Should have UP status")
        void shouldHaveUpStatus() {
            assertThat(ServiceHealth.HealthStatus.valueOf("UP")).isEqualTo(ServiceHealth.HealthStatus.UP);
        }

        @Test
        @DisplayName("Should have DOWN status")
        void shouldHaveDownStatus() {
            assertThat(ServiceHealth.HealthStatus.valueOf("DOWN")).isEqualTo(ServiceHealth.HealthStatus.DOWN);
        }

        @Test
        @DisplayName("Should have DEGRADED status")
        void shouldHaveDegradedStatus() {
            assertThat(ServiceHealth.HealthStatus.valueOf("DEGRADED")).isEqualTo(ServiceHealth.HealthStatus.DEGRADED);
        }

        @Test
        @DisplayName("Should have UNKNOWN status")
        void shouldHaveUnknownStatus() {
            assertThat(ServiceHealth.HealthStatus.valueOf("UNKNOWN")).isEqualTo(ServiceHealth.HealthStatus.UNKNOWN);
        }

        @Test
        @DisplayName("Should have 4 status values")
        void shouldHave4StatusValues() {
            assertThat(ServiceHealth.HealthStatus.values()).hasSize(4);
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should track healthy service lifecycle")
        void shouldTrackHealthyServiceLifecycle() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "Service started");
            health.updateMetrics(Map.of("cpu", 30.0));

            assertThat(health.getStatus()).isEqualTo(ServiceHealth.HealthStatus.UP);
            assertThat(health.getTotalChecks()).isEqualTo(1L);
            assertThat(health.getSuccessRate()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should track service degradation")
        void shouldTrackServiceDegradation() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "Normal");
            health.updateStatus(ServiceHealth.HealthStatus.UP, "Normal");
            health.updateStatus(ServiceHealth.HealthStatus.DEGRADED, "High latency");
            health.updateStatus(ServiceHealth.HealthStatus.UP, "Recovered");

            assertThat(health.getTotalChecks()).isEqualTo(4L);
            assertThat(health.getFailedChecks()).isEqualTo(1L);
            assertThat(health.getSuccessRate()).isEqualTo(0.75);
        }

        @Test
        @DisplayName("Should track service outage")
        void shouldTrackServiceOutage() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateStatus(ServiceHealth.HealthStatus.UP, "Normal");
            health.updateStatus(ServiceHealth.HealthStatus.DOWN, "Connection lost");
            health.updateStatus(ServiceHealth.HealthStatus.DOWN, "Still down");
            health.updateStatus(ServiceHealth.HealthStatus.UP, "Recovered");

            assertThat(health.getTotalChecks()).isEqualTo(4L);
            assertThat(health.getFailedChecks()).isEqualTo(2L);
            assertThat(health.getSuccessRate()).isEqualTo(0.5);
        }

        @Test
        @DisplayName("Should handle service with dynamic metrics")
        void shouldHandleServiceWithDynamicMetrics() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);
            Map<String, Object> initialMetrics = new HashMap<>();
            initialMetrics.put("cpu", 50.0);
            initialMetrics.put("memory", 60.0);

            health.updateMetrics(initialMetrics);

            Map<String, Object> updatedMetrics = new HashMap<>();
            updatedMetrics.put("cpu", 75.0);
            updatedMetrics.put("memory", 80.0);
            updatedMetrics.put("disk", 45.0);

            health.updateMetrics(updatedMetrics);

            assertThat(health.getMetrics()).hasSize(3);
            assertThat(health.getMetrics().get("cpu")).isEqualTo(75.0);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle long service name")
        void shouldHandleLongServiceName() {
            String longName = "this-is-a-very-long-service-name-that-exceeds-normal-length".repeat(2);

            ServiceHealth health = new ServiceHealth(longName, TENANT_ID);

            assertThat(health.getServiceName()).isEqualTo(longName);
        }

        @Test
        @DisplayName("Should handle special characters in service name")
        void shouldHandleSpecialCharactersInServiceName() {
            String serviceName = "service_with.special:chars";

            ServiceHealth health = new ServiceHealth(serviceName, TENANT_ID);

            assertThat(health.getServiceName()).isEqualTo(serviceName);
        }

        @Test
        @DisplayName("Should handle unicode in service name")
        void shouldHandleUnicodeInServiceName() {
            String serviceName = "サービス-健康";

            ServiceHealth health = new ServiceHealth(serviceName, TENANT_ID);

            assertThat(health.getServiceName()).isEqualTo(serviceName);
        }

        @Test
        @DisplayName("Should handle empty metrics map")
        void shouldHandleEmptyMetricsMap() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            health.updateMetrics(Map.of());

            assertThat(health.getMetrics()).isEmpty();
        }

        @Test
        @DisplayName("Should handle complex metric values")
        void shouldHandleComplexMetricValues() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);
            Map<String, Object> complexMetrics = Map.of(
                    "cpu", 50.5,
                    "memory", 1024L,
                    "status", "healthy",
                    "errors", Arrays.asList("error1", "error2"),
                    "nested", Map.of("key", "value")
            );

            health.updateMetrics(complexMetrics);

            assertThat(health.getMetrics()).hasSize(5);
        }
    }
}
