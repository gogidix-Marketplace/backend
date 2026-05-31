package com.gogidix.dashboard.gateway.chart.application.service;

import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartConfigurationResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartDataResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartSummaryResponseDto;
import com.gogidix.dashboard.gateway.chart.domain.model.ChartConfiguration;
import com.gogidix.dashboard.gateway.chart.domain.model.TimeSeriesData;
import com.gogidix.dashboard.gateway.chart.domain.repository.ChartConfigurationRepository;
import com.gogidix.dashboard.gateway.chart.domain.repository.TimeSeriesDataRepository;
import com.gogidix.dashboard.gateway.chart.infrastructure.security.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChartDataServiceTest {

    @Mock
    private ChartConfigurationRepository chartConfigurationRepository;

    @Mock
    private TimeSeriesDataRepository timeSeriesDataRepository;

    @InjectMocks
    private ChartDataService chartDataService;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId("test-tenant");
        startTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        endTime = LocalDateTime.of(2025, 1, 2, 0, 0);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void getChartData_whenChartNotFound_returnsEmptyData() {
        when(chartConfigurationRepository.findByChartIdAndTenantId("chart1", "tenant1"))
                .thenReturn(Optional.empty());

        ChartDataResponseDto result = chartDataService.getChartData("chart1", "tenant1", startTime, endTime);

        assertNotNull(result);
        assertEquals("chart1", result.getChartId());
        assertTrue(result.getData().isEmpty());
    }

    @Test
    void getChartData_whenChartDisabled_returnsEmptyData() {
        ChartConfiguration disabledChart = ChartConfiguration.builder()
                .chartId("c1").enabled(false).build();
        when(chartConfigurationRepository.findByChartIdAndTenantId("c1", "t1"))
                .thenReturn(Optional.of(disabledChart));

        ChartDataResponseDto result = chartDataService.getChartData("c1", "t1", startTime, endTime);

        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
    }

    @Test
    void getChartData_whenChartEnabled_returnsData() {
        ChartConfiguration chart = ChartConfiguration.builder()
                .chartId("c1").chartName("Test").chartType("line")
                .tenantId("t1").dataSource("cpu").enabled(true)
                .config(Map.of("color", "red")).build();

        TimeSeriesData tsData = TimeSeriesData.builder()
                .timestamp(startTime).value(42.0).labels("host=s1").build();

        when(chartConfigurationRepository.findByChartIdAndTenantId("c1", "t1"))
                .thenReturn(Optional.of(chart));
        when(timeSeriesDataRepository.findByMetricNameAndTenantIdAndTimestampBetweenOrderByTimestampAsc(
                eq("cpu"), eq("t1"), any(), any()))
                .thenReturn(List.of(tsData));

        ChartDataResponseDto result = chartDataService.getChartData("c1", "t1", startTime, endTime);

        assertNotNull(result);
        assertEquals("c1", result.getChartId());
        assertEquals("Test", result.getChartName());
        assertEquals("line", result.getChartType());
        assertEquals(1, result.getData().size());
        assertEquals(42.0, result.getData().get(0).getValue());
    }

    @Test
    void getChartData_withNullTimeRanges_usesDefaults() {
        ChartConfiguration chart = ChartConfiguration.builder()
                .chartId("c1").chartName("N").chartType("bar")
                .dataSource("mem").enabled(true).build();

        when(chartConfigurationRepository.findByChartIdAndTenantId("c1", "t1"))
                .thenReturn(Optional.of(chart));
        when(timeSeriesDataRepository.findByMetricNameAndTenantIdAndTimestampBetweenOrderByTimestampAsc(
                eq("mem"), eq("t1"), any(), any()))
                .thenReturn(Collections.emptyList());

        ChartDataResponseDto result = chartDataService.getChartData("c1", "t1", null, null);

        assertNotNull(result);
        assertEquals("c1", result.getChartId());
    }

    @Test
    void getAllCharts_returnsMappedDtos() {
        ChartConfiguration chart = ChartConfiguration.builder()
                .id(1L).chartId("c1").chartName("Chart1").chartType("line")
                .tenantId("t1").description("d").dataSource("ds").query("q")
                .config(Map.of()).refreshIntervalSeconds(60).enabled(true)
                .createdAt(LocalDateTime.now()).build();

        when(chartConfigurationRepository.findByTenantIdAndEnabledTrueOrderByCreatedAtDesc("t1"))
                .thenReturn(List.of(chart));

        List<ChartConfigurationResponseDto> result = chartDataService.getAllCharts("t1");

        assertEquals(1, result.size());
        assertEquals("c1", result.get(0).getChartId());
        assertEquals("Chart1", result.get(0).getChartName());
    }

    @Test
    void getAllCharts_whenNoCharts_returnsEmptyList() {
        when(chartConfigurationRepository.findByTenantIdAndEnabledTrueOrderByCreatedAtDesc("t1"))
                .thenReturn(Collections.emptyList());

        List<ChartConfigurationResponseDto> result = chartDataService.getAllCharts("t1");

        assertTrue(result.isEmpty());
    }

    @Test
    void createChart_savesAndReturnsDto() {
        ChartConfiguration input = ChartConfiguration.builder()
                .chartId("new-chart").chartName("New").chartType("pie")
                .build();

        ChartConfiguration saved = ChartConfiguration.builder()
                .id(1L).chartId("new-chart").chartName("New").chartType("pie")
                .tenantId("test-tenant").createdAt(LocalDateTime.now()).build();

        when(chartConfigurationRepository.save(any(ChartConfiguration.class))).thenReturn(saved);

        ChartConfigurationResponseDto result = chartDataService.createChart(input);

        assertNotNull(result);
        verify(chartConfigurationRepository).save(argThat(c ->
                "test-tenant".equals(c.getTenantId()) && c.getCreatedAt() != null));
    }

    @Test
    void getChartSummary_returnsCorrectCounts() {
        ChartConfiguration c1 = ChartConfiguration.builder().chartType("line").enabled(true).build();
        ChartConfiguration c2 = ChartConfiguration.builder().chartType("bar").enabled(true).build();
        ChartConfiguration c3 = ChartConfiguration.builder().chartType("line").enabled(true).build();

        when(chartConfigurationRepository.findByTenantIdAndEnabledTrueOrderByCreatedAtDesc("t1"))
                .thenReturn(List.of(c1, c2, c3));
        when(timeSeriesDataRepository.count()).thenReturn(100L);

        ChartSummaryResponseDto result = chartDataService.getChartSummary("t1");

        assertEquals(3, result.getTotalCharts());
        assertEquals(3, result.getActiveCharts());
        assertEquals(100L, result.getTotalDataPoints());
        assertEquals(2L, result.getChartsByType().get("line"));
        assertEquals(1L, result.getChartsByType().get("bar"));
    }

    @Test
    void addTimeSeriesData_savesWithTimestamp() {
        TimeSeriesData input = TimeSeriesData.builder()
                .metricName("cpu").tenantId("t1").value(80.0).build();

        when(timeSeriesDataRepository.save(any(TimeSeriesData.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        TimeSeriesData result = chartDataService.addTimeSeriesData(input);

        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        assertEquals("cpu", result.getMetricName());
    }

    @Test
    void getAggregateMetrics_returnsMetricsMap() {
        when(timeSeriesDataRepository.findDistinctMetricNamesByTenantId("t1"))
                .thenReturn(List.of("cpu", "mem"));
        when(timeSeriesDataRepository.getAverageValue(eq("cpu"), eq("t1"), any(), any()))
                .thenReturn(75.5);
        when(timeSeriesDataRepository.getAverageValue(eq("mem"), eq("t1"), any(), any()))
                .thenReturn(50.0);

        Map<String, Object> result = chartDataService.getAggregateMetrics("t1");

        assertEquals(2, result.get("totalMetrics"));
        assertNotNull(result.get("timestamp"));
        @SuppressWarnings("unchecked")
        Map<String, Double> latest = (Map<String, Double>) result.get("latestValues");
        assertEquals(75.5, latest.get("cpu"));
        assertEquals(50.0, latest.get("mem"));
    }

    @Test
    void getAggregateMetrics_handlesExceptionForMetric() {
        when(timeSeriesDataRepository.findDistinctMetricNamesByTenantId("t1"))
                .thenReturn(List.of("cpu", "bad"));
        when(timeSeriesDataRepository.getAverageValue(eq("cpu"), eq("t1"), any(), any()))
                .thenReturn(10.0);
        when(timeSeriesDataRepository.getAverageValue(eq("bad"), eq("t1"), any(), any()))
                .thenThrow(new RuntimeException("DB error"));

        Map<String, Object> result = chartDataService.getAggregateMetrics("t1");

        assertEquals(2, result.get("totalMetrics"));
        @SuppressWarnings("unchecked")
        Map<String, Double> latest = (Map<String, Double>) result.get("latestValues");
        assertEquals(10.0, latest.get("cpu"));
        assertFalse(latest.containsKey("bad"));
    }

    @Test
    void getAggregateMetrics_handlesNullAverage() {
        when(timeSeriesDataRepository.findDistinctMetricNamesByTenantId("t1"))
                .thenReturn(List.of("cpu"));
        when(timeSeriesDataRepository.getAverageValue(eq("cpu"), eq("t1"), any(), any()))
                .thenReturn(null);

        Map<String, Object> result = chartDataService.getAggregateMetrics("t1");

        @SuppressWarnings("unchecked")
        Map<String, Double> latest = (Map<String, Double>) result.get("latestValues");
        assertTrue(latest.isEmpty());
    }
}
