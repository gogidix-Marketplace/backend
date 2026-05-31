package com.gogidix.monitoring.performance;

import com.gogidix.monitoring.performance.domain.model.*;
import com.gogidix.monitoring.performance.interfaces.rest.HealthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Performance Monitoring Service Tests")
class PerformanceMonitoringTest {

    @Nested
    @DisplayName("PerformanceAlert Tests")
    class PerformanceAlertTest {

        @Test
        @DisplayName("Should create alert with factory")
        void shouldCreateWithFactory() {
            PerformanceAlert alert = PerformanceAlert.create(
                    "t1", "svc1", "High CPU",
                    ">", 80.0, 95.0, "CPU usage critical");
            assertEquals("t1", alert.getTenantId());
            assertEquals("svc1", alert.getServiceId());
            assertEquals("High CPU", alert.getAlertName());
            assertEquals(">", alert.getCondition());
            assertEquals(80.0, alert.getThreshold());
            assertEquals(95.0, alert.getActualValue());
            assertEquals(AlertStatus.OPEN, alert.getStatus());
            assertNotNull(alert.getTriggeredAt());
        }

        @Test
        @DisplayName("Should resolve alert")
        void shouldResolveAlert() {
            PerformanceAlert alert = PerformanceAlert.create(
                    "t1", "svc1", "Alert", ">", 80.0, 90.0, "msg");
            alert.resolve();
            assertEquals(AlertStatus.RESOLVED, alert.getStatus());
            assertNotNull(alert.getResolvedAt());
        }

        @Test
        @DisplayName("Should build with builder")
        void shouldBuildWithBuilder() {
            PerformanceAlert alert = PerformanceAlert.builder()
                    .id("a1").tenantId("t1").alertName("Test")
                    .status(AlertStatus.ACKNOWLEDGED).build();
            assertEquals("a1", alert.getId());
            assertEquals(AlertStatus.ACKNOWLEDGED, alert.getStatus());
        }
    }

    @Nested
    @DisplayName("MetricData Tests")
    class MetricDataTest {

        @Test
        @DisplayName("Should create metric data with factory")
        void shouldCreateWithFactory() {
            MetricData data = MetricData.create(
                    "t1", "svc1", "cpu_usage",
                    MetricType.GAUGE, 75.5, Map.of("host", "server1"));
            assertEquals("t1", data.getTenantId());
            assertEquals("cpu_usage", data.getMetricName());
            assertEquals(MetricType.GAUGE, data.getMetricType());
            assertEquals(75.5, data.getValue());
            assertNotNull(data.getTimestamp());
        }

        @Test
        @DisplayName("Should build with builder")
        void shouldBuildWithBuilder() {
            MetricData data = MetricData.builder()
                    .metricName("memory").value(2048.0).build();
            assertEquals("memory", data.getMetricName());
            assertEquals(2048.0, data.getValue());
        }
    }

    @Nested
    @DisplayName("AlertStatus Tests")
    class AlertStatusTest {

        @Test
        @DisplayName("Should have statuses with codes")
        void shouldHaveStatuses() {
            assertEquals(4, AlertStatus.values().length);
            assertEquals("open", AlertStatus.OPEN.getCode());
            assertEquals("acknowledged", AlertStatus.ACKNOWLEDGED.getCode());
            assertEquals("resolved", AlertStatus.RESOLVED.getCode());
            assertEquals("silenced", AlertStatus.SILENCED.getCode());
            assertNotNull(AlertStatus.OPEN.getDescription());
        }
    }

    @Nested
    @DisplayName("MetricType Tests")
    class MetricTypeTest {

        @Test
        @DisplayName("Should have types with codes")
        void shouldHaveTypes() {
            assertEquals(5, MetricType.values().length);
            assertEquals("counter", MetricType.COUNTER.getCode());
            assertEquals("gauge", MetricType.GAUGE.getCode());
            assertEquals("histogram", MetricType.HISTOGRAM.getCode());
            assertEquals("summary", MetricType.SUMMARY.getCode());
            assertEquals("timing", MetricType.TIMING.getCode());
            assertNotNull(MetricType.COUNTER.getDescription());
        }
    }

    @Nested
    @DisplayName("HealthController Tests")
    class HealthControllerTest {

        private final HealthController controller = new HealthController();

        @Test
        @DisplayName("Should return health")
        void shouldReturnHealth() {
            ResponseEntity<Map<String, String>> resp = controller.health();
            assertEquals("UP", resp.getBody().get("status"));
        }

        @Test
        @DisplayName("Should return readiness")
        void shouldReturnReadiness() {
            ResponseEntity<Map<String, String>> resp = controller.readiness();
            assertEquals("READY", resp.getBody().get("status"));
        }
    }
}
