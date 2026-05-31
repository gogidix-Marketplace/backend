package com.gogidix.analytics.metrics.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MetricAggregationTest {

    @Test
    void testBuilder() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now();

        MetricAggregation agg = MetricAggregation.builder()
                .id(1L)
                .metricName("cpu.usage")
                .aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(3600)
                .windowStart(start)
                .windowEnd(end)
                .count(100L)
                .sum(new BigDecimal("8500.5"))
                .avg(new BigDecimal("85.005"))
                .min(new BigDecimal("10.0"))
                .max(new BigDecimal("99.9"))
                .p50(new BigDecimal("80.0"))
                .p95(new BigDecimal("95.0"))
                .p99(new BigDecimal("99.0"))
                .tenantId("tenant-1")
                .dimensions("{\"region\":\"us-east\"}")
                .createdAt(end)
                .updatedAt(end)
                .build();

        assertEquals(1L, agg.getId());
        assertEquals("cpu.usage", agg.getMetricName());
        assertEquals(MetricAggregation.AggregationType.AVG, agg.getAggregationType());
        assertEquals(3600, agg.getWindowSizeSeconds());
        assertEquals(start, agg.getWindowStart());
        assertEquals(end, agg.getWindowEnd());
        assertEquals(100L, agg.getCount());
        assertEquals(new BigDecimal("8500.5"), agg.getSum());
        assertEquals(new BigDecimal("85.005"), agg.getAvg());
        assertEquals(new BigDecimal("10.0"), agg.getMin());
        assertEquals(new BigDecimal("99.9"), agg.getMax());
        assertEquals(new BigDecimal("80.0"), agg.getP50());
        assertEquals(new BigDecimal("95.0"), agg.getP95());
        assertEquals(new BigDecimal("99.0"), agg.getP99());
        assertEquals("tenant-1", agg.getTenantId());
        assertEquals("{\"region\":\"us-east\"}", agg.getDimensions());
    }

    @Test
    void testNoArgsConstructor() {
        MetricAggregation agg = new MetricAggregation();
        assertNotNull(agg);
        assertNull(agg.getId());
        assertNull(agg.getMetricName());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        MetricAggregation agg = new MetricAggregation(
                1L, "mem", MetricAggregation.AggregationType.SUM, 60, now, now,
                10L, BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ONE,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, "t1", "dim", now, now);

        assertEquals(1L, agg.getId());
        assertEquals("mem", agg.getMetricName());
        assertEquals(MetricAggregation.AggregationType.SUM, agg.getAggregationType());
    }

    @Test
    void testSettersAndGetters() {
        MetricAggregation agg = new MetricAggregation();
        LocalDateTime now = LocalDateTime.now();

        agg.setId(2L);
        agg.setMetricName("disk.io");
        agg.setAggregationType(MetricAggregation.AggregationType.MAX);
        agg.setWindowSizeSeconds(120);
        agg.setWindowStart(now);
        agg.setWindowEnd(now);
        agg.setCount(50L);
        agg.setSum(new BigDecimal("500.0"));
        agg.setAvg(new BigDecimal("10.0"));
        agg.setMin(new BigDecimal("1.0"));
        agg.setMax(new BigDecimal("50.0"));
        agg.setP50(new BigDecimal("25.0"));
        agg.setP95(new BigDecimal("45.0"));
        agg.setP99(new BigDecimal("49.0"));
        agg.setTenantId("tenant-2");
        agg.setDimensions("{}");
        agg.setCreatedAt(now);
        agg.setUpdatedAt(now);

        assertEquals(2L, agg.getId());
        assertEquals("disk.io", agg.getMetricName());
        assertEquals(MetricAggregation.AggregationType.MAX, agg.getAggregationType());
        assertEquals(120, agg.getWindowSizeSeconds());
        assertEquals(now, agg.getWindowStart());
        assertEquals(now, agg.getWindowEnd());
        assertEquals(50L, agg.getCount());
        assertEquals(new BigDecimal("500.0"), agg.getSum());
        assertEquals(new BigDecimal("10.0"), agg.getAvg());
        assertEquals(new BigDecimal("1.0"), agg.getMin());
        assertEquals(new BigDecimal("50.0"), agg.getMax());
        assertEquals(new BigDecimal("25.0"), agg.getP50());
        assertEquals(new BigDecimal("45.0"), agg.getP95());
        assertEquals(new BigDecimal("49.0"), agg.getP99());
        assertEquals("tenant-2", agg.getTenantId());
        assertEquals("{}", agg.getDimensions());
        assertEquals(now, agg.getCreatedAt());
        assertEquals(now, agg.getUpdatedAt());
    }

    @Test
    void testOnCreate() {
        MetricAggregation agg = new MetricAggregation();
        agg.onCreate();
        assertNotNull(agg.getCreatedAt());
        assertNotNull(agg.getUpdatedAt());
    }

    @Test
    void testOnUpdate() {
        MetricAggregation agg = new MetricAggregation();
        agg.setUpdatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
        agg.onUpdate();
        assertNotNull(agg.getUpdatedAt());
    }

    @Test
    void testAggregationTypeEnumValues() {
        assertEquals(7, MetricAggregation.AggregationType.values().length);
        assertSame(MetricAggregation.AggregationType.SUM, MetricAggregation.AggregationType.valueOf("SUM"));
        assertSame(MetricAggregation.AggregationType.AVG, MetricAggregation.AggregationType.valueOf("AVG"));
        assertSame(MetricAggregation.AggregationType.MIN, MetricAggregation.AggregationType.valueOf("MIN"));
        assertSame(MetricAggregation.AggregationType.MAX, MetricAggregation.AggregationType.valueOf("MAX"));
        assertSame(MetricAggregation.AggregationType.COUNT, MetricAggregation.AggregationType.valueOf("COUNT"));
        assertSame(MetricAggregation.AggregationType.RATE, MetricAggregation.AggregationType.valueOf("RATE"));
        assertSame(MetricAggregation.AggregationType.PERCENTILE, MetricAggregation.AggregationType.valueOf("PERCENTILE"));
    }

    @Test
    void testEqualsAndHashCode() {
        MetricAggregation a1 = MetricAggregation.builder().id(1L).metricName("x").build();
        MetricAggregation a2 = MetricAggregation.builder().id(1L).metricName("x").build();
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testToString() {
        MetricAggregation agg = MetricAggregation.builder().metricName("test").build();
        assertNotNull(agg.toString());
        assertTrue(agg.toString().contains("test"));
    }

    @Test
    void testEquals_sameReference() {
        MetricAggregation agg = new MetricAggregation();
        assertEquals(agg, agg);
    }

    @Test
    void testEquals_null_returnsFalse() {
        MetricAggregation agg = new MetricAggregation();
        assertNotEquals(null, agg);
    }

    @Test
    void testEquals_differentType_returnsFalse() {
        MetricAggregation agg = new MetricAggregation();
        assertNotEquals("not an aggregation", agg);
    }

    @Test
    void testEquals_fullyPopulatedEqual() {
        LocalDateTime now = LocalDateTime.now();
        MetricAggregation a1 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        MetricAggregation a2 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testEquals_fullyPopulatedDifferInId() {
        LocalDateTime now = LocalDateTime.now();
        MetricAggregation a1 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        MetricAggregation a2 = MetricAggregation.builder()
                .id(2L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differInMetricName() {
        LocalDateTime now = LocalDateTime.now();
        MetricAggregation a1 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        MetricAggregation a2 = MetricAggregation.builder()
                .id(1L).metricName("mem").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differInAggregationType() {
        LocalDateTime now = LocalDateTime.now();
        MetricAggregation a1 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        MetricAggregation a2 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.SUM)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differInTenantId() {
        LocalDateTime now = LocalDateTime.now();
        MetricAggregation a1 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        MetricAggregation a2 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t2").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testHashCode_fullyPopulated() {
        LocalDateTime now = LocalDateTime.now();
        MetricAggregation agg = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        int h1 = agg.hashCode();
        agg.setMetricName("mem");
        int h2 = agg.hashCode();
        assertNotEquals(h1, h2);
    }

    @Test
    void testEquals_oneNullOneSet_differ() {
        MetricAggregation a1 = MetricAggregation.builder().id(1L).metricName("cpu").build();
        MetricAggregation a2 = MetricAggregation.builder().id(1L).metricName("cpu")
                .aggregationType(MetricAggregation.AggregationType.AVG).build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differentWindowSize() {
        LocalDateTime now = LocalDateTime.now();
        MetricAggregation a1 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        MetricAggregation a2 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(120).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differentCount() {
        LocalDateTime now = LocalDateTime.now();
        MetricAggregation a1 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(100L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        MetricAggregation a2 = MetricAggregation.builder()
                .id(1L).metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG)
                .windowSizeSeconds(60).windowStart(now).windowEnd(now.plusHours(1))
                .count(200L).sum(new BigDecimal("1000")).avg(new BigDecimal("10"))
                .min(new BigDecimal("1")).max(new BigDecimal("100"))
                .p50(new BigDecimal("50")).p95(new BigDecimal("95")).p99(new BigDecimal("99"))
                .tenantId("t1").dimensions("{\"r\":\"us\"}").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }
}
