package com.gogidix.centralizeddashboard.metrics.model;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CentralizedPerformanceMetricTest {

    private com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric createMetric(
            String serviceName, String instanceId, String metricName,
            PerformanceMetric.MetricType type, double value, String env) {
        return new PerformanceMetric(serviceName, instanceId, metricName, type, value, env);
    }

    @Test
    void constructor_createsMetric() {
        PerformanceMetric pm = createMetric("order-svc", "inst-1", "cpu_usage",
                PerformanceMetric.MetricType.RESOURCE_METRIC, 75.0, "prod");

        assertNotNull(pm.getMetricId());
        assertEquals("order-svc", pm.getServiceName());
        assertEquals("inst-1", pm.getInstanceId());
        assertEquals("cpu_usage", pm.getMetricName());
        assertEquals(75.0, pm.getValue());
        assertEquals("prod", pm.getEnvironment());
        assertNotNull(pm.getTimestamp());
    }

    @Test
    void constructor_nullServiceName_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                createMetric(null, "i1", "m1",
                        PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod"));
    }

    @Test
    void constructor_emptyServiceName_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                createMetric("", "i1", "m1",
                        PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod"));
    }

    @Test
    void constructor_nullInstanceId_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                createMetric("svc", null, "m1",
                        PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod"));
    }

    @Test
    void constructor_nullMetricName_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                createMetric("svc", "i1", null,
                        PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod"));
    }

    @Test
    void constructor_nullValue_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                createMetric("svc", "i1", "m1",
                        PerformanceMetric.MetricType.CUSTOM_METRIC, -1.0, "prod"));
    }

    @Test
    void constructor_negativeValue_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                createMetric("svc", "i1", "m1",
                        PerformanceMetric.MetricType.CUSTOM_METRIC, -1.0, "prod"));
    }

    @Test
    void constructor_nanValue_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                createMetric("svc", "i1", "m1",
                        PerformanceMetric.MetricType.CUSTOM_METRIC, Double.NaN, "prod"));
    }

    @Test
    void constructor_nullMetricType_throws() {
        assertThrows(NullPointerException.class, () ->
                new PerformanceMetric("svc", "i1", "m1", null, 50.0, "prod"));
    }

    @Test
    void constructor_nullEnvironment_throws() {
        assertThrows(NullPointerException.class, () ->
                createMetric("svc", "i1", "m1",
                        PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, null));
    }

    @Test
    void isNormal_returnsTrue() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        assertTrue(pm.isNormal());
    }

    @Test
    void isNormal_whenAnomalous_returnsFalse() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm = pm.markAnomalous(true);
        assertFalse(pm.isNormal());
    }

    @Test
    void isWarning_withWarningThreshold() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm = pm.withThresholds(30.0, 80.0);
        assertTrue(pm.isWarning());
    }

    @Test
    void isCritical_withCriticalThreshold() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm = pm.withThresholds(30.0, 40.0);
        assertTrue(pm.isCritical());
    }

    @Test
    void exceedsThreshold_true() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 100.0, "prod");
        assertTrue(pm.exceedsThreshold(50));
    }

    @Test
    void exceedsThreshold_false() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 30.0, "prod");
        assertFalse(pm.exceedsThreshold(50));
    }

    @Test
    void belowThreshold_true() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 30.0, "prod");
        assertTrue(pm.belowThreshold(50));
    }

    @Test
    void belowThreshold_false() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 100.0, "prod");
        assertFalse(pm.belowThreshold(50));
    }

    @Test
    void calculateDeviationFromBaseline_withBaseline() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 60.0, "prod");
        pm = pm.withBaseline(50.0);
        assertEquals(10.0, pm.calculateDeviationFromBaseline(), 0.01);
    }

    @Test
    void calculateDeviationFromBaseline_noBaseline() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 60.0, "prod");
        assertEquals(0.0, pm.calculateDeviationFromBaseline());
    }

    @Test
    void calculatePercentageDeviation_noBaseline() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 60.0, "prod");
        assertEquals(0.0, pm.calculatePercentageDeviationFromBaseline());
    }

    @Test
    void isLatencyMetric_true() {
        PerformanceMetric pm = createMetric("svc", "i1", "response_time",
                PerformanceMetric.MetricType.LATENCY_METRIC, 100.0, "prod");
        assertTrue(pm.isLatencyMetric());
    }

    @Test
    void isLatencyMetric_false() {
        PerformanceMetric pm = createMetric("svc", "i1", "cpu_usage",
                PerformanceMetric.MetricType.RESOURCE_METRIC, 50.0, "prod");
        assertFalse(pm.isLatencyMetric());
    }

    @Test
    void isBetterThanBaseline_latencyMetric() {
        PerformanceMetric pm = createMetric("svc", "i1", "response_time",
                PerformanceMetric.MetricType.LATENCY_METRIC, 50.0, "prod");
        pm = pm.withBaseline(100.0);
        assertTrue(pm.isBetterThanBaseline());
    }

    @Test
    void isWorseThanBaseline_latencyMetric() {
        PerformanceMetric pm = createMetric("svc", "i1", "response_time",
                PerformanceMetric.MetricType.LATENCY_METRIC, 150.0, "prod");
        pm = pm.withBaseline(100.0);
        assertTrue(pm.isWorseThanBaseline());
    }

    @Test
    void isBetterThanBaseline_throughputMetric() {
        PerformanceMetric pm = createMetric("svc", "i1", "throughput",
                PerformanceMetric.MetricType.THROUGHPUT_METRIC, 200.0, "prod");
        pm = pm.withBaseline(100.0);
        assertTrue(pm.isBetterThanBaseline());
    }

    @Test
    void isWorseThanBaseline_noBaseline() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        assertFalse(pm.isWorseThanBaseline());
    }

    @Test
    void getPerformanceScore_normalMetric() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        assertTrue(pm.getPerformanceScore() > 0);
    }

    @Test
    void getPerformanceScore_criticalMetric() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm = pm.withThresholds(30.0, 40.0);
        assertTrue(pm.getPerformanceScore() < 50);
    }

    @Test
    void requiresAttention_critical() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm = pm.withThresholds(30.0, 40.0);
        assertTrue(pm.requiresAttention());
    }

    @Test
    void getUrgencyLevel_critical() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm = pm.withThresholds(30.0, 40.0);
        assertEquals(PerformanceMetric.UrgencyLevel.URGENT, pm.getUrgencyLevel());
    }

    @Test
    void getUrgencyLevel_anomalous() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm = pm.markAnomalous(true);
        assertEquals(PerformanceMetric.UrgencyLevel.HIGH, pm.getUrgencyLevel());
    }

    @Test
    void getUrgencyLevel_normal() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        assertEquals(PerformanceMetric.UrgencyLevel.LOW, pm.getUrgencyLevel());
    }

    @Test
    void isImproving_latencyMetric() {
        PerformanceMetric pm = createMetric("svc", "i1", "response_time",
                PerformanceMetric.MetricType.LATENCY_METRIC, 50.0, "prod");
        pm = pm.withTrendSlope(-1.0);
        assertTrue(pm.isImproving());
    }

    @Test
    void isDegrading_latencyMetric() {
        PerformanceMetric pm = createMetric("svc", "i1", "response_time",
                PerformanceMetric.MetricType.LATENCY_METRIC, 50.0, "prod");
        pm = pm.withTrendSlope(1.0);
        assertTrue(pm.isDegrading());
    }

    @Test
    void isStable() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm = pm.withTrendSlope(0.01);
        assertTrue(pm.isStable());
    }

    @Test
    void withTag_createsNewInstance() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        PerformanceMetric tagged = pm.withTag("env", "staging");
        assertEquals("staging", tagged.getTagValue("env"));
        assertFalse(pm.hasTag("env"));
    }

    @Test
    void hasTag_false() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        assertFalse(pm.hasTag("nonexistent"));
    }

    @Test
    void withMetadata_createsNewInstance() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        PerformanceMetric withMeta = pm.withMetadata("region", "us-east");
        assertEquals("us-east", withMeta.getMetadata().get("region"));
    }

    @Test
    void withDescription_createsNewInstance() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        PerformanceMetric desc = pm.withDescription("Test metric");
        assertEquals("Test metric", desc.getDescription());
        assertNull(pm.getDescription());
    }

    @Test
    void getCorrelationKey() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        assertEquals("svc_m1_prod", pm.getCorrelationKey());
    }

    @Test
    void matchesService() {
        PerformanceMetric pm = createMetric("payment-svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        assertTrue(pm.matchesService("payment-.*"));
        assertFalse(pm.matchesService("order-.*"));
    }

    @Test
    void compareWith_sameMetric_better() {
        PerformanceMetric pm1 = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.THROUGHPUT_METRIC, 200.0, "prod");
        PerformanceMetric pm2 = createMetric("svc", "i2", "m1",
                PerformanceMetric.MetricType.THROUGHPUT_METRIC, 100.0, "prod");
        var comp = pm1.compareWith(pm2);
        assertEquals(PerformanceMetric.ComparisonResult.BETTER, comp.getResult());
    }

    @Test
    void compareWith_sameMetric_worse() {
        PerformanceMetric pm1 = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.THROUGHPUT_METRIC, 50.0, "prod");
        PerformanceMetric pm2 = createMetric("svc", "i2", "m1",
                PerformanceMetric.MetricType.THROUGHPUT_METRIC, 100.0, "prod");
        var comp = pm1.compareWith(pm2);
        assertEquals(PerformanceMetric.ComparisonResult.WORSE, comp.getResult());
    }

    @Test
    void compareWith_equal() {
        PerformanceMetric pm1 = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 100.0, "prod");
        PerformanceMetric pm2 = createMetric("svc", "i2", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 100.0, "prod");
        var comp = pm1.compareWith(pm2);
        assertEquals(PerformanceMetric.ComparisonResult.EQUAL, comp.getResult());
    }

    @Test
    void compareWith_null_returnsIncomparable() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        var comp = pm.compareWith(null);
        assertEquals(PerformanceMetric.ComparisonResult.INCOMPARABLE, comp.getResult());
    }

    @Test
    void calculateZScore() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 60.0, "prod");
        double z = pm.calculateZScore(50, 10);
        assertEquals(1.0, z, 0.01);
    }

    @Test
    void isOutlier_true() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 100.0, "prod");
        assertTrue(pm.isOutlier(50, 10, 2.0));
    }

    @Test
    void isOutlier_false() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 55.0, "prod");
        assertFalse(pm.isOutlier(50, 10, 2.0));
    }

    @Test
    void getSummary() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        var summary = pm.getSummary();
        assertNotNull(summary);
        assertEquals("svc", summary.getService());
        assertEquals("m1", summary.getMetric());
    }

    @Test
    void createSnapshot() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        var snapshot = pm.createSnapshot();
        assertNotNull(snapshot);
        assertEquals(50.0, snapshot.getValue());
    }

    @Test
    void equals_sameMetricId() {
        PerformanceMetric pm1 = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        assertEquals(pm1, pm1);
    }

    @Test
    void equals_differentMetricId() {
        PerformanceMetric pm1 = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        PerformanceMetric pm2 = createMetric("svc", "i2", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        assertNotEquals(pm1, pm2);
    }

    @Test
    void toString_containsFields() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        String str = pm.toString();
        assertTrue(str.contains("svc"));
        assertTrue(str.contains("m1"));
    }

    @Test
    void getTags_returnsDefensiveCopy() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm.getTags().put("key", "value");
        assertTrue(pm.getTags().isEmpty());
    }

    @Test
    void getMetadata_returnsDefensiveCopy() {
        PerformanceMetric pm = createMetric("svc", "i1", "m1",
                PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod");
        pm.getMetadata().put("key", "value");
        assertTrue(pm.getMetadata().isEmpty());
    }

    @Test
    void metricType_enumValues() {
        assertEquals(8, PerformanceMetric.MetricType.values().length);
        assertTrue(PerformanceMetric.MetricType.CRITICAL_METRIC.isHighPriority());
        assertFalse(PerformanceMetric.MetricType.CUSTOM_METRIC.isHighPriority());
    }

    @Test
    void performanceState_requiresAction() {
        assertTrue(PerformanceMetric.PerformanceState.WARNING.requiresAction());
        assertTrue(PerformanceMetric.PerformanceState.CRITICAL.requiresAction());
        assertFalse(PerformanceMetric.PerformanceState.NORMAL.requiresAction());
    }

    @Test
    void performanceQuality_isAcceptable() {
        assertTrue(PerformanceMetric.PerformanceQuality.EXCELLENT.isAcceptable());
        assertTrue(PerformanceMetric.PerformanceQuality.GOOD.isAcceptable());
        assertFalse(PerformanceMetric.PerformanceQuality.POOR.isAcceptable());
    }

    @Test
    void validation_nullServiceName() {
        assertThrows(IllegalArgumentException.class, () ->
                new com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric(
                        null, "inst", "metric", PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod"));
    }

    @Test
    void validation_emptyServiceName() {
        assertThrows(IllegalArgumentException.class, () ->
                new com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric(
                        "", "inst", "metric", PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod"));
    }

    @Test
    void validation_nullInstanceId() {
        assertThrows(IllegalArgumentException.class, () ->
                new com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric(
                        "svc", null, "metric", PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod"));
    }

    @Test
    void validation_nullMetricName() {
        assertThrows(IllegalArgumentException.class, () ->
                new com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric(
                        "svc", "inst", null, PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, "prod"));
    }

    @Test
    void validation_nullMetricType() {
        assertThrows(NullPointerException.class, () ->
                new com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric(
                        "svc", "inst", "metric", null, 50.0, "prod"));
    }

    @Test
    void validation_nullValue() {
        assertThrows(IllegalArgumentException.class, () ->
                new com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric(
                        "svc", "inst", "metric", PerformanceMetric.MetricType.CUSTOM_METRIC, -1.0, "prod"));
    }

    @Test
    void validation_negativeValue() {
        assertThrows(IllegalArgumentException.class, () ->
                new com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric(
                        "svc", "inst", "metric", PerformanceMetric.MetricType.CUSTOM_METRIC, -5.0, "prod"));
    }

    @Test
    void validation_nanValue() {
        assertThrows(IllegalArgumentException.class, () ->
                new com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric(
                        "svc", "inst", "metric", PerformanceMetric.MetricType.CUSTOM_METRIC, Double.NaN, "prod"));
    }

    @Test
    void validation_nullEnvironment() {
        assertThrows(NullPointerException.class, () ->
                new com.gogidix.centralizeddashboard.metrics.model.PerformanceMetric(
                        "svc", "inst", "metric", PerformanceMetric.MetricType.CUSTOM_METRIC, 50.0, null));
    }
}
