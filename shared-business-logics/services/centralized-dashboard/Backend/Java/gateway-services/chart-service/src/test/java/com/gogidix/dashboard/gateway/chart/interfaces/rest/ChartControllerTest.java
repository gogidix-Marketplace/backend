package com.gogidix.dashboard.gateway.chart.interfaces.rest;

import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartConfigurationResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartDataResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartSummaryResponseDto;
import com.gogidix.dashboard.gateway.chart.application.service.ChartDataService;
import com.gogidix.dashboard.gateway.chart.domain.model.ChartConfiguration;
import com.gogidix.dashboard.gateway.chart.domain.model.TimeSeriesData;
import com.gogidix.dashboard.gateway.chart.infrastructure.security.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChartControllerTest {

    @Mock
    private ChartDataService chartDataService;

    @InjectMocks
    private ChartController chartController;

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void getChartData_returnsOk() {
        ChartDataResponseDto dto = ChartDataResponseDto.builder()
                .chartId("c1").data(List.of()).build();
        when(chartDataService.getChartData(eq("c1"), eq("t1"), any(), any()))
                .thenReturn(dto);

        ResponseEntity<ChartDataResponseDto> response = chartController.getChartData(
                "c1", null, null, "t1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("c1", response.getBody().getChartId());
        assertEquals("t1", TenantContext.getTenantId());
    }

    @Test
    void getChartData_withTimeRange_returnsOk() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 2, 0, 0);
        ChartDataResponseDto dto = ChartDataResponseDto.builder().chartId("c1").build();
        when(chartDataService.getChartData("c1", "t1", start, end)).thenReturn(dto);

        ResponseEntity<ChartDataResponseDto> response = chartController.getChartData("c1", start, end, "t1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllCharts_returnsOk() {
        ChartConfigurationResponseDto dto = ChartConfigurationResponseDto.builder()
                .chartId("c1").build();
        when(chartDataService.getAllCharts("t1")).thenReturn(List.of(dto));

        ResponseEntity<List<ChartConfigurationResponseDto>> response = chartController.getAllCharts("t1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllCharts_returnsEmptyList() {
        when(chartDataService.getAllCharts("t1")).thenReturn(List.of());

        ResponseEntity<List<ChartConfigurationResponseDto>> response = chartController.getAllCharts("t1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getChartSummary_returnsOk() {
        ChartSummaryResponseDto dto = ChartSummaryResponseDto.builder()
                .totalCharts(5L).activeCharts(3L).build();
        when(chartDataService.getChartSummary("t1")).thenReturn(dto);

        ResponseEntity<ChartSummaryResponseDto> response = chartController.getChartSummary("t1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody().getTotalCharts());
    }

    @Test
    void createChart_returnsCreated() {
        ChartConfiguration input = ChartConfiguration.builder()
                .chartId("new").chartName("Test").chartType("line").build();
        ChartConfigurationResponseDto dto = ChartConfigurationResponseDto.builder()
                .chartId("new").chartName("Test").build();
        when(chartDataService.createChart(any(ChartConfiguration.class))).thenReturn(dto);

        ResponseEntity<ChartConfigurationResponseDto> response = chartController.createChart(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("new", response.getBody().getChartId());
    }

    @Test
    void addDataPoint_returnsCreated() {
        TimeSeriesData input = TimeSeriesData.builder()
                .metricName("cpu").value(50.0).build();
        TimeSeriesData saved = TimeSeriesData.builder()
                .id(1L).metricName("cpu").value(50.0).build();
        when(chartDataService.addTimeSeriesData(any(TimeSeriesData.class))).thenReturn(saved);

        ResponseEntity<TimeSeriesData> response = chartController.addDataPoint(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getAggregateMetrics_returnsOk() {
        Map<String, Object> metrics = Map.of("totalMetrics", 3);
        when(chartDataService.getAggregateMetrics("t1")).thenReturn(metrics);

        ResponseEntity<Map<String, Object>> response = chartController.getAggregateMetrics("t1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().get("totalMetrics"));
    }
}
