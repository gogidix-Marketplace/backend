package com.gogidix.dashboard.gateway.chart.application.service;

import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartConfigurationResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartDataResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartSummaryResponseDto;
import com.gogidix.dashboard.gateway.chart.domain.model.ChartConfiguration;
import com.gogidix.dashboard.gateway.chart.domain.model.TimeSeriesData;
import com.gogidix.dashboard.gateway.chart.domain.repository.ChartConfigurationRepository;
import com.gogidix.dashboard.gateway.chart.domain.repository.TimeSeriesDataRepository;
import com.gogidix.dashboard.gateway.chart.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for handling chart data operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChartDataService {

    private final ChartConfigurationRepository chartConfigurationRepository;
    private final TimeSeriesDataRepository timeSeriesDataRepository;

    /**
     * Get chart data by chart ID
     */
    @Cacheable(value = "chart-data", key = "#chartId + ':' + #tenantId", unless = "#result == null")
    public ChartDataResponseDto getChartData(String chartId, String tenantId,
                                               LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Fetching chart data for chartId: {}, tenantId: {}", chartId, tenantId);

        ChartConfiguration chart = chartConfigurationRepository
                .findByChartIdAndTenantId(chartId, tenantId)
                .orElse(null);

        if (chart == null || !chart.getEnabled()) {
            return ChartDataResponseDto.builder()
                    .chartId(chartId)
                    .tenantId(tenantId)
                    .data(Collections.emptyList())
                    .generatedAt(LocalDateTime.now())
                    .build();
        }

        List<ChartDataResponseDto.DataPoint> dataPoints = fetchTimeSeriesData(
                chart.getDataSource(), tenantId, startTime, endTime);

        return ChartDataResponseDto.builder()
                .chartId(chartId)
                .chartName(chart.getChartName())
                .chartType(chart.getChartType())
                .data(dataPoints)
                .metadata(chart.getConfig())
                .generatedAt(LocalDateTime.now())
                .tenantId(tenantId)
                .build();
    }

    /**
     * Get all charts for tenant
     */
    public List<ChartConfigurationResponseDto> getAllCharts(String tenantId) {
        List<ChartConfiguration> charts = chartConfigurationRepository
                .findByTenantIdAndEnabledTrueOrderByCreatedAtDesc(tenantId);

        return charts.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Create chart configuration
     */
    public ChartConfigurationResponseDto createChart(ChartConfiguration chartConfiguration) {
        String tenantId = TenantContext.getTenantId();
        chartConfiguration.setTenantId(tenantId);
        chartConfiguration.setCreatedAt(LocalDateTime.now());

        ChartConfiguration saved = chartConfigurationRepository.save(chartConfiguration);
        return toResponseDto(saved);
    }

    /**
     * Get chart summary
     */
    @Cacheable(value = "chart-summary", key = "#tenantId", unless = "#result == null")
    public ChartSummaryResponseDto getChartSummary(String tenantId) {
        List<ChartConfiguration> allCharts = chartConfigurationRepository
                .findByTenantIdAndEnabledTrueOrderByCreatedAtDesc(tenantId);

        long totalCharts = allCharts.size();
        long activeCharts = allCharts.stream().filter(ChartConfiguration::getEnabled).count();
        long totalDataPoints = timeSeriesDataRepository.count();

        Map<String, Long> chartsByType = allCharts.stream()
                .collect(Collectors.groupingBy(ChartConfiguration::getChartType, Collectors.counting()));

        return ChartSummaryResponseDto.builder()
                .totalCharts(totalCharts)
                .activeCharts(activeCharts)
                .totalDataPoints(totalDataPoints)
                .chartsByType(chartsByType)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    /**
     * Add time series data point
     */
    public TimeSeriesData addTimeSeriesData(TimeSeriesData data) {
        data.setCreatedAt(LocalDateTime.now());
        return timeSeriesDataRepository.save(data);
    }

    /**
     * Get aggregate metrics for dashboard
     */
    public Map<String, Object> getAggregateMetrics(String tenantId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);

        List<String> metricNames = timeSeriesDataRepository.findDistinctMetricNamesByTenantId(tenantId);

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalMetrics", metricNames.size());
        metrics.put("metricNames", metricNames);
        metrics.put("timestamp", now.toString());

        // Get latest values for each metric
        Map<String, Double> latestValues = new HashMap<>();
        for (String metricName : metricNames) {
            try {
                Double avgValue = timeSeriesDataRepository.getAverageValue(metricName, tenantId, yesterday, now);
                if (avgValue != null) {
                    latestValues.put(metricName, avgValue);
                }
            } catch (Exception e) {
                log.warn("Error fetching metric {}: {}", metricName, e.getMessage());
            }
        }
        metrics.put("latestValues", latestValues);

        return metrics;
    }

    /**
     * Fetch time series data
     */
    private List<ChartDataResponseDto.DataPoint> fetchTimeSeriesData(
            String metricName, String tenantId, LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime == null) {
            startTime = LocalDateTime.now().minusHours(24);
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }

        List<TimeSeriesData> timeSeriesData = timeSeriesDataRepository
                .findByMetricNameAndTenantIdAndTimestampBetweenOrderByTimestampAsc(
                        metricName, tenantId, startTime, endTime);

        return timeSeriesData.stream()
                .map(ts -> ChartDataResponseDto.DataPoint.builder()
                        .timestamp(ts.getTimestamp())
                        .value(ts.getValue())
                        .label(ts.getLabels())
                        .build())
                .collect(Collectors.toList());
    }

    private ChartConfigurationResponseDto toResponseDto(ChartConfiguration chart) {
        return ChartConfigurationResponseDto.builder()
                .id(chart.getId())
                .chartId(chart.getChartId())
                .chartName(chart.getChartName())
                .chartType(chart.getChartType())
                .tenantId(chart.getTenantId())
                .description(chart.getDescription())
                .dataSource(chart.getDataSource())
                .query(chart.getQuery())
                .config(chart.getConfig())
                .refreshIntervalSeconds(chart.getRefreshIntervalSeconds())
                .enabled(chart.getEnabled())
                .createdAt(chart.getCreatedAt())
                .updatedAt(chart.getUpdatedAt())
                .build();
    }
}
