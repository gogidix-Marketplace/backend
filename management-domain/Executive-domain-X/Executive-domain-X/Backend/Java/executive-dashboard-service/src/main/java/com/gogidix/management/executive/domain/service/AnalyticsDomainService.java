package com.gogidix.management.executive.domain.service;

import com.gogidix.management.executive.domain.model.AnalyticsData;
import com.gogidix.management.executive.domain.model.Dashboard;
import com.gogidix.management.executive.application.dto.AnalyticsQueryRequest;

import java.time.Instant;
import java.util.List;

/**
 * Domain service for Analytics business logic
 */
public interface AnalyticsDomainService {

    /**
     * Analytics result record
     */
    static record AnalyticsResult(String metricId, Object value, Instant timestamp, String metadata) {}

    /**
     * Analytics aggregate record
     */
    static record AnalyticsAggregate(double sum, double average, double min, double max, long count) {}

    /**
     * Trend analysis record
     */
    static record TrendAnalysis(String metricId, double current, double previous, double changePercent, TrendDirection direction) {}

    /**
     * Trend direction enum
     */
    static enum TrendDirection {
        UP, DOWN, STABLE
    }

    /**
     * Validate analytics query request
     */
    void validateAnalyticsQuery(AnalyticsQueryRequest query);

    /**
     * Execute analytics query
     */
    AnalyticsResult executeQuery(AnalyticsQueryRequest query, String tenantId);

    /**
     * Get analytics data for dashboard
     */
    List<AnalyticsData> getDashboardAnalytics(String dashboardId, String tenantId);

    /**
     * Aggregate analytics across dashboards
     */
    AnalyticsAggregate aggregateDashboardAnalytics(List<String> dashboardIds, String tenantId);

    /**
     * Calculate trend analysis
     */
    TrendAnalysis calculateTrend(String metricId, Instant startDate, Instant endDate, String tenantId);

    /**
     * Validate date range for analytics query
     */
    void validateDateRange(Instant startDate, Instant endDate);
}
