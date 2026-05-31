package com.gogidix.finance.globalfinancedashboard.domain.port.in;

import com.gogidix.finance.globalfinancedashboard.domain.model.Metric;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Input port for metric commands
 * Defines operations for creating and updating metrics
 */
public interface MetricCommand {

    /**
     * Creates a new metric
     */
    Metric createMetric(String tenantId, String widgetId, String dashboardId,
                       String name, Metric.MetricType type, Metric.MetricCategory category,
                       BigDecimal value, String currency);

    /**
     * Updates metric value
     */
    Metric updateMetricValue(String tenantId, String metricId, BigDecimal newValue,
                            BigDecimal previousValue);

    /**
     * Sets metric target
     */
    Metric setMetricTarget(String tenantId, String metricId, BigDecimal target);

    /**
     * Adds data point to metric
     */
    Metric addDataPoint(String tenantId, String metricId, LocalDate date,
                       BigDecimal value, String label);

    /**
     * Sets metric time period
     */
    Metric setTimePeriod(String tenantId, String metricId, LocalDate start,
                        LocalDate end, String granularity);

    /**
     * Sets monthly period
     */
    Metric setMonthlyPeriod(String tenantId, String metricId, int year, int month);

    /**
     * Sets quarterly period
     */
    Metric setQuarterlyPeriod(String tenantId, String metricId, int year, int quarter);

    /**
     * Sets yearly period
     */
    Metric setYearlyPeriod(String tenantId, String metricId, int year);

    /**
     * Marks metric as calculated
     */
    Metric markAsCalculated(String tenantId, String metricId, String calculatedBy);

    /**
     * Marks metric as error
     */
    Metric markAsError(String tenantId, String metricId, String errorMessage);

    /**
     * Recalculates metric
     */
    Metric recalculate(String tenantId, String metricId);

    /**
     * Deletes metric
     */
    void deleteMetric(String tenantId, String metricId);

    /**
     * Batch creates metrics
     */
    List<Metric> batchCreate(List<Metric> metrics);

    /**
     * Batch updates metrics
     */
    List<Metric> batchUpdate(List<Metric> metrics);
}
