package com.gogidix.hr.globalhrdashboard.application.service;

import com.gogidix.hr.globalhrdashboard.domain.model.GlobalWorkforceMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;
import com.gogidix.hr.globalhrdashboard.domain.model.ExecutiveLevel;
import com.gogidix.hr.globalhrdashboard.domain.repository.GlobalWorkforceMetricRepository;
import com.gogidix.hr.globalhrdashboard.shared.exception.NotFoundException;
import com.gogidix.hr.globalhrdashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Command Service for Global Workforce Metrics
 * Handles all write operations for global HR metrics
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GlobalMetricsCommandService {

    private final GlobalWorkforceMetricRepository globalWorkforceMetricRepository;

    /**
     * Create a new global workforce metric
     */
    @Transactional
    public GlobalWorkforceMetric createMetric(String metricName, MetricCategory category,
                                               ExecutiveLevel executiveLevel, Double value, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        log.info("Creating global metric: {} for tenant: {}", metricName, tenantId);

        GlobalWorkforceMetric metric = new GlobalWorkforceMetric(
                tenantId, metricName, category, executiveLevel, value, period
        );

        GlobalWorkforceMetric saved = globalWorkforceMetricRepository.save(metric);
        log.info("Created global metric with ID: {}", saved.getId());
        return saved;
    }

    /**
     * Update an existing metric's value
     */
    @Transactional
    public GlobalWorkforceMetric updateMetricValue(String metricId, Double newValue) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        log.info("Updating metric value for ID: {} in tenant: {}", metricId, tenantId);

        GlobalWorkforceMetric metric = globalWorkforceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("GlobalWorkforceMetric", metricId));

        metric.setValue(newValue);
        metric.updateTimestamp(userId);

        return globalWorkforceMetricRepository.save(metric);
    }

    /**
     * Update metric target value
     */
    @Transactional
    public GlobalWorkforceMetric updateMetricTarget(String metricId, Double targetValue) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        log.info("Updating metric target for ID: {} in tenant: {}", metricId, tenantId);

        GlobalWorkforceMetric metric = globalWorkforceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("GlobalWorkforceMetric", metricId));

        metric.setTargetValue(targetValue);
        metric.updateTimestamp(userId);

        return globalWorkforceMetricRepository.save(metric);
    }

    /**
     * Add or update regional breakdown
     */
    @Transactional
    public GlobalWorkforceMetric addRegionalBreakdown(String metricId, String regionCode,
                                                       com.gogidix.hr.globalhrdashboard.domain.model.RegionalMetric regionalMetric) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        log.info("Adding regional breakdown for metric ID: {} region: {} in tenant: {}",
                metricId, regionCode, tenantId);

        GlobalWorkforceMetric metric = globalWorkforceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("GlobalWorkforceMetric", metricId));

        metric.addRegionalMetric(regionCode, regionalMetric);
        metric.updateTimestamp(userId);

        return globalWorkforceMetricRepository.save(metric);
    }

    /**
     * Add metadata to a metric
     */
    @Transactional
    public GlobalWorkforceMetric addMetadata(String metricId, String key, Object value) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        log.debug("Adding metadata to metric ID: {} in tenant: {}", metricId, tenantId);

        GlobalWorkforceMetric metric = globalWorkforceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("GlobalWorkforceMetric", metricId));

        metric.addMetadata(key, value);
        metric.updateTimestamp(userId);

        return globalWorkforceMetricRepository.save(metric);
    }

    /**
     * Update metadata for a metric
     */
    @Transactional
    public GlobalWorkforceMetric updateMetadata(String metricId, Map<String, Object> metadata) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        log.debug("Updating metadata for metric ID: {} in tenant: {}", metricId, tenantId);

        GlobalWorkforceMetric metric = globalWorkforceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("GlobalWorkforceMetric", metricId));

        metric.setMetadata(metadata);
        metric.updateTimestamp(userId);

        return globalWorkforceMetricRepository.save(metric);
    }

    /**
     * Activate a metric
     */
    @Transactional
    public GlobalWorkforceMetric activateMetric(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        log.info("Activating metric ID: {} in tenant: {}", metricId, tenantId);

        GlobalWorkforceMetric metric = globalWorkforceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("GlobalWorkforceMetric", metricId));

        metric.activate();
        metric.updateTimestamp(userId);

        return globalWorkforceMetricRepository.save(metric);
    }

    /**
     * Deactivate a metric
     */
    @Transactional
    public GlobalWorkforceMetric deactivateMetric(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        log.info("Deactivating metric ID: {} in tenant: {}", metricId, tenantId);

        GlobalWorkforceMetric metric = globalWorkforceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("GlobalWorkforceMetric", metricId));

        metric.deactivate();
        metric.updateTimestamp(userId);

        return globalWorkforceMetricRepository.save(metric);
    }

    /**
     * Delete a metric
     */
    @Transactional
    public void deleteMetric(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Deleting metric ID: {} in tenant: {}", metricId, tenantId);

        if (!globalWorkforceMetricRepository.existsByIdAndTenantId(metricId, tenantId)) {
            throw new NotFoundException("GlobalWorkforceMetric", metricId);
        }

        globalWorkforceMetricRepository.deleteByIdAndTenantId(metricId, tenantId);
        log.info("Deleted metric ID: {}", metricId);
    }

    /**
     * Delete all metrics for a period
     */
    @Transactional
    public void deleteMetricsByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Deleting all metrics for period: {} in tenant: {}", period, tenantId);

        globalWorkforceMetricRepository.deleteByTenantIdAndPeriod(tenantId, period);
        log.info("Deleted metrics for period: {}", period);
    }

    /**
     * Record aggregation for a metric
     */
    @Transactional
    public GlobalWorkforceMetric recordAggregation(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");

        log.debug("Recording aggregation for metric ID: {} in tenant: {}", metricId, tenantId);

        GlobalWorkforceMetric metric = globalWorkforceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("GlobalWorkforceMetric", metricId));

        metric.recordAggregation();
        metric.updateTimestamp(userId);

        return globalWorkforceMetricRepository.save(metric);
    }

    /**
     * Batch save metrics
     */
    @Transactional
    public List<GlobalWorkforceMetric> batchSave(List<GlobalWorkforceMetric> metrics) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Batch saving {} metrics for tenant: {}", metrics.size(), tenantId);

        // Ensure all metrics belong to the current tenant
        metrics.forEach(m -> {
            if (!tenantId.equals(m.getTenantId())) {
                m.setTenantId(tenantId);
            }
        });

        return globalWorkforceMetricRepository.saveAll(metrics);
    }
}
