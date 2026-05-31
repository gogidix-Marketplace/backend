package com.gogidix.finance.globalfinancedashboard.domain.port.out;

import java.time.Period;

import com.gogidix.finance.globalfinancedashboard.domain.model.Metric;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Output port for metric persistence
 */
public interface MetricRepository {

    /**
     * Saves a metric
     */
    Metric save(Metric metric);

    /**
     * Finds a metric by ID
     */
    Optional<Metric> findById(String tenantId, String metricId);

    /**
     * Finds all metrics for a widget
     */
    List<Metric> findByWidgetId(String tenantId, String widgetId);

    /**
     * Finds metrics for a dashboard
     */
    List<Metric> findByDashboardId(String tenantId, String dashboardId);

    /**
     * Finds metrics by type
     */
    List<Metric> findByType(String tenantId, Metric.MetricType type);

    /**
     * Finds metrics by category
     */
    List<Metric> findByCategory(String tenantId, Metric.MetricCategory category);

    /**
     * Finds metrics by time period
     */
    List<Metric> findByTimePeriod(String tenantId, LocalDate start, LocalDate end);

    /**
     * Finds latest metrics
     */
    List<Metric> findLatest(String tenantId, int limit);

    /**
     * Finds stale metrics
     */
    List<Metric> findStale(String tenantId);

    /**
     * Finds metrics in error state
     */
    List<Metric> findInError(String tenantId);

    /**
     * Deletes a metric
     */
    void delete(Metric metric);

    /**
     * Deletes all metrics for a widget
     */
    void deleteByWidgetId(String tenantId, String widgetId);

    /**
     * Batch saves metrics
     */
    List<Metric> saveAll(List<Metric> metrics);

    /**
     * Deletes metrics older than given date
     */
    int deleteOlderThan(String tenantId, LocalDate date);
}
