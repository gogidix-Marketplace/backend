package com.gogidix.hr.globalhrdashboard.application.service;

import com.gogidix.hr.globalhrdashboard.domain.model.DiversityMetric;
import com.gogidix.hr.globalhrdashboard.domain.repository.DiversityMetricRepository;
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
 * Query Service for Diversity Metrics
 * Handles all read operations for diversity data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DiversityQueryService {

    private final DiversityMetricRepository diversityMetricRepository;

    /**
     * Get all diversity metrics
     */
    public List<DiversityMetric> getAllDiversityMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all diversity metrics for tenant: {}", tenantId);
        return diversityMetricRepository.findByTenantId(tenantId);
    }

    /**
     * Get diversity metrics by country
     */
    public List<DiversityMetric> getDiversityByCountry(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metrics for country: {} tenant: {}", countryCode, tenantId);
        return diversityMetricRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
    }

    /**
     * Get diversity metric by country and period
     */
    public DiversityMetric getDiversityByCountryAndPeriod(String countryCode, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metric for country: {} period: {} tenant: {}",
                countryCode, period, tenantId);

        return diversityMetricRepository.findByTenantIdAndCountryCodeAndPeriod(tenantId, countryCode, period)
                .orElseThrow(() -> new NotFoundException("DiversityMetric",
                        String.format("country=%s,period=%s", countryCode, period)));
    }

    /**
     * Get diversity metrics by region
     */
    public List<DiversityMetric> getDiversityByRegion(String regionCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metrics for region: {} tenant: {}", regionCode, tenantId);
        return diversityMetricRepository.findByTenantIdAndRegionCode(tenantId, regionCode);
    }

    /**
     * Get diversity metrics by period
     */
    public List<DiversityMetric> getDiversityByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metrics for period: {} tenant: {}", period, tenantId);
        return diversityMetricRepository.findByTenantIdAndPeriod(tenantId, period);
    }

    /**
     * Get metrics with high gender diversity
     */
    public List<DiversityMetric> getWithHighGenderDiversity(Double minScore) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metrics with gender diversity >= {} for tenant: {}", minScore, tenantId);
        return diversityMetricRepository.findWithHighGenderDiversity(tenantId, minScore);
    }

    /**
     * Get metrics with high national diversity
     */
    public List<DiversityMetric> getWithHighNationalDiversity(Double minScore) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metrics with national diversity >= {} for tenant: {}", minScore, tenantId);
        return diversityMetricRepository.findWithHighNationalDiversity(tenantId, minScore);
    }

    /**
     * Get metrics with high overall diversity
     */
    public List<DiversityMetric> getWithHighOverallDiversity(Double minScore) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metrics with overall diversity >= {} for tenant: {}", minScore, tenantId);
        return diversityMetricRepository.findWithHighOverallDiversity(tenantId, minScore);
    }

    /**
     * Get average gender diversity score
     */
    public Double getAverageGenderDiversityScore(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating average gender diversity score for period: {} tenant: {}", period, tenantId);
        return diversityMetricRepository.getAverageGenderDiversityScore(tenantId, period);
    }

    /**
     * Get average national diversity score
     */
    public Double getAverageNationalDiversityScore(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating average national diversity score for period: {} tenant: {}", period, tenantId);
        return diversityMetricRepository.getAverageNationalDiversityScore(tenantId, period);
    }

    /**
     * Get average overall diversity score
     */
    public Double getAverageOverallDiversityScore(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating average overall diversity score for period: {} tenant: {}", period, tenantId);
        return diversityMetricRepository.getAverageOverallDiversityScore(tenantId, period);
    }

    /**
     * Get diversity trend for a country
     */
    public List<DiversityMetric> getDiversityTrendByCountry(String countryCode, String startPeriod, String endPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity trend for country: {} from {} to {} tenant: {}",
                countryCode, startPeriod, endPeriod, tenantId);
        return diversityMetricRepository.findTrendDataByCountry(tenantId, countryCode, startPeriod, endPeriod);
    }

    /**
     * Get diversity trend for a region
     */
    public List<DiversityMetric> getDiversityTrendByRegion(String regionCode, String startPeriod, String endPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity trend for region: {} from {} to {} tenant: {}",
                regionCode, startPeriod, endPeriod, tenantId);
        return diversityMetricRepository.findTrendDataByRegion(tenantId, regionCode, startPeriod, endPeriod);
    }

    /**
     * Get diversity summary
     */
    public DiversitySummary getDiversitySummary(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity summary for period: {} tenant: {}", period, tenantId);

        List<DiversityMetric> metrics = diversityMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        int totalEmployees = metrics.stream()
                .mapToInt(m -> m.getTotalEmployees() != null ? m.getTotalEmployees() : 0)
                .sum();

        Double avgGenderScore = metrics.stream()
                .filter(m -> m.getGenderDiversityScore() != null)
                .mapToDouble(DiversityMetric::getGenderDiversityScore)
                .average()
                .orElse(0.0);

        Double avgNationalScore = metrics.stream()
                .filter(m -> m.getNationalDiversityScore() != null)
                .mapToDouble(DiversityMetric::getNationalDiversityScore)
                .average()
                .orElse(0.0);

        Double avgOverallScore = metrics.stream()
                .filter(m -> m.getOverallDiversityScore() != null)
                .mapToDouble(DiversityMetric::getOverallDiversityScore)
                .average()
                .orElse(0.0);

        return new DiversitySummary(
                metrics.size(),
                totalEmployees,
                avgGenderScore,
                avgNationalScore,
                avgOverallScore
        );
    }

    /**
     * Get metric by ID
     */
    public DiversityMetric getDiversityById(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metric by ID: {} for tenant: {}", metricId, tenantId);
        return diversityMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("DiversityMetric", metricId));
    }

    /**
     * Get metrics updated since
     */
    public List<DiversityMetric> getMetricsUpdatedSince(Instant lastUpdated) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metrics updated since: {} for tenant: {}", lastUpdated, tenantId);
        return diversityMetricRepository.findByTenantIdAndLastUpdatedAfter(tenantId, lastUpdated);
    }

    /**
     * Get metrics needing update
     */
    public List<DiversityMetric> getMetricsNeedingUpdate(Instant before) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching diversity metrics needing update before: {} for tenant: {}", before, tenantId);
        return diversityMetricRepository.findMetricsNeedingUpdate(tenantId, before);
    }

    /**
     * Get latest diversity metrics for a period
     */
    public List<DiversityMetric> getLatestByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest diversity metrics for period: {} tenant: {}", period, tenantId);
        return diversityMetricRepository.findLatestByPeriod(tenantId, period);
    }

    /**
     * Get global diversity aggregate
     */
    public DiversityMetric getGlobalAggregate(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching global diversity aggregate for period: {} tenant: {}", period, tenantId);
        return diversityMetricRepository.findGlobalAggregateByTenantIdAndPeriod(tenantId, period)
                .orElseThrow(() -> new NotFoundException("GlobalDiversityMetric", period));
    }

    /**
     * Get top countries by diversity score
     */
    public List<DiversityMetric> getTopCountriesByDiversity(String period, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching top {} countries by diversity for period: {} tenant: {}",
                limit, period, tenantId);
        return diversityMetricRepository.findTopCountriesByDiversityScore(tenantId, period, limit);
    }

    /**
     * Get countries with good gender balance
     */
    public List<DiversityMetric> getCountriesWithGoodGenderBalance(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching countries with good gender balance for period: {} tenant: {}", period, tenantId);

        List<DiversityMetric> metrics = diversityMetricRepository.findByTenantIdAndPeriod(tenantId, period);
        return metrics.stream()
                .filter(DiversityMetric::hasGoodGenderBalance)
                .collect(Collectors.toList());
    }

    /**
     * Get countries with high national diversity
     */
    public List<DiversityMetric> getCountriesWithHighNationalDiversity(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching countries with high national diversity for period: {} tenant: {}", period, tenantId);

        List<DiversityMetric> metrics = diversityMetricRepository.findByTenantIdAndPeriod(tenantId, period);
        return metrics.stream()
                .filter(DiversityMetric::hasHighNationalDiversity)
                .collect(Collectors.toList());
    }

    /**
     * Count metrics by country
     */
    public long countByCountry(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        return diversityMetricRepository.countByTenantIdAndCountryCode(tenantId, countryCode);
    }

    /**
     * Check if metric exists
     */
    public boolean metricExists(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return diversityMetricRepository.existsByIdAndTenantId(metricId, tenantId);
    }

    /**
     * Diversity summary record
     */
    public record DiversitySummary(
            long totalCountries,
            int totalEmployees,
            double averageGenderDiversityScore,
            double averageNationalDiversityScore,
            double averageOverallDiversityScore
    ) {}
}
