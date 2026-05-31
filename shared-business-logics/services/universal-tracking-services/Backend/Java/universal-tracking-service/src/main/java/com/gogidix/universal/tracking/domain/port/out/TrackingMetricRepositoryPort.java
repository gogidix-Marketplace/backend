package com.gogidix.universal.tracking.domain.port.out;

import com.gogidix.universal.tracking.domain.model.TrackingMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Tracking Metric repository operations.
 */
public interface TrackingMetricRepositoryPort {

    /**
     * Save a tracking metric
     */
    TrackingMetric save(TrackingMetric metric);

    /**
     * Save multiple metrics
     */
    List<TrackingMetric> saveAll(List<TrackingMetric> metrics);

    /**
     * Find metric by ID
     */
    Optional<TrackingMetric> findById(UUID id);

    /**
     * Find metrics by tenant ID with pagination
     */
    Page<TrackingMetric> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find metrics by name and tenant
     */
    List<TrackingMetric> findByMetricNameAndTenantId(String metricName, String tenantId);

    /**
     * Find metrics by filters
     */
    Page<TrackingMetric> findByFilters(
        String tenantId,
        String metricName,
        String metricType,
        String eventType,
        String source,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    );

    /**
     * Find metric by unique key (name, date, hour, tenant)
     */
    Optional<TrackingMetric> findByUniqueKey(
        String metricName,
        LocalDate metricDate,
        Integer metricHour,
        String tenantId
    );

    /**
     * Find metrics by date range and tenant
     */
    List<TrackingMetric> findByMetricDateBetweenAndTenantId(
        LocalDate startDate,
        LocalDate endDate,
        String tenantId
    );

    /**
     * Delete by ID
     */
    void deleteById(UUID id);

    /**
     * Delete metrics older than specified date
     */
    void deleteByMetricDateBefore(LocalDate date);
}
