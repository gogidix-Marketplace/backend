package com.gogidix.analytics.metrics.domain.port.in;

import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IngestMetricCommandTest {

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> tags = Map.of("env", "prod");
        Map<String, Object> dims = Map.of("region", "us-east");

        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("cpu.usage")
                .metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5"))
                .unit("percent")
                .timestamp(now)
                .sourceService("order-service")
                .tags(tags)
                .dimensions(dims)
                .aggregationLevel("raw")
                .build();

        assertEquals("cpu.usage", cmd.getMetricName());
        assertEquals(MetricDataPoint.MetricType.GAUGE, cmd.getMetricType());
        assertEquals(new BigDecimal("85.5"), cmd.getMetricValue());
        assertEquals("percent", cmd.getUnit());
        assertEquals(now, cmd.getTimestamp());
        assertEquals("order-service", cmd.getSourceService());
        assertEquals(tags, cmd.getTags());
        assertEquals(dims, cmd.getDimensions());
        assertEquals("raw", cmd.getAggregationLevel());
    }

    @Test
    void testNoArgsConstructor() {
        IngestMetricCommand cmd = new IngestMetricCommand();
        assertNotNull(cmd);
        assertNull(cmd.getMetricName());
    }

    @Test
    void testAllArgsConstructor() {
        Map<String, String> tags = Map.of("k", "v");
        Map<String, Object> dims = Map.of("d", 1);
        LocalDateTime now = LocalDateTime.now();
        IngestMetricCommand cmd = new IngestMetricCommand(
                "m", MetricDataPoint.MetricType.COUNTER, BigDecimal.ONE, "u", now,
                "svc", tags, dims, "raw");

        assertEquals("m", cmd.getMetricName());
        assertEquals(MetricDataPoint.MetricType.COUNTER, cmd.getMetricType());
        assertEquals(BigDecimal.ONE, cmd.getMetricValue());
        assertEquals("u", cmd.getUnit());
        assertEquals(now, cmd.getTimestamp());
        assertEquals("svc", cmd.getSourceService());
        assertEquals(tags, cmd.getTags());
        assertEquals(dims, cmd.getDimensions());
        assertEquals("raw", cmd.getAggregationLevel());
    }

    @Test
    void testSettersAndGetters() {
        IngestMetricCommand cmd = new IngestMetricCommand();
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> tags = Map.of("a", "b");
        Map<String, Object> dims = Map.of("c", "d");

        cmd.setMetricName("mem");
        cmd.setMetricType(MetricDataPoint.MetricType.HISTOGRAM);
        cmd.setMetricValue(BigDecimal.TEN);
        cmd.setUnit("bytes");
        cmd.setTimestamp(now);
        cmd.setSourceService("gateway");
        cmd.setTags(tags);
        cmd.setDimensions(dims);
        cmd.setAggregationLevel("hourly");

        assertEquals("mem", cmd.getMetricName());
        assertEquals(MetricDataPoint.MetricType.HISTOGRAM, cmd.getMetricType());
        assertEquals(BigDecimal.TEN, cmd.getMetricValue());
        assertEquals("bytes", cmd.getUnit());
        assertEquals(now, cmd.getTimestamp());
        assertEquals("gateway", cmd.getSourceService());
        assertEquals(tags, cmd.getTags());
        assertEquals(dims, cmd.getDimensions());
        assertEquals("hourly", cmd.getAggregationLevel());
    }

    @Test
    void testEqualsAndHashCode() {
        IngestMetricCommand c1 = IngestMetricCommand.builder().metricName("a").build();
        IngestMetricCommand c2 = IngestMetricCommand.builder().metricName("a").build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        IngestMetricCommand cmd = IngestMetricCommand.builder().metricName("cpu").build();
        assertNotNull(cmd.toString());
        assertTrue(cmd.toString().contains("cpu"));
    }

    @Test
    void testEquals_sameReference() {
        IngestMetricCommand cmd = new IngestMetricCommand();
        assertEquals(cmd, cmd);
    }

    @Test
    void testEquals_null_returnsFalse() {
        IngestMetricCommand cmd = new IngestMetricCommand();
        assertNotEquals(null, cmd);
    }

    @Test
    void testEquals_differentType_returnsFalse() {
        IngestMetricCommand cmd = new IngestMetricCommand();
        assertNotEquals("not a command", cmd);
    }

    @Test
    void testEquals_fullyPopulatedEqual() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> tags = Map.of("env", "prod");
        Map<String, Object> dims = Map.of("region", "us-east");
        IngestMetricCommand c1 = IngestMetricCommand.builder()
                .metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags(tags).dimensions(dims).aggregationLevel("raw")
                .build();
        IngestMetricCommand c2 = IngestMetricCommand.builder()
                .metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags(tags).dimensions(dims).aggregationLevel("raw")
                .build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEquals_differInMetricName() {
        LocalDateTime now = LocalDateTime.now();
        IngestMetricCommand c1 = IngestMetricCommand.builder()
                .metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .aggregationLevel("raw").build();
        IngestMetricCommand c2 = IngestMetricCommand.builder()
                .metricName("mem.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .aggregationLevel("raw").build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testEquals_differInMetricType() {
        IngestMetricCommand c1 = IngestMetricCommand.builder()
                .metricName("cpu").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.ONE).sourceService("svc").build();
        IngestMetricCommand c2 = IngestMetricCommand.builder()
                .metricName("cpu").metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE).sourceService("svc").build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testEquals_differInMetricValue() {
        IngestMetricCommand c1 = IngestMetricCommand.builder()
                .metricName("cpu").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.ONE).sourceService("svc").build();
        IngestMetricCommand c2 = IngestMetricCommand.builder()
                .metricName("cpu").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.TEN).sourceService("svc").build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testEquals_differInSourceService() {
        IngestMetricCommand c1 = IngestMetricCommand.builder()
                .metricName("cpu").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.ONE).sourceService("svc-a").build();
        IngestMetricCommand c2 = IngestMetricCommand.builder()
                .metricName("cpu").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.ONE).sourceService("svc-b").build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testHashCode_fullyPopulated() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> tags = Map.of("env", "prod");
        Map<String, Object> dims = Map.of("region", "us-east");
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags(tags).dimensions(dims).aggregationLevel("raw")
                .build();
        int h1 = cmd.hashCode();
        cmd.setMetricName("mem.usage");
        int h2 = cmd.hashCode();
        assertNotEquals(h1, h2);
    }

    @Test
    void testEquals_oneNullOneSet_differ() {
        IngestMetricCommand c1 = IngestMetricCommand.builder()
                .metricName("cpu").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.ONE).sourceService("svc").build();
        IngestMetricCommand c2 = IngestMetricCommand.builder()
                .metricName("cpu").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.ONE).sourceService("svc")
                .unit("percent").build();
        assertNotEquals(c1, c2);
    }
}
