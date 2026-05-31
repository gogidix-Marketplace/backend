package com.gogidix.analytics.metrics.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MetricDataPointTest {

    private MetricDataPoint.MetricType[] metricTypes;

    @BeforeEach
    void setUp() {
        metricTypes = MetricDataPoint.MetricType.values();
    }

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint point = MetricDataPoint.builder()
                .id(1L)
                .metricName("cpu.usage")
                .metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5"))
                .unit("percent")
                .timestamp(now)
                .sourceService("order-service")
                .tags("{\"env\":\"prod\"}")
                .aggregationLevel("raw")
                .tenantId("tenant-1")
                .dimensions("{\"region\":\"us-east\"}")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, point.getId());
        assertEquals("cpu.usage", point.getMetricName());
        assertEquals(MetricDataPoint.MetricType.GAUGE, point.getMetricType());
        assertEquals(new BigDecimal("85.5"), point.getMetricValue());
        assertEquals("percent", point.getUnit());
        assertEquals(now, point.getTimestamp());
        assertEquals("order-service", point.getSourceService());
        assertEquals("{\"env\":\"prod\"}", point.getTags());
        assertEquals("raw", point.getAggregationLevel());
        assertEquals("tenant-1", point.getTenantId());
        assertEquals("{\"region\":\"us-east\"}", point.getDimensions());
        assertEquals(now, point.getCreatedAt());
        assertEquals(now, point.getUpdatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        MetricDataPoint point = new MetricDataPoint();
        assertNotNull(point);
        assertNull(point.getId());
        assertNull(point.getMetricName());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint point = new MetricDataPoint(
                1L, "cpu.usage", MetricDataPoint.MetricType.COUNTER,
                BigDecimal.TEN, "ms", now, "svc", "tags",
                "raw", "t1", "dim", now, now);

        assertEquals(1L, point.getId());
        assertEquals("cpu.usage", point.getMetricName());
        assertEquals(MetricDataPoint.MetricType.COUNTER, point.getMetricType());
        assertEquals(BigDecimal.TEN, point.getMetricValue());
        assertEquals("ms", point.getUnit());
        assertEquals("svc", point.getSourceService());
        assertEquals("tags", point.getTags());
        assertEquals("raw", point.getAggregationLevel());
        assertEquals("t1", point.getTenantId());
        assertEquals("dim", point.getDimensions());
    }

    @Test
    void testSettersAndGetters() {
        MetricDataPoint point = new MetricDataPoint();
        LocalDateTime now = LocalDateTime.now();

        point.setId(99L);
        point.setMetricName("memory");
        point.setMetricType(MetricDataPoint.MetricType.HISTOGRAM);
        point.setMetricValue(BigDecimal.ONE);
        point.setUnit("bytes");
        point.setTimestamp(now);
        point.setSourceService("gateway");
        point.setTags("tags-json");
        point.setAggregationLevel("hourly");
        point.setTenantId("tenant-2");
        point.setDimensions("dims-json");
        point.setCreatedAt(now);
        point.setUpdatedAt(now);

        assertEquals(99L, point.getId());
        assertEquals("memory", point.getMetricName());
        assertEquals(MetricDataPoint.MetricType.HISTOGRAM, point.getMetricType());
        assertEquals(BigDecimal.ONE, point.getMetricValue());
        assertEquals("bytes", point.getUnit());
        assertEquals(now, point.getTimestamp());
        assertEquals("gateway", point.getSourceService());
        assertEquals("tags-json", point.getTags());
        assertEquals("hourly", point.getAggregationLevel());
        assertEquals("tenant-2", point.getTenantId());
        assertEquals("dims-json", point.getDimensions());
        assertEquals(now, point.getCreatedAt());
        assertEquals(now, point.getUpdatedAt());
    }

    @Test
    void testOnCreateSetsTimestamps() {
        MetricDataPoint point = MetricDataPoint.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .tenantId("t1")
                .timestamp(null)
                .build();

        point.onCreate();

        assertNotNull(point.getCreatedAt());
        assertNotNull(point.getUpdatedAt());
        assertNotNull(point.getTimestamp());
    }

    @Test
    void testOnCreatePreservesExistingTimestamp() {
        LocalDateTime custom = LocalDateTime.of(2025, 1, 1, 0, 0);
        MetricDataPoint point = MetricDataPoint.builder()
                .metricName("test")
                .timestamp(custom)
                .build();

        point.onCreate();

        assertEquals(custom, point.getTimestamp());
        assertNotNull(point.getCreatedAt());
    }

    @Test
    void testOnUpdate() {
        MetricDataPoint point = new MetricDataPoint();
        point.setUpdatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
        point.onUpdate();
        assertNotNull(point.getUpdatedAt());
    }

    @Test
    void testMetricTypeEnumValues() {
        assertEquals(5, MetricDataPoint.MetricType.values().length);
        assertEquals(MetricDataPoint.MetricType.COUNTER, MetricDataPoint.MetricType.valueOf("COUNTER"));
        assertEquals(MetricDataPoint.MetricType.GAUGE, MetricDataPoint.MetricType.valueOf("GAUGE"));
        assertEquals(MetricDataPoint.MetricType.HISTOGRAM, MetricDataPoint.MetricType.valueOf("HISTOGRAM"));
        assertEquals(MetricDataPoint.MetricType.SUMMARY, MetricDataPoint.MetricType.valueOf("SUMMARY"));
        assertEquals(MetricDataPoint.MetricType.TIMER, MetricDataPoint.MetricType.valueOf("TIMER"));
    }

    @Test
    void testEqualsAndHashCode() {
        MetricDataPoint p1 = MetricDataPoint.builder().id(1L).metricName("a").build();
        MetricDataPoint p2 = MetricDataPoint.builder().id(1L).metricName("a").build();
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testToString() {
        MetricDataPoint point = MetricDataPoint.builder().metricName("cpu").build();
        String str = point.toString();
        assertNotNull(str);
        assertTrue(str.contains("cpu"));
    }

    @Test
    void testEquals_sameReference() {
        MetricDataPoint point = new MetricDataPoint();
        assertEquals(point, point);
    }

    @Test
    void testEquals_null_returnsFalse() {
        MetricDataPoint point = new MetricDataPoint();
        assertNotEquals(null, point);
    }

    @Test
    void testEquals_differentType_returnsFalse() {
        MetricDataPoint point = new MetricDataPoint();
        assertNotEquals("not a datapoint", point);
    }

    @Test
    void testEquals_fullyPopulatedEqual() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint p1 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        MetricDataPoint p2 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testEquals_fullyPopulatedDifferInId() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint p1 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        MetricDataPoint p2 = MetricDataPoint.builder()
                .id(2L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(p1, p2);
    }

    @Test
    void testEquals_differInMetricName() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint p1 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        MetricDataPoint p2 = MetricDataPoint.builder()
                .id(1L).metricName("mem.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(p1, p2);
    }

    @Test
    void testEquals_differInMetricType() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint p1 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        MetricDataPoint p2 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(p1, p2);
    }

    @Test
    void testEquals_differInMetricValue() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint p1 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        MetricDataPoint p2 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("90.0")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(p1, p2);
    }

    @Test
    void testEquals_differInTenantId() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint p1 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        MetricDataPoint p2 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t2").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(p1, p2);
    }

    @Test
    void testHashCode_fullyPopulated() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint point = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("order-service")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        int h1 = point.hashCode();
        point.setMetricName("mem.usage");
        int h2 = point.hashCode();
        assertNotEquals(h1, h2);
    }

    @Test
    void testEquals_oneNullOneSet_differ() {
        MetricDataPoint p1 = MetricDataPoint.builder().id(1L).metricName("cpu").build();
        MetricDataPoint p2 = MetricDataPoint.builder().id(1L).metricName("cpu")
                .unit("percent").build();
        assertNotEquals(p1, p2);
    }

    @Test
    void testEquals_differInSourceService() {
        LocalDateTime now = LocalDateTime.now();
        MetricDataPoint p1 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("svc-a")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        MetricDataPoint p2 = MetricDataPoint.builder()
                .id(1L).metricName("cpu.usage").metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5")).unit("percent")
                .timestamp(now).sourceService("svc-b")
                .tags("{\"env\":\"prod\"}").aggregationLevel("raw")
                .tenantId("t1").dimensions("{\"region\":\"us-east\"}")
                .createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(p1, p2);
    }
}
