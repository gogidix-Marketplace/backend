package com.gogidix.dashboard.reporting.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportParametersTest {

    @Test
    void builder_createsParameters() {
        ReportParameters params = ReportParameters.builder()
                .withDomain("order")
                .withDateRange(LocalDateTime.of(2025, 1, 1, 0, 0),
                        LocalDateTime.of(2025, 1, 31, 23, 59))
                .withParameters(Map.of("key", "value"))
                .build();

        assertEquals("order", params.getDomain());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), params.getFromDate());
        assertEquals(LocalDateTime.of(2025, 1, 31, 23, 59), params.getToDate());
        assertEquals("value", params.getParameters().get("key"));
    }

    @Test
    void getId_returnsCompositeId() {
        ReportParameters params = ReportParameters.builder()
                .withDomain("order")
                .withDateRange(LocalDateTime.of(2025, 1, 1, 0, 0),
                        LocalDateTime.of(2025, 1, 31, 0, 0))
                .build();
        assertNotNull(params.getId());
        assertTrue(params.getId().contains("order"));
    }

    @Test
    void hasSorting_true() {
        ReportParameters params = ReportParameters.builder()
                .withParameters(Map.of("sort", "name")).build();
        assertTrue(params.hasSorting());
    }

    @Test
    void hasSorting_false() {
        ReportParameters params = ReportParameters.builder()
                .withParameters(Map.of()).build();
        assertFalse(params.hasSorting());
    }

    @Test
    void hasFiltering_true() {
        ReportParameters params = ReportParameters.builder()
                .withParameters(Map.of("filter", "active")).build();
        assertTrue(params.hasFiltering());
    }

    @Test
    void getDataForSection_returnsCopy() {
        ReportParameters params = ReportParameters.builder()
                .withParameters(Map.of("k1", "v1")).build();
        Map<String, Object> data = params.getDataForSection("s1");
        assertEquals("v1", data.get("k1"));
    }

    @Test
    void getTableDataForSection_returnsEmptyWhenNotSet() {
        ReportParameters params = ReportParameters.builder()
                .withParameters(Map.of()).build();
        assertTrue(params.getTableDataForSection("s1").isEmpty());
    }

    @Test
    void getChartDataForSection_returnsEmptyWhenNotSet() {
        ReportParameters params = ReportParameters.builder()
                .withParameters(Map.of()).build();
        assertTrue(params.getChartDataForSection("s1").isEmpty());
    }

    @Test
    void getMetricsForSection_returnsEmptyWhenNotSet() {
        ReportParameters params = ReportParameters.builder()
                .withParameters(Map.of()).build();
        assertTrue(params.getMetricsForSection("s1").isEmpty());
    }

    @Test
    void builder_nullParameters_defaultsToEmpty() {
        ReportParameters params = ReportParameters.builder()
                .withParameters(null).build();
        assertTrue(params.getParameters().isEmpty());
    }
}
