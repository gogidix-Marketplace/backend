package com.gogidix.dashboard.performance.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceMetricTest {

    private PerformanceMetric createMetric(double value, MetricType type) {
        return new PerformanceMetric.Builder()
                .withId(MetricId.generate())
                .withMetricName("test-metric")
                .withMetricType(type)
                .withValue(PerformanceValue.of(value, type.getUnit()))
                .withThresholds(new PerformanceThresholds(0, 100, 80, 95))
                .withTimestamp(LocalDateTime.now())
                .withSource("test-domain", "test-service")
                .build();
    }

    private PerformanceMetric createMetricWithThresholds(double value, MetricType type,
            double min, double max, double warning, double critical) {
        return new PerformanceMetric.Builder()
                .withId(MetricId.generate())
                .withMetricName("test-metric")
                .withMetricType(type)
                .withValue(PerformanceValue.of(value, type.getUnit()))
                .withThresholds(new PerformanceThresholds(min, max, warning, critical))
                .withTimestamp(LocalDateTime.now())
                .withSource("test-domain", "test-service")
                .build();
    }

    @Test
    void builder_requiresAllFields() {
        assertThrows(NullPointerException.class, () -> new PerformanceMetric.Builder().build());
    }

    @Test
    void builder_createsMetricSuccessfully() {
        PerformanceMetric metric = createMetric(50.0, MetricType.CPU_USAGE);

        assertNotNull(metric.getId());
        assertEquals("test-metric", metric.getMetricName());
        assertEquals(MetricType.CPU_USAGE, metric.getMetricType());
        assertEquals(50.0, metric.getValue().getValue());
        assertEquals("test-domain", metric.getSourceDomain());
        assertEquals("test-service", metric.getSourceService());
        assertNotNull(metric.getTimestamp());
    }

    @Test
    void builder_withTags() {
        PerformanceMetric metric = new PerformanceMetric.Builder()
                .withId(MetricId.generate())
                .withMetricName("test")
                .withMetricType(MetricType.CPU_USAGE)
                .withValue(PerformanceValue.of(50.0))
                .withThresholds(new PerformanceThresholds(0, 100, 80, 95))
                .withTimestamp(LocalDateTime.now())
                .withSource("d", "s")
                .withTags(Map.of("env", "prod"))
                .build();

        assertEquals(Map.of("env", "prod"), metric.getTags());
    }

    @Test
    void builder_withAnomalies() {
        PerformanceAnomaly anomaly = new PerformanceAnomaly("test",
                AnomalySeverity.P0, LocalDateTime.now());
        PerformanceMetric metric = new PerformanceMetric.Builder()
                .withId(MetricId.generate())
                .withMetricName("test")
                .withMetricType(MetricType.CPU_USAGE)
                .withValue(PerformanceValue.of(50.0))
                .withThresholds(new PerformanceThresholds(0, 100, 80, 95))
                .withTimestamp(LocalDateTime.now())
                .withSource("d", "s")
                .withAnomalies(List.of(anomaly))
                .build();

        assertEquals(1, metric.getAnomalies().size());
    }

    @Test
    void calculateStatus_critical() {
        PerformanceMetric metric = createMetricWithThresholds(95.0, MetricType.CPU_USAGE, 0, 100, 80, 95);
        assertEquals(PerformanceStatus.CRITICAL, metric.getStatus());
    }

    @Test
    void calculateStatus_warning() {
        PerformanceMetric metric = createMetricWithThresholds(85.0, MetricType.CPU_USAGE, 0, 100, 80, 95);
        assertEquals(PerformanceStatus.WARNING, metric.getStatus());
    }

    @Test
    void calculateStatus_optimal() {
        PerformanceMetric metric = createMetricWithThresholds(50.0, MetricType.CPU_USAGE, 0, 100, 80, 95);
        assertEquals(PerformanceStatus.OPTIMAL, metric.getStatus());
    }

    @Test
    void calculateStatus_normal() {
        PerformanceMetric metric = createMetricWithThresholds(75.0, MetricType.CPU_USAGE, 10, 60, 80, 95);
        assertEquals(PerformanceStatus.NORMAL, metric.getStatus());
    }

    @Test
    void requiresImmediateAttention_criticalStatus() {
        PerformanceMetric metric = createMetricWithThresholds(95.0, MetricType.CPU_USAGE, 0, 100, 80, 95);
        assertTrue(metric.requiresImmediateAttention());
    }

    @Test
    void requiresImmediateAttention_notRequired() {
        PerformanceMetric metric = createMetricWithThresholds(50.0, MetricType.CPU_USAGE, 0, 100, 80, 95);
        assertFalse(metric.requiresImmediateAttention());
    }

    @Test
    void hasP0Anomalies_true() {
        PerformanceAnomaly p0Anomaly = new PerformanceAnomaly("critical", AnomalySeverity.P0, LocalDateTime.now());
        PerformanceMetric metric = new PerformanceMetric.Builder()
                .withId(MetricId.generate())
                .withMetricName("test").withMetricType(MetricType.CPU_USAGE)
                .withValue(PerformanceValue.of(50.0))
                .withThresholds(new PerformanceThresholds(0, 100, 80, 95))
                .withTimestamp(LocalDateTime.now()).withSource("d", "s")
                .withAnomalies(List.of(p0Anomaly)).build();
        assertTrue(metric.hasP0Anomalies());
    }

    @Test
    void hasP0Anomalies_false() {
        PerformanceMetric metric = createMetric(50.0, MetricType.CPU_USAGE);
        assertFalse(metric.hasP0Anomalies());
    }

    @Test
    void isServiceDegraded_errorRate() {
        PerformanceMetric metric = createMetric(10.0, MetricType.ERROR_RATE);
        assertTrue(metric.isServiceDegraded());
    }

    @Test
    void isServiceDegraded_responseTime() {
        PerformanceMetric metric = createMetric(5000.0, MetricType.RESPONSE_TIME);
        assertTrue(metric.isServiceDegraded());
    }

    @Test
    void isServiceDegraded_availability() {
        PerformanceMetric metric = createMetric(95.0, MetricType.AVAILABILITY);
        assertTrue(metric.isServiceDegraded());
    }

    @Test
    void isServiceDegraded_normalValues() {
        PerformanceMetric metric = createMetric(2.0, MetricType.ERROR_RATE);
        assertFalse(metric.isServiceDegraded());
    }

    @Test
    void isServiceDegraded_otherMetricType() {
        PerformanceMetric metric = createMetric(50.0, MetricType.CPU_USAGE);
        assertFalse(metric.isServiceDegraded());
    }

    @Test
    void calculateSLACompliance_availability() {
        PerformanceMetric metric = createMetric(99.9, MetricType.AVAILABILITY);
        double compliance = metric.calculateSLACompliance();
        assertEquals(100.0, compliance, 0.1);
    }

    @Test
    void calculateSLACompliance_errorRate() {
        PerformanceMetric metric = createMetric(0.1, MetricType.ERROR_RATE);
        double compliance = metric.calculateSLACompliance();
        assertTrue(compliance >= 99.0);
    }

    @Test
    void calculateSLACompliance_otherType() {
        PerformanceMetric metric = createMetric(50.0, MetricType.CPU_USAGE);
        assertEquals(100.0, metric.calculateSLACompliance());
    }

    @Test
    void analyzeTrend_emptyHistory_returnsStable() {
        PerformanceMetric metric = createMetric(50.0, MetricType.CPU_USAGE);
        assertEquals(PerformanceTrend.STABLE, metric.analyzeTrend(Collections.emptyList()));
    }

    @Test
    void analyzeTrend_improving_higherBetter() {
        PerformanceMetric current = createMetric(15000.0, MetricType.THROUGHPUT);
        List<PerformanceMetric> history = List.of(
                createMetric(10000.0, MetricType.THROUGHPUT));
        assertEquals(PerformanceTrend.IMPROVING, current.analyzeTrend(history));
    }

    @Test
    void analyzeTrend_degrading_higherBetter() {
        PerformanceMetric current = createMetric(5000.0, MetricType.THROUGHPUT);
        List<PerformanceMetric> history = List.of(
                createMetric(10000.0, MetricType.THROUGHPUT));
        assertEquals(PerformanceTrend.DEGRADING, current.analyzeTrend(history));
    }

    @Test
    void analyzeTrend_stable() {
        PerformanceMetric current = createMetric(101.0, MetricType.CPU_USAGE);
        List<PerformanceMetric> history = List.of(
                createMetric(100.0, MetricType.CPU_USAGE));
        assertEquals(PerformanceTrend.STABLE, current.analyzeTrend(history));
    }

    @Test
    void calculateUtilizationPercentage_resourceMetric() {
        PerformanceMetric cpu = createMetric(75.0, MetricType.CPU_USAGE);
        assertEquals(75.0, cpu.calculateUtilizationPercentage());

        PerformanceMetric mem = createMetric(60.0, MetricType.MEMORY_USAGE);
        assertEquals(60.0, mem.calculateUtilizationPercentage());

        PerformanceMetric disk = createMetric(45.0, MetricType.DISK_USAGE);
        assertEquals(45.0, disk.calculateUtilizationPercentage());
    }

    @Test
    void calculateUtilizationPercentage_nonResourceMetric() {
        PerformanceMetric metric = createMetric(50.0, MetricType.THROUGHPUT);
        assertEquals(0.0, metric.calculateUtilizationPercentage());
    }

    @Test
    void shouldTriggerAutoScaling_cpu() {
        PerformanceMetric metric = createMetric(85.0, MetricType.CPU_USAGE);
        assertTrue(metric.shouldTriggerAutoScaling());
    }

    @Test
    void shouldTriggerAutoScaling_memory() {
        PerformanceMetric metric = createMetric(90.0, MetricType.MEMORY_USAGE);
        assertTrue(metric.shouldTriggerAutoScaling());
    }

    @Test
    void shouldNotTriggerAutoScaling_normalValues() {
        PerformanceMetric metric = createMetric(50.0, MetricType.CPU_USAGE);
        assertFalse(metric.shouldTriggerAutoScaling());
    }

    @Test
    void getAlertPriority_critical() {
        PerformanceMetric metric = createMetricWithThresholds(95.0, MetricType.CPU_USAGE, 0, 100, 80, 95);
        assertEquals(AlertPriority.P1, metric.getAlertPriority());
    }

    @Test
    void getAlertPriority_warning_criticalMetric() {
        PerformanceMetric metric = createMetricWithThresholds(85.0, MetricType.RESPONSE_TIME, 0, 100, 80, 95);
        assertEquals(AlertPriority.P2, metric.getAlertPriority());
    }

    @Test
    void getAlertPriority_warning_nonCriticalMetric() {
        PerformanceMetric metric = createMetricWithThresholds(85.0, MetricType.CPU_USAGE, 0, 100, 80, 95);
        assertEquals(AlertPriority.P3, metric.getAlertPriority());
    }

    @Test
    void getAlertPriority_normal() {
        PerformanceMetric metric = createMetricWithThresholds(50.0, MetricType.CPU_USAGE, 0, 100, 80, 95);
        assertEquals(AlertPriority.P4, metric.getAlertPriority());
    }

    @Test
    void estimateTimeToThresholdBreach_zeroRate() {
        PerformanceMetric metric = createMetric(50.0, MetricType.CPU_USAGE);
        Duration result = metric.estimateTimeToThresholdBreach(0);
        assertEquals(Duration.ofDays(365), result);
    }

    @Test
    void estimateTimeToThresholdBreach_positiveRate() {
        PerformanceMetric metric = createMetric(50.0, MetricType.CPU_USAGE);
        Duration result = metric.estimateTimeToThresholdBreach(5);
        assertNotNull(result);
        assertTrue(result.toMinutes() > 0);
    }

    @Test
    void getAnomalies_returnsDefensiveCopy() {
        PerformanceMetric metric = createMetric(50.0, MetricType.CPU_USAGE);
        List<PerformanceAnomaly> anomalies = metric.getAnomalies();
        anomalies.add(new PerformanceAnomaly("x", AnomalySeverity.P1, LocalDateTime.now()));
        assertEquals(0, metric.getAnomalies().size());
    }
}
