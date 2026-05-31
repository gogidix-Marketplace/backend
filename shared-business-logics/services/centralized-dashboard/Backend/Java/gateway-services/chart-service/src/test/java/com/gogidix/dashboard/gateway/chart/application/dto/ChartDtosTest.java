package com.gogidix.dashboard.gateway.chart.application.dto;

import com.gogidix.dashboard.gateway.chart.application.dto.request.ChartDataRequestDto;
import com.gogidix.dashboard.gateway.chart.application.dto.request.CreateChartRequestDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartConfigurationResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartDataResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartSummaryResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChartDtosTest {

    @Test
    void chartDataResponseDto_builderAndGetters() {
        ChartDataResponseDto dto = ChartDataResponseDto.builder()
                .chartId("c1")
                .chartName("Test")
                .chartType("line")
                .data(List.of(ChartDataResponseDto.DataPoint.builder()
                        .timestamp(LocalDateTime.now())
                        .value(42.0)
                        .label("l1")
                        .metadata(Map.of("k", "v"))
                        .build()))
                .metadata(Map.of("key", "val"))
                .generatedAt(LocalDateTime.now())
                .tenantId("t1")
                .build();

        assertEquals("c1", dto.getChartId());
        assertEquals("Test", dto.getChartName());
        assertEquals(1, dto.getData().size());
        assertEquals(42.0, dto.getData().get(0).getValue());
    }

    @Test
    void chartDataResponseDto_noArgsAndSetters() {
        ChartDataResponseDto dto = new ChartDataResponseDto();
        dto.setChartId("x");
        dto.setChartName("y");
        assertEquals("x", dto.getChartId());
        assertEquals("y", dto.getChartName());
    }

    @Test
    void chartDataResponseDto_dataPoint_noArgsAndSetters() {
        ChartDataResponseDto.DataPoint dp = new ChartDataResponseDto.DataPoint();
        dp.setLabel("lbl");
        dp.setValue(10.0);
        dp.setTimestamp(LocalDateTime.now());
        assertEquals("lbl", dp.getLabel());
        assertEquals(10.0, dp.getValue());
    }

    @Test
    void chartConfigurationResponseDto_builderAndGetters() {
        ChartConfigurationResponseDto dto = ChartConfigurationResponseDto.builder()
                .id(1L).chartId("c1").chartName("N").chartType("bar")
                .tenantId("t1").description("d").dataSource("ds")
                .query("q").config(Map.of()).refreshIntervalSeconds(30)
                .enabled(true).createdAt(LocalDateTime.now()).build();

        assertEquals(1L, dto.getId());
        assertEquals("c1", dto.getChartId());
        assertEquals(30, dto.getRefreshIntervalSeconds());
    }

    @Test
    void chartConfigurationResponseDto_noArgsAndSetters() {
        ChartConfigurationResponseDto dto = new ChartConfigurationResponseDto();
        dto.setChartId("x");
        dto.setEnabled(false);
        assertEquals("x", dto.getChartId());
        assertFalse(dto.getEnabled());
    }

    @Test
    void chartSummaryResponseDto_builderAndGetters() {
        ChartSummaryResponseDto dto = ChartSummaryResponseDto.builder()
                .totalCharts(10L).activeCharts(8L).totalDataPoints(1000L)
                .chartsByType(Map.of("line", 5L, "bar", 5L))
                .timestamp("2025-01-01T00:00:00").build();

        assertEquals(10L, dto.getTotalCharts());
        assertEquals(8L, dto.getActiveCharts());
        assertEquals(1000L, dto.getTotalDataPoints());
        assertEquals(2, dto.getChartsByType().size());
    }

    @Test
    void chartSummaryResponseDto_noArgsAndSetters() {
        ChartSummaryResponseDto dto = new ChartSummaryResponseDto();
        dto.setTotalCharts(5L);
        assertEquals(5L, dto.getTotalCharts());
    }

    @Test
    void chartDataRequestDto_builderAndGetters() {
        ChartDataRequestDto dto = ChartDataRequestDto.builder()
                .chartId("c1")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .aggregation("avg")
                .intervalMinutes(60L)
                .limit(100)
                .build();

        assertEquals("c1", dto.getChartId());
        assertEquals("avg", dto.getAggregation());
        assertEquals(60L, dto.getIntervalMinutes());
        assertEquals(100, dto.getLimit());
    }

    @Test
    void chartDataRequestDto_noArgsAndSetters() {
        ChartDataRequestDto dto = new ChartDataRequestDto();
        dto.setChartId("x");
        dto.setAggregation("sum");
        assertEquals("x", dto.getChartId());
        assertEquals("sum", dto.getAggregation());
    }

    @Test
    void createChartRequestDto_builderAndGetters() {
        CreateChartRequestDto dto = CreateChartRequestDto.builder()
                .chartId("c1").chartName("Name").chartType("pie")
                .description("d").dataSource("ds").query("q")
                .config(Map.of("k", "v")).refreshIntervalSeconds(30)
                .enabled(true).build();

        assertEquals("c1", dto.getChartId());
        assertEquals("Name", dto.getChartName());
        assertEquals("pie", dto.getChartType());
        assertTrue(dto.getEnabled());
    }

    @Test
    void createChartRequestDto_noArgsAndSetters() {
        CreateChartRequestDto dto = new CreateChartRequestDto();
        dto.setChartId("x");
        dto.setChartName("n");
        dto.setChartType("t");
        dto.setEnabled(false);
        assertEquals("x", dto.getChartId());
        assertFalse(dto.getEnabled());
    }
}
