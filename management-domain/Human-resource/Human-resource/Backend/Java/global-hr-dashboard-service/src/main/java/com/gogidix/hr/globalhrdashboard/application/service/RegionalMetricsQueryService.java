package com.gogidix.hr.globalhrdashboard.application.service;

import com.gogidix.hr.globalhrdashboard.domain.model.RegionalMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;
import com.gogidix.hr.globalhrdashboard.domain.repository.RegionalMetricRepository;
import com.gogidix.hr.globalhrdashboard.shared.exception.NotFoundException;
import com.gogidix.hr.globalhrdashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Query Service for Regional Metrics
 * Handles all read operations for regional HR metrics
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegionalMetricsQueryService {

    private final RegionalMetricRepository regionalMetricRepository;

    /**
     * Get all regional metrics
     */
    public List<RegionalMetric> getAllRegionalMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all regional metrics for tenant: {}", tenantId);
        return regionalMetricRepository.findByTenantId(tenantId);
    }

    /**
     * Get metrics by region code
     */
    public List<RegionalMetric> getMetricsByRegion(String regionCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional metrics for region: {} tenant: {}", regionCode, tenantId);
        return regionalMetricRepository.findByTenantIdAndRegionCode(tenantId, regionCode);
    }

    /**
     * Get specific regional metric
     */
    public RegionalMetric getRegionalMetric(String regionCode, String metricName, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional metric for region: {} metric: {} period: {} tenant: {}",
                regionCode, metricName, period, tenantId);

        return regionalMetricRepository.findByTenantIdAndRegionCodeAndMetricNameAndPeriod(
                tenantId, regionCode, metricName, period)
                .orElseThrow(() -> new NotFoundException("RegionalMetric",
                        String.format("region=%s,metric=%s,period=%s", regionCode, metricName, period)));
    }

    /**
     * Get metrics by period
     */
    public List<RegionalMetric> getMetricsByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional metrics for period: {} tenant: {}", period, tenantId);
        return regionalMetricRepository.findByTenantIdAndPeriod(tenantId, period);
    }

    /**
     * Get metrics by region and period
     */
    public List<RegionalMetric> getMetricsByRegionAndPeriod(String regionCode, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional metrics for region: {} period: {} tenant: {}",
                regionCode, period, tenantId);
        return regionalMetricRepository.findByTenantIdAndRegionCodeAndPeriod(tenantId, regionCode, period);
    }

    /**
     * Get countries in a region
     */
    public List<RegionalMetric.CountryMetric> getCountriesInRegion(String regionCode, String metricName, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching countries for region: {} metric: {} period: {} tenant: {}",
                regionCode, metricName, period, tenantId);

        RegionalMetric metric = regionalMetricRepository.findByTenantIdAndRegionCodeAndMetricNameAndPeriod(
                tenantId, regionCode, metricName, period)
                .orElseThrow(() -> new NotFoundException("RegionalMetric",
                        String.format("region=%s,metric=%s,period=%s", regionCode, metricName, period)));

        return metric.getCountries();
    }

    /**
     * Get regional trend
     */
    public List<RegionalMetric> getRegionalTrend(String regionCode, String metricName,
                                                  String startPeriod, String endPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional trend for region: {} metric: {} from {} to {} tenant: {}",
                regionCode, metricName, startPeriod, endPeriod, tenantId);

        return regionalMetricRepository.findTrendData(tenantId, regionCode, metricName, startPeriod, endPeriod);
    }

    /**
     * Get all regions
     */
    public List<String> getAllRegions() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all regions for tenant: {}", tenantId);
        return regionalMetricRepository.findDistinctRegionsByTenantId(tenantId);
    }

    /**
     * Get metrics by category
     */
    public List<RegionalMetric> getMetricsByCategory(MetricCategory category) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional metrics by category: {} tenant: {}", category, tenantId);
        return regionalMetricRepository.findByTenantIdAndMetricCategory(tenantId, category);
    }

    /**
     * Get active metrics
     */
    public List<RegionalMetric> getActiveMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching active regional metrics for tenant: {}", tenantId);
        return regionalMetricRepository.findActiveByTenantId(tenantId);
    }

    /**
     * Get regional summary for a period
     */
    public RegionalSummary getRegionalSummary(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional summary for period: {} tenant: {}", period, tenantId);

        List<RegionalMetric> metrics = regionalMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        Map<String, Long> countByRegion = metrics.stream()
                .collect(Collectors.groupingBy(
                        RegionalMetric::getRegionCode,
                        Collectors.counting()
                ));

        Map<String, Double> averageValueByRegion = metrics.stream()
                .collect(Collectors.groupingBy(
                        RegionalMetric::getRegionCode,
                        Collectors.averagingDouble(m -> m.getValue() != null ? m.getValue() : 0.0)
                ));

        return new RegionalSummary(
                metrics.size(),
                countByRegion.size(),
                countByRegion,
                averageValueByRegion
        );
    }

    /**
     * Get metric by ID
     */
    public RegionalMetric getMetricById(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional metric by ID: {} for tenant: {}", metricId, tenantId);
        return regionalMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("RegionalMetric", metricId));
    }

    /**
     * Get metrics updated since
     */
    public List<RegionalMetric> getMetricsUpdatedSince(Instant lastUpdated) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional metrics updated since: {} for tenant: {}", lastUpdated, tenantId);
        return regionalMetricRepository.findByTenantIdAndLastUpdatedAfter(tenantId, lastUpdated);
    }

    /**
     * Get metrics needing update
     */
    public List<RegionalMetric> getMetricsNeedingUpdate(Instant before) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional metrics needing update before: {} for tenant: {}", before, tenantId);
        return regionalMetricRepository.findMetricsNeedingUpdate(tenantId, before);
    }

    /**
     * Get latest metrics by metric name and period
     */
    public List<RegionalMetric> getLatestByMetricNameAndPeriod(String metricName, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest regional metrics for metric: {} period: {} tenant: {}",
                metricName, period, tenantId);
        return regionalMetricRepository.findLatestByMetricNameAndPeriod(tenantId, metricName, period);
    }

    /**
     * Get top performing regions by metric
     */
    public List<RegionalMetric> getTopRegionsByMetric(String metricName, String period, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching top {} regions for metric: {} period: {} tenant: {}",
                limit, metricName, period, tenantId);

        List<RegionalMetric> metrics = regionalMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        return metrics.stream()
                .filter(m -> metricName.equals(m.getMetricName()))
                .filter(m -> m.getValue() != null)
                .sorted(Comparator.comparing(RegionalMetric::getValue).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Count metrics by region
     */
    public long countMetricsByRegion(String regionCode) {
        String tenantId = RequestContextHolder.getTenantId();
        return regionalMetricRepository.countByTenantIdAndRegionCode(tenantId, regionCode);
    }

    /**
     * Check if metric exists
     */
    public boolean metricExists(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return regionalMetricRepository.existsByIdAndTenantId(metricId, tenantId);
    }

    /**
     * Regional summary record
     */
    public record RegionalSummary(
            long totalMetrics,
            long totalRegions,
            Map<String, Long> countByRegion,
            Map<String, Double> averageValueByRegion
    ) {}
}
