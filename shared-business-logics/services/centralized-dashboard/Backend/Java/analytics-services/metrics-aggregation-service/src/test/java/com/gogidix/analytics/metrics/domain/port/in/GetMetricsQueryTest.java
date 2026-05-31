package com.gogidix.analytics.metrics.domain.port.in;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GetMetricsQueryTest {

    @Test
    void testBuilder() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now();

        GetMetricsQuery query = GetMetricsQuery.builder()
                .metricName("cpu.usage")
                .sourceService("order-service")
                .startTime(start)
                .endTime(end)
                .aggregationType("AVG")
                .aggregationWindowSeconds(300)
                .limit(100)
                .offset(0)
                .orderBy("timestamp")
                .groupBy("tenant")
                .build();

        assertEquals("cpu.usage", query.getMetricName());
        assertEquals("order-service", query.getSourceService());
        assertEquals(start, query.getStartTime());
        assertEquals(end, query.getEndTime());
        assertEquals("AVG", query.getAggregationType());
        assertEquals(300, query.getAggregationWindowSeconds());
        assertEquals(100, query.getLimit());
        assertEquals(0, query.getOffset());
        assertEquals("timestamp", query.getOrderBy());
        assertEquals("tenant", query.getGroupBy());
    }

    @Test
    void testNoArgsConstructor() {
        GetMetricsQuery query = new GetMetricsQuery();
        assertNotNull(query);
        assertNull(query.getMetricName());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery query = new GetMetricsQuery(
                "m", "s", now, now, "AVG", 60, 10, 0, "t", "g");

        assertEquals("m", query.getMetricName());
        assertEquals("s", query.getSourceService());
        assertEquals("AVG", query.getAggregationType());
        assertEquals(60, query.getAggregationWindowSeconds());
        assertEquals(10, query.getLimit());
        assertEquals(0, query.getOffset());
        assertEquals("t", query.getOrderBy());
        assertEquals("g", query.getGroupBy());
    }

    @Test
    void testSettersAndGetters() {
        GetMetricsQuery query = new GetMetricsQuery();
        LocalDateTime now = LocalDateTime.now();

        query.setMetricName("mem");
        query.setSourceService("svc");
        query.setStartTime(now);
        query.setEndTime(now);
        query.setAggregationType("SUM");
        query.setAggregationWindowSeconds(120);
        query.setLimit(50);
        query.setOffset(10);
        query.setOrderBy("value");
        query.setGroupBy("region");

        assertEquals("mem", query.getMetricName());
        assertEquals("svc", query.getSourceService());
        assertEquals(now, query.getStartTime());
        assertEquals(now, query.getEndTime());
        assertEquals("SUM", query.getAggregationType());
        assertEquals(120, query.getAggregationWindowSeconds());
        assertEquals(50, query.getLimit());
        assertEquals(10, query.getOffset());
        assertEquals("value", query.getOrderBy());
        assertEquals("region", query.getGroupBy());
    }

    @Test
    void testEqualsAndHashCode() {
        GetMetricsQuery q1 = GetMetricsQuery.builder().metricName("a").build();
        GetMetricsQuery q2 = GetMetricsQuery.builder().metricName("a").build();
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    void testToString() {
        GetMetricsQuery query = GetMetricsQuery.builder().metricName("cpu").build();
        assertNotNull(query.toString());
        assertTrue(query.toString().contains("cpu"));
    }

    @Test
    void testEquals_sameReference() {
        GetMetricsQuery query = new GetMetricsQuery();
        assertEquals(query, query);
    }

    @Test
    void testEquals_null_returnsFalse() {
        GetMetricsQuery query = new GetMetricsQuery();
        assertNotEquals(null, query);
    }

    @Test
    void testEquals_differentType_returnsFalse() {
        GetMetricsQuery query = new GetMetricsQuery();
        assertNotEquals("not a query", query);
    }

    @Test
    void testEquals_fullyPopulatedEqual() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery q1 = GetMetricsQuery.builder()
                .metricName("cpu.usage").sourceService("order-service")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        GetMetricsQuery q2 = GetMetricsQuery.builder()
                .metricName("cpu.usage").sourceService("order-service")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    void testEquals_differInMetricName() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery q1 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        GetMetricsQuery q2 = GetMetricsQuery.builder()
                .metricName("mem").sourceService("svc")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        assertNotEquals(q1, q2);
    }

    @Test
    void testEquals_differInSourceService() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery q1 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc-a")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        GetMetricsQuery q2 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc-b")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        assertNotEquals(q1, q2);
    }

    @Test
    void testEquals_differInStartTime() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery q1 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        GetMetricsQuery q2 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc")
                .startTime(now.plusHours(1)).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        assertNotEquals(q1, q2);
    }

    @Test
    void testEquals_differInAggregationType() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery q1 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        GetMetricsQuery q2 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc")
                .startTime(now).endTime(now).aggregationType("SUM")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        assertNotEquals(q1, q2);
    }

    @Test
    void testHashCode_fullyPopulated() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery query = GetMetricsQuery.builder()
                .metricName("cpu.usage").sourceService("order-service")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        int h1 = query.hashCode();
        query.setMetricName("mem.usage");
        int h2 = query.hashCode();
        assertNotEquals(h1, h2);
    }

    @Test
    void testEquals_oneNullOneSet_differ() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery q1 = GetMetricsQuery.builder()
                .metricName("cpu").startTime(now).endTime(now).build();
        GetMetricsQuery q2 = GetMetricsQuery.builder()
                .metricName("cpu").startTime(now).endTime(now)
                .sourceService("svc").build();
        assertNotEquals(q1, q2);
    }

    @Test
    void testEquals_differInLimit() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery q1 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        GetMetricsQuery q2 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(50).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        assertNotEquals(q1, q2);
    }

    @Test
    void testEquals_differInOrderBy() {
        LocalDateTime now = LocalDateTime.now();
        GetMetricsQuery q1 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("timestamp").groupBy("tenant")
                .build();
        GetMetricsQuery q2 = GetMetricsQuery.builder()
                .metricName("cpu").sourceService("svc")
                .startTime(now).endTime(now).aggregationType("AVG")
                .aggregationWindowSeconds(300).limit(100).offset(0)
                .orderBy("value").groupBy("tenant")
                .build();
        assertNotEquals(q1, q2);
    }
}
