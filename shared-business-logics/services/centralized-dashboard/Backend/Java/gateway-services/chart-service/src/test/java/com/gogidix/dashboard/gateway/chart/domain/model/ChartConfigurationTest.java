package com.gogidix.dashboard.gateway.chart.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChartConfigurationTest {

    @Test
    void builderCreatesInstance() {
        LocalDateTime now = LocalDateTime.now();
        ChartConfiguration config = ChartConfiguration.builder()
                .id(1L)
                .chartId("chart-1")
                .chartName("Sales Chart")
                .chartType("line")
                .tenantId("tenant-1")
                .description("desc")
                .dataSource("sales-metric")
                .query("SELECT * FROM sales")
                .config(Map.of("color", "blue"))
                .refreshIntervalSeconds(120)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, config.getId());
        assertEquals("chart-1", config.getChartId());
        assertEquals("Sales Chart", config.getChartName());
        assertEquals("line", config.getChartType());
        assertEquals("tenant-1", config.getTenantId());
        assertEquals("desc", config.getDescription());
        assertEquals("sales-metric", config.getDataSource());
        assertEquals("SELECT * FROM sales", config.getQuery());
        assertEquals(Map.of("color", "blue"), config.getConfig());
        assertEquals(120, config.getRefreshIntervalSeconds());
        assertTrue(config.getEnabled());
        assertEquals(now, config.getCreatedAt());
        assertEquals(now, config.getUpdatedAt());
    }

    @Test
    void noArgsConstructorCreatesInstance() {
        ChartConfiguration config = new ChartConfiguration();
        assertNotNull(config);
        assertNull(config.getId());
        assertNull(config.getChartId());
    }

    @Test
    void allArgsConstructorCreatesInstance() {
        ChartConfiguration config = new ChartConfiguration(
                1L, "c1", "name", "bar", "t1", "desc",
                "ds", "q", Map.of(), 30, true,
                LocalDateTime.now(), LocalDateTime.now());
        assertEquals(1L, config.getId());
        assertEquals("c1", config.getChartId());
    }

    @Test
    void settersWork() {
        ChartConfiguration config = new ChartConfiguration();
        config.setId(5L);
        config.setChartId("chart-5");
        config.setChartName("Test");
        config.setChartType("pie");
        config.setTenantId("t2");
        config.setDescription("new desc");
        config.setDataSource("src");
        config.setQuery("q");
        config.setConfig(Map.of("k", "v"));
        config.setRefreshIntervalSeconds(45);
        config.setEnabled(false);
        LocalDateTime now = LocalDateTime.now();
        config.setCreatedAt(now);
        config.setUpdatedAt(now);

        assertEquals(5L, config.getId());
        assertEquals("chart-5", config.getChartId());
        assertEquals("Test", config.getChartName());
        assertEquals("pie", config.getChartType());
        assertEquals("t2", config.getTenantId());
        assertEquals("new desc", config.getDescription());
        assertEquals("src", config.getDataSource());
        assertEquals("q", config.getQuery());
        assertEquals(Map.of("k", "v"), config.getConfig());
        assertEquals(45, config.getRefreshIntervalSeconds());
        assertFalse(config.getEnabled());
        assertEquals(now, config.getCreatedAt());
    }

    @Test
    void builderDefaults() {
        ChartConfiguration config = ChartConfiguration.builder().build();
        assertEquals(Map.of(), config.getConfig());
        assertEquals(60, config.getRefreshIntervalSeconds());
        assertTrue(config.getEnabled());
    }

    @Test
    void equalsAndHashCode() {
        ChartConfiguration c1 = ChartConfiguration.builder().chartId("a").build();
        ChartConfiguration c2 = ChartConfiguration.builder().chartId("a").build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void toStringContainsFields() {
        ChartConfiguration config = ChartConfiguration.builder()
                .chartId("c1").chartName("Test").build();
        String str = config.toString();
        assertNotNull(str);
        assertTrue(str.contains("c1"));
    }
}
