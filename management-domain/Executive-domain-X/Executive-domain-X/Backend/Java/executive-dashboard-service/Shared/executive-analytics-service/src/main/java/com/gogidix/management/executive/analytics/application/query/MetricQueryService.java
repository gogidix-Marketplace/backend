package com.gogidix.management.executive.analytics.application.query;

import com.gogidix.management.executive.analytics.domain.model.Metric;
import com.gogidix.management.executive.analytics.domain.repository.MetricRepository;
import com.gogidix.management.shared.exception.NotFoundException;
import com.gogidix.management.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricQueryService {
    private final MetricRepository metricRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "metric", key = "#id")
    public Metric getMetricById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metric: {} for tenant: {}", id, tenantId);
        return metricRepository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new NotFoundException("Metric", id));
    }

    @Transactional(readOnly = true)
    public List<Metric> getMetricsByName(String metricName) {
        log.debug("Fetching metrics by name: {}", metricName);
        return metricRepository.findByMetricName(metricName);
    }

    @Transactional(readOnly = true)
    public Page<Metric> getLatestMetricsByName(String metricName, Pageable pageable) {
        log.debug("Fetching latest metrics by name: {}", metricName);
        return metricRepository.findByMetricNameOrderByTimestampDesc(metricName, pageable);
    }

    @Transactional(readOnly = true)
    public List<Metric> getMetricsBySourceDomain(String sourceDomain) {
        log.debug("Fetching metrics by source domain: {}", sourceDomain);
        return metricRepository.findBySourceDomain(sourceDomain);
    }

    @Transactional(readOnly = true)
    public List<Metric> getMetricsByNameAndSource(String metricName, String sourceDomain) {
        log.debug("Fetching metrics: {} from {}", metricName, sourceDomain);
        return metricRepository.findByMetricNameAndSourceDomain(metricName, sourceDomain);
    }

    @Transactional(readOnly = true)
    public List<Metric> getMetricsByTimeRange(Instant startTime, Instant endTime) {
        log.debug("Fetching metrics between {} and {}", startTime, endTime);
        return metricRepository.findByTimestampRange(startTime, endTime);
    }

    @Transactional(readOnly = true)
    public List<Metric> getRecentMetrics(int hours) {
        Instant since = Instant.now().minusSeconds(hours * 3600L);
        log.debug("Fetching metrics from last {} hours", hours);
        return metricRepository.findRecent(since);
    }

    @Transactional(readOnly = true)
    public List<Metric> getHighQualityMetrics() {
        log.debug("Fetching high quality metrics");
        return metricRepository.findHighQuality();
    }

    @Transactional(readOnly = true)
    public List<Metric> getAggregateMetrics() {
        log.debug("Fetching aggregate metrics");
        return metricRepository.findByIsAggregateTrue();
    }

    @Transactional(readOnly = true)
    public List<Metric> getRawMetrics() {
        log.debug("Fetching raw metrics");
        return metricRepository.findByIsAggregateFalse();
    }

    @Transactional(readOnly = true)
    public List<Metric> getMetricsByDimension(String dimensionKey, String dimensionValue) {
        log.debug("Fetching metrics by dimension: {} = {}", dimensionKey, dimensionValue);
        return metricRepository.findByDimension(dimensionKey, dimensionValue);
    }

    @Transactional(readOnly = true)
    public List<Metric> getMetricsBySourceDomains(List<String> sourceDomains) {
        log.debug("Fetching metrics from sources: {}", sourceDomains);
        return metricRepository.findBySourceDomainIn(sourceDomains);
    }

    @Transactional(readOnly = true)
    public List<Metric> searchMetrics(String searchTerm) {
        log.debug("Searching metrics with term: {}", searchTerm);
        return metricRepository.search(searchTerm);
    }

    @Transactional(readOnly = true)
    public List<Metric> getMetricsForAggregation(String metricName, Instant startTime, Instant endTime, String granularity) {
        log.debug("Fetching metrics for aggregation: {} from {} to {}", metricName, startTime, endTime);
        return metricRepository.findForAggregation(metricName, startTime, endTime, granularity);
    }

    @Transactional(readOnly = true)
    public List<Metric> getTopLevelMetrics() {
        log.debug("Fetching top-level metrics");
        return metricRepository.findTopLevel();
    }

    @Transactional(readOnly = true)
    public List<Metric> getChildMetrics(String parentMetricId) {
        log.debug("Fetching child metrics of: {}", parentMetricId);
        return metricRepository.findByParentMetricId(parentMetricId);
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueMetricNames() {
        return metricRepository.findDistinctMetricNames();
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueSourceDomains() {
        return metricRepository.findDistinctSourceDomains();
    }

    @Transactional(readOnly = true)
    public long countBySourceDomain(String sourceDomain) {
        return metricRepository.countBySourceDomain(sourceDomain);
    }
}
