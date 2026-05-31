package com.gogidix.hr.globalhrdashboard.application.service;

import com.gogidix.hr.globalhrdashboard.domain.model.RetentionMetric;
import com.gogidix.hr.globalhrdashboard.domain.repository.RetentionMetricRepository;
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
 * Query Service for Retention Metrics
 * Handles all read operations for retention data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RetentionQueryService {

    private final RetentionMetricRepository retentionMetricRepository;

    /**
     * Get all retention metrics
     */
    public List<RetentionMetric> getAllRetentionMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all retention metrics for tenant: {}", tenantId);
        return retentionMetricRepository.findByTenantId(tenantId);
    }

    /**
     * Get retention metrics by country
     */
    public List<RetentionMetric> getRetentionByCountry(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metrics for country: {} tenant: {}", countryCode, tenantId);
        return retentionMetricRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
    }

    /**
     * Get retention metric by country and period
     */
    public RetentionMetric getRetentionByCountryAndPeriod(String countryCode, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metric for country: {} period: {} tenant: {}",
                countryCode, period, tenantId);

        return retentionMetricRepository.findByTenantIdAndCountryCodeAndPeriod(tenantId, countryCode, period)
                .orElseThrow(() -> new NotFoundException("RetentionMetric",
                        String.format("country=%s,period=%s", countryCode, period)));
    }

    /**
     * Get retention metrics by region
     */
    public List<RetentionMetric> getRetentionByRegion(String regionCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metrics for region: {} tenant: {}", regionCode, tenantId);
        return retentionMetricRepository.findByTenantIdAndRegionCode(tenantId, regionCode);
    }

    /**
     * Get retention metrics by period
     */
    public List<RetentionMetric> getRetentionByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metrics for period: {} tenant: {}", period, tenantId);
        return retentionMetricRepository.findByTenantIdAndPeriod(tenantId, period);
    }

    /**
     * Get retention metrics by region and period
     */
    public List<RetentionMetric> getRetentionByRegionAndPeriod(String regionCode, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metrics for region: {} period: {} tenant: {}",
                regionCode, period, tenantId);
        return retentionMetricRepository.findByTenantIdAndRegionCodeAndPeriod(tenantId, regionCode, period);
    }

    /**
     * Get metrics with high retention rate
     */
    public List<RetentionMetric> getWithHighRetentionRate(Double minRate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metrics with retention rate >= {} for tenant: {}", minRate, tenantId);
        return retentionMetricRepository.findWithHighRetentionRate(tenantId, minRate);
    }

    /**
     * Get metrics with high turnover rate
     */
    public List<RetentionMetric> getWithHighTurnoverRate(Double maxRate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metrics with turnover rate > {} for tenant: {}", maxRate, tenantId);
        return retentionMetricRepository.findWithHighTurnoverRate(tenantId, maxRate);
    }

    /**
     * Get metrics with low retention rate
     */
    public List<RetentionMetric> getWithLowRetentionRate(Double maxRate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metrics with retention rate < {} for tenant: {}", maxRate, tenantId);
        return retentionMetricRepository.findWithLowRetentionRate(tenantId, maxRate);
    }

    /**
     * Get average retention rate
     */
    public Double getAverageRetentionRate(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating average retention rate for period: {} tenant: {}", period, tenantId);
        return retentionMetricRepository.getAverageRetentionRate(tenantId, period);
    }

    /**
     * Get average turnover rate
     */
    public Double getAverageTurnoverRate(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating average turnover rate for period: {} tenant: {}", period, tenantId);
        return retentionMetricRepository.getAverageTurnoverRate(tenantId, period);
    }

    /**
     * Get global retention rate
     */
    public Double getGlobalRetentionRate(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating global retention rate for period: {} tenant: {}", period, tenantId);
        return retentionMetricRepository.calculateGlobalRetentionRate(tenantId, period);
    }

    /**
     * Get global turnover rate
     */
    public Double getGlobalTurnoverRate(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating global turnover rate for period: {} tenant: {}", period, tenantId);
        return retentionMetricRepository.calculateGlobalTurnoverRate(tenantId, period);
    }

    /**
     * Get global average tenure
     */
    public Double getGlobalAverageTenure(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating global average tenure for period: {} tenant: {}", period, tenantId);
        return retentionMetricRepository.calculateGlobalAverageTenure(tenantId, period);
    }

    /**
     * Get retention trend for a country
     */
    public List<RetentionMetric> getRetentionTrendByCountry(String countryCode, String startPeriod, String endPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention trend for country: {} from {} to {} tenant: {}",
                countryCode, startPeriod, endPeriod, tenantId);
        return retentionMetricRepository.findTrendDataByCountry(tenantId, countryCode, startPeriod, endPeriod);
    }

    /**
     * Get retention trend for a region
     */
    public List<RetentionMetric> getRetentionTrendByRegion(String regionCode, String startPeriod, String endPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention trend for region: {} from {} to {} tenant: {}",
                regionCode, startPeriod, endPeriod, tenantId);
        return retentionMetricRepository.findTrendDataByRegion(tenantId, regionCode, startPeriod, endPeriod);
    }

    /**
     * Get retention summary
     */
    public RetentionSummary getRetentionSummary(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention summary for period: {} tenant: {}", period, tenantId);

        List<RetentionMetric> metrics = retentionMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        int totalEmployees = metrics.stream()
                .mapToInt(m -> m.getTotalEmployees() != null ? m.getTotalEmployees() : 0)
                .sum();

        int totalDepartures = metrics.stream()
                .mapToInt(RetentionMetric::getTotalDepartures)
                .sum();

        Double avgRetentionRate = metrics.stream()
                .filter(m -> m.getRetentionRate() != null)
                .mapToDouble(RetentionMetric::getRetentionRate)
                .average()
                .orElse(0.0);

        Double avgTurnoverRate = metrics.stream()
                .filter(m -> m.getTurnoverRate() != null)
                .mapToDouble(RetentionMetric::getTurnoverRate)
                .average()
                .orElse(0.0);

        Double avgTenure = metrics.stream()
                .filter(m -> m.getAvgTenure() != null)
                .mapToDouble(RetentionMetric::getAvgTenure)
                .average()
                .orElse(0.0);

        long healthyRetentionCount = metrics.stream()
                .filter(RetentionMetric::hasHealthyRetention)
                .count();

        long highTurnoverCount = metrics.stream()
                .filter(RetentionMetric::hasHighTurnover)
                .count();

        return new RetentionSummary(
                metrics.size(),
                totalEmployees,
                totalDepartures,
                avgRetentionRate,
                avgTurnoverRate,
                avgTenure,
                healthyRetentionCount,
                highTurnoverCount
        );
    }

    /**
     * Get metric by ID
     */
    public RetentionMetric getRetentionById(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metric by ID: {} for tenant: {}", metricId, tenantId);
        return retentionMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("RetentionMetric", metricId));
    }

    /**
     * Get metrics updated since
     */
    public List<RetentionMetric> getMetricsUpdatedSince(Instant lastUpdated) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metrics updated since: {} for tenant: {}", lastUpdated, tenantId);
        return retentionMetricRepository.findByTenantIdAndLastUpdatedAfter(tenantId, lastUpdated);
    }

    /**
     * Get metrics needing update
     */
    public List<RetentionMetric> getMetricsNeedingUpdate(Instant before) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching retention metrics needing update before: {} for tenant: {}", before, tenantId);
        return retentionMetricRepository.findMetricsNeedingUpdate(tenantId, before);
    }

    /**
     * Get latest retention metrics for a period
     */
    public List<RetentionMetric> getLatestByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest retention metrics for period: {} tenant: {}", period, tenantId);
        return retentionMetricRepository.findLatestByPeriod(tenantId, period);
    }

    /**
     * Get global retention aggregate
     */
    public RetentionMetric getGlobalAggregate(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching global retention aggregate for period: {} tenant: {}", period, tenantId);
        return retentionMetricRepository.findGlobalAggregateByTenantIdAndPeriod(tenantId, period)
                .orElseThrow(() -> new NotFoundException("GlobalRetentionMetric", period));
    }

    /**
     * Get top countries by retention rate
     */
    public List<RetentionMetric> getTopCountriesByRetention(String period, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching top {} countries by retention for period: {} tenant: {}",
                limit, period, tenantId);
        return retentionMetricRepository.findTopCountriesByRetentionRate(tenantId, period, limit);
    }

    /**
     * Get bottom countries by retention rate
     */
    public List<RetentionMetric> getBottomCountriesByRetention(String period, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching bottom {} countries by retention for period: {} tenant: {}",
                limit, period, tenantId);
        return retentionMetricRepository.findBottomCountriesByRetentionRate(tenantId, period, limit);
    }

    /**
     * Get countries with healthy retention
     */
    public List<RetentionMetric> getCountriesWithHealthyRetention(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching countries with healthy retention for period: {} tenant: {}", period, tenantId);

        List<RetentionMetric> metrics = retentionMetricRepository.findByTenantIdAndPeriod(tenantId, period);
        return metrics.stream()
                .filter(RetentionMetric::hasHealthyRetention)
                .collect(Collectors.toList());
    }

    /**
     * Get countries with high turnover
     */
    public List<RetentionMetric> getCountriesWithHighTurnover(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching countries with high turnover for period: {} tenant: {}", period, tenantId);

        List<RetentionMetric> metrics = retentionMetricRepository.findByTenantIdAndPeriod(tenantId, period);
        return metrics.stream()
                .filter(RetentionMetric::hasHighTurnover)
                .collect(Collectors.toList());
    }

    /**
     * Count metrics by country
     */
    public long countByCountry(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        return retentionMetricRepository.countByTenantIdAndCountryCode(tenantId, countryCode);
    }

    /**
     * Check if metric exists
     */
    public boolean metricExists(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return retentionMetricRepository.existsByIdAndTenantId(metricId, tenantId);
    }

    /**
     * Retention summary record
     */
    public record RetentionSummary(
            long totalCountries,
            int totalEmployees,
            int totalDepartures,
            double averageRetentionRate,
            double averageTurnoverRate,
            double averageTenure,
            long healthyRetentionCount,
            long highTurnoverCount
    ) {}
}
