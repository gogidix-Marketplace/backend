package com.gogidix.monitoring.monitoringdataservice.application.service;

import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricAggregation;
import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.MetricAggregationRepositoryPort;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.MetricDataPointRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for metric aggregation.
 * Runs scheduled jobs to aggregate metrics into different time windows.
 */
@Service
public class MetricAggregationService {

    private static final Logger log = LoggerFactory.getLogger(MetricAggregationService.class);

    private final MetricDataPointRepositoryPort metricRepository;
    private final MetricAggregationRepositoryPort aggregationRepository;

    public MetricAggregationService(
            MetricDataPointRepositoryPort metricRepository,
            MetricAggregationRepositoryPort aggregationRepository) {
        this.metricRepository = metricRepository;
        this.aggregationRepository = aggregationRepository;
    }

    /**
     * Aggregate metrics into 1-minute windows.
     * Runs every minute.
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 60000)
    @Transactional
    public void aggregateMinuteWindow() {
        log.debug("Aggregating metrics into 1-minute windows");

        Instant windowEnd = Instant.now();
        Instant windowStart = windowEnd.minusSeconds(60);

        aggregateForWindow(MetricAggregation.AggregationWindow.MINUTE, windowStart, windowEnd);
    }

    /**
     * Aggregate metrics into 5-minute windows.
     * Runs every 5 minutes.
     */
    @Scheduled(fixedDelay = 300000, initialDelay = 300000)
    @Transactional
    public void aggregateFiveMinuteWindow() {
        log.debug("Aggregating metrics into 5-minute windows");

        Instant windowEnd = Instant.now();
        Instant windowStart = windowEnd.minusSeconds(300);

        aggregateForWindow(MetricAggregation.AggregationWindow.FIVE_MINUTES, windowStart, windowEnd);
    }

    /**
     * Aggregate metrics into 1-hour windows.
     * Runs every hour.
     */
    @Scheduled(fixedDelay = 3600000, initialDelay = 3600000)
    @Transactional
    public void aggregateHourWindow() {
        log.debug("Aggregating metrics into 1-hour windows");

        Instant windowEnd = Instant.now();
        Instant windowStart = windowEnd.minusSeconds(3600);

        aggregateForWindow(MetricAggregation.AggregationWindow.HOUR, windowStart, windowEnd);
    }

    /**
     * Aggregate metrics for a specific window.
     * In production, this would iterate over all services and metrics.
     */
    private void aggregateForWindow(MetricAggregation.AggregationWindow window, Instant windowStart, Instant windowEnd) {
        // This is a simplified implementation
        // In production, you would:
        // 1. Query all registered services
        // 2. For each service, query all active metrics
        // 3. Aggregate each metric separately

        log.debug("Aggregation for window {} from {} to {}", window, windowStart, windowEnd);

        // Placeholder - actual implementation would aggregate all metrics
        // For now, we just log that aggregation ran
    }

    /**
     * Aggregate a single metric for a window.
     */
    public void aggregateMetric(
            String tenantId,
            String serviceName,
            String metricName,
            MetricAggregation.AggregationWindow window,
            Instant windowStart,
            Instant windowEnd
    ) {
        List<MetricDataPoint> dataPoints = metricRepository.findByServiceMetricAndTimeRange(
                tenantId, serviceName, metricName, windowStart, windowEnd
        );

        if (dataPoints.isEmpty()) {
            return;
        }

        DoubleSummaryStatistics stats = dataPoints.stream()
                .mapToDouble(MetricDataPoint::getValue)
                .summaryStatistics();

        List<Double> sortedValues = dataPoints.stream()
                .map(MetricDataPoint::getValue)
                .sorted()
                .collect(Collectors.toList());

        MetricAggregation aggregation = MetricAggregation.builder()
                .tenantId(tenantId)
                .serviceName(serviceName)
                .serviceType(dataPoints.get(0).getServiceType())
                .metricName(metricName)
                .window(window)
                .windowStart(windowStart)
                .windowEnd(windowEnd)
                .count(dataPoints.size())
                .min(stats.getMin())
                .max(stats.getMax())
                .avg(stats.getAverage())
                .sum(stats.getSum())
                .p50(calculatePercentile(sortedValues, 50))
                .p95(calculatePercentile(sortedValues, 95))
                .p99(calculatePercentile(sortedValues, 99))
                .computedAt(Instant.now())
                .build();

        aggregationRepository.save(aggregation);
        log.debug("Saved aggregation for {}:{}:{}", serviceName, metricName, window);
    }

    private Double calculatePercentile(List<Double> sortedValues, int percentile) {
        if (sortedValues.isEmpty()) {
            return null;
        }
        int index = (int) Math.ceil((percentile / 100.0) * sortedValues.size()) - 1;
        return sortedValues.get(Math.max(0, Math.min(index, sortedValues.size() - 1)));
    }

    /**
     * Cleanup old aggregations.
     */
    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 AM daily
    @Transactional
    public void cleanupOldAggregations() {
        log.info("Cleaning up old aggregations");

        // Keep daily aggregations for 90 days, hourly for 30 days, others for 7 days
        Instant dailyCutoff = Instant.now().minusSeconds(90 * 86400);
        Instant hourlyCutoff = Instant.now().minusSeconds(30 * 86400);
        Instant standardCutoff = Instant.now().minusSeconds(7 * 86400);

        long deletedDaily = aggregationRepository.deleteByWindowAndOlderThan(
                MetricAggregation.AggregationWindow.DAY, dailyCutoff
        );

        long deletedHourly = aggregationRepository.deleteByWindowAndOlderThan(
                MetricAggregation.AggregationWindow.HOUR, hourlyCutoff
        );

        long deletedStandard = aggregationRepository.deleteOlderThan(standardCutoff);

        log.info("Deleted {} old aggregations (daily: {}, hourly: {}, standard: {})",
                deletedDaily + deletedHourly + deletedStandard, deletedDaily, deletedHourly, deletedStandard);
    }
}
