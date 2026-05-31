package com.gogidix.hr.globalhrdashboard.application.service;

import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus;
import com.gogidix.hr.globalhrdashboard.domain.repository.ComplianceMetricRepository;
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
 * Query Service for Compliance Metrics
 * Handles all read operations for compliance data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ComplianceQueryService {

    private final ComplianceMetricRepository complianceMetricRepository;

    /**
     * Get all compliance metrics
     */
    public List<ComplianceMetric> getAllComplianceMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all compliance metrics for tenant: {}", tenantId);
        return complianceMetricRepository.findByTenantId(tenantId);
    }

    /**
     * Get compliance metrics by country
     */
    public List<ComplianceMetric> getComplianceByCountry(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metrics for country: {} tenant: {}", countryCode, tenantId);
        return complianceMetricRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
    }

    /**
     * Get compliance metric by country and period
     */
    public ComplianceMetric getComplianceByCountryAndPeriod(String countryCode, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metric for country: {} period: {} tenant: {}",
                countryCode, period, tenantId);

        return complianceMetricRepository.findByTenantIdAndCountryCodeAndPeriod(tenantId, countryCode, period)
                .orElseThrow(() -> new NotFoundException("ComplianceMetric",
                        String.format("country=%s,period=%s", countryCode, period)));
    }

    /**
     * Get compliance metrics by period
     */
    public List<ComplianceMetric> getComplianceByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metrics for period: {} tenant: {}", period, tenantId);
        return complianceMetricRepository.findByTenantIdAndPeriod(tenantId, period);
    }

    /**
     * Get compliance metrics by region
     */
    public List<ComplianceMetric> getComplianceByRegion(String regionCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metrics for region: {} tenant: {}", regionCode, tenantId);
        return complianceMetricRepository.findByTenantIdAndRegionCode(tenantId, regionCode);
    }

    /**
     * Get at-risk countries
     */
    public List<ComplianceMetric> getAtRiskCountries() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching at-risk compliance metrics for tenant: {}", tenantId);
        return complianceMetricRepository.findAtRiskByTenantId(tenantId);
    }

    /**
     * Get non-compliant countries
     */
    public List<ComplianceMetric> getNonCompliantCountries() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching non-compliant metrics for tenant: {}", tenantId);
        return complianceMetricRepository.findNonCompliantByTenantId(tenantId);
    }

    /**
     * Get metrics with critical issues
     */
    public List<ComplianceMetric> getWithCriticalIssues() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metrics with critical issues for tenant: {}", tenantId);
        return complianceMetricRepository.findWithCriticalIssuesByTenantId(tenantId);
    }

    /**
     * Get compliance metrics by status
     */
    public List<ComplianceMetric> getComplianceByStatus(ComplianceStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metrics by status: {} tenant: {}", status, tenantId);
        return complianceMetricRepository.findByTenantIdAndStatus(tenantId, status);
    }

    /**
     * Get compliance metrics with score below threshold
     */
    public List<ComplianceMetric> getComplianceBelowThreshold(Double threshold) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metrics below threshold: {} tenant: {}", threshold, tenantId);
        return complianceMetricRepository.findByTenantIdAndComplianceScoreLessThan(tenantId, threshold);
    }

    /**
     * Get compliance metrics with score above threshold
     */
    public List<ComplianceMetric> getComplianceAboveThreshold(Double threshold) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metrics above threshold: {} tenant: {}", threshold, tenantId);
        return complianceMetricRepository.findByTenantIdAndComplianceScoreGreaterThanEqual(tenantId, threshold);
    }

    /**
     * Get average compliance score
     */
    public Double getAverageComplianceScore(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Calculating average compliance score for period: {} tenant: {}", period, tenantId);
        return complianceMetricRepository.getAverageComplianceScore(tenantId, period);
    }

    /**
     * Get global compliance status
     */
    public ComplianceStatus getGlobalComplianceStatus(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching global compliance status for period: {} tenant: {}", period, tenantId);
        return complianceMetricRepository.getGlobalComplianceStatus(tenantId, period)
                .orElse(ComplianceStatus.PENDING_REVIEW);
    }

    /**
     * Get compliance trend for a country
     */
    public List<ComplianceMetric> getComplianceTrendByCountry(String countryCode, String startPeriod, String endPeriod) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance trend for country: {} from {} to {} tenant: {}",
                countryCode, startPeriod, endPeriod, tenantId);
        return complianceMetricRepository.findTrendDataByCountry(tenantId, countryCode, startPeriod, endPeriod);
    }

    /**
     * Get compliance summary
     */
    public ComplianceSummary getComplianceSummary(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance summary for period: {} tenant: {}", period, tenantId);

        List<ComplianceMetric> metrics = complianceMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        long compliantCount = metrics.stream().filter(m -> m.getStatus() == ComplianceStatus.COMPLIANT).count();
        long atRiskCount = metrics.stream().filter(m -> m.getStatus() == ComplianceStatus.AT_RISK).count();
        long nonCompliantCount = metrics.stream().filter(m -> m.getStatus() == ComplianceStatus.NON_COMPLIANT).count();
        long withCriticalIssues = metrics.stream().filter(ComplianceMetric::hasCriticalIssues).count();

        Double avgScore = metrics.stream()
                .mapToDouble(ComplianceMetric::getComplianceScore)
                .average()
                .orElse(0.0);

        return new ComplianceSummary(
                metrics.size(),
                compliantCount,
                atRiskCount,
                nonCompliantCount,
                withCriticalIssues,
                avgScore
        );
    }

    /**
     * Get metric by ID
     */
    public ComplianceMetric getComplianceById(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metric by ID: {} for tenant: {}", metricId, tenantId);
        return complianceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("ComplianceMetric", metricId));
    }

    /**
     * Get metrics assessed since
     */
    public List<ComplianceMetric> getMetricsAssessedSince(Instant lastAssessed) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metrics assessed since: {} for tenant: {}", lastAssessed, tenantId);
        return complianceMetricRepository.findByTenantIdAndLastAssessedAfter(tenantId, lastAssessed);
    }

    /**
     * Get metrics needing assessment
     */
    public List<ComplianceMetric> getMetricsNeedingAssessment(Instant before) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance metrics needing assessment before: {} for tenant: {}", before, tenantId);
        return complianceMetricRepository.findMetricsNeedingAssessment(tenantId, before);
    }

    /**
     * Get latest compliance metrics for a period
     */
    public List<ComplianceMetric> getLatestByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest compliance metrics for period: {} tenant: {}", period, tenantId);
        return complianceMetricRepository.findLatestByPeriod(tenantId, period);
    }

    /**
     * Get countries with lowest compliance score
     */
    public List<ComplianceMetric> getBottomCountriesByCompliance(String period, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching bottom {} countries by compliance for period: {} tenant: {}",
                limit, period, tenantId);

        List<ComplianceMetric> metrics = complianceMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        return metrics.stream()
                .filter(m -> m.getComplianceScore() != null)
                .sorted(Comparator.comparing(ComplianceMetric::getComplianceScore))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get countries with highest compliance score
     */
    public List<ComplianceMetric> getTopCountriesByCompliance(String period, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching top {} countries by compliance for period: {} tenant: {}",
                limit, period, tenantId);

        List<ComplianceMetric> metrics = complianceMetricRepository.findByTenantIdAndPeriod(tenantId, period);

        return metrics.stream()
                .filter(m -> m.getComplianceScore() != null)
                .sorted(Comparator.comparing(ComplianceMetric::getComplianceScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Count metrics by status
     */
    public long countByStatus(ComplianceStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return complianceMetricRepository.countByTenantIdAndStatus(tenantId, status);
    }

    /**
     * Count metrics with critical issues
     */
    public long countWithCriticalIssues() {
        String tenantId = RequestContextHolder.getTenantId();
        return complianceMetricRepository.countWithCriticalIssuesByTenantId(tenantId);
    }

    /**
     * Check if metric exists
     */
    public boolean metricExists(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return complianceMetricRepository.existsByIdAndTenantId(metricId, tenantId);
    }

    /**
     * Compliance summary record
     */
    public record ComplianceSummary(
            long totalCountries,
            long compliantCount,
            long atRiskCount,
            long nonCompliantCount,
            long withCriticalIssues,
            double averageScore
    ) {}
}
