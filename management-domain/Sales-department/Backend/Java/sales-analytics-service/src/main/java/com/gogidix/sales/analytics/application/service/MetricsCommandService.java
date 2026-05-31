package com.gogidix.sales.analytics.application.service;

import com.gogidix.sales.analytics.domain.model.*;
import com.gogidix.sales.analytics.domain.repository.*;
import com.gogidix.sales.analytics.domain.event.*;
import com.gogidix.sales.analytics.infrastructure.messaging.KafkaEventPublisher;
import com.gogidix.sales.analytics.shared.exception.NotFoundException;
import com.gogidix.sales.analytics.shared.exception.ValidationException;
import com.gogidix.sales.analytics.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Command Service for Sales Analytics Metrics
 * Handles all write operations for metrics
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsCommandService {

    private final MetricRepository metricRepository;
    private final PerformanceMetricRepository performanceMetricRepository;
    private final SalesCycleMetricRepository salesCycleMetricRepository;
    private final PipelineMetricRepository pipelineMetricRepository;
    private final WinLossMetricRepository winLossMetricRepository;
    private final AnalyticsReportRepository analyticsReportRepository;
    private final DashboardWidgetRepository dashboardWidgetRepository;
    private final KafkaEventPublisher eventPublisher;

    // ========== Metric Operations ==========

    @Transactional
    public Metric createMetric(String entityType, String entityId,
                              Metric.MetricType metricType, BigDecimal value,
                              String period, Instant periodStart, Instant periodEnd) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();

        log.info("Creating metric for tenant: {}, entity: {}/{}", tenantId, entityType, entityId);

        Metric metric = Metric.create(tenantId, entityType, entityId, metricType, value,
                period, periodStart, periodEnd, userId);
        metric = metricRepository.save(metric);

        // Publish domain event
        eventPublisher.publishMetricUpdated(metric.getDomainEvents().isEmpty() ?
                MetricUpdatedEvent.create(metric.getMetricId(), tenantId, metricType.name(),
                        entityType, entityId, value, period, periodStart, periodEnd,
                        metric.getMetadata(), "METRIC_CREATED") :
                metric.getDomainEvents().get(0));

        metric.clearDomainEvents();
        return metric;
    }

    @Transactional
    public Metric updateMetricValue(String metricId, BigDecimal newValue) {
        String tenantId = RequestContextHolder.getTenantId();

        Metric metric = metricRepository.findByMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("Metric", metricId));

        metric.updateValue(newValue);
        metric = metricRepository.save(metric);

        // Publish domain events
        for (MetricUpdatedEvent event : metric.getDomainEvents()) {
            eventPublisher.publishMetricUpdated(event);
        }
        metric.clearDomainEvents();

        return metric;
    }

    @Transactional
    public void deleteMetric(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();

        if (!metricRepository.existsByMetricIdAndTenantId(metricId, tenantId)) {
            throw new NotFoundException("Metric", metricId);
        }

        metricRepository.deleteByMetricIdAndTenantId(metricId, tenantId);
        log.info("Deleted metric: {} for tenant: {}", metricId, tenantId);
    }

    // ========== Performance Metric Operations ==========

    @Transactional
    public PerformanceMetric createPerformanceMetric(String entityType, String entityId, String entityName,
                                                     PerformanceMetric.PerformancePeriod period,
                                                     LocalDate periodStart, LocalDate periodEnd) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();

        PerformanceMetric metric = PerformanceMetric.create(tenantId, entityType, entityId,
                entityName, period, periodStart, periodEnd, userId);

        metric = performanceMetricRepository.save(metric);

        // Publish event
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("entityType", entityType);
        metadata.put("entityName", entityName);
        metadata.put("period", period.name());

        eventPublisher.publishSalesPerformanceUpdated(
                SalesPerformanceUpdatedEvent.create(tenantId, entityType, entityId, entityName,
                        periodStart.atStartOfDay(ZoneOffset.UTC).toInstant(),
                        periodEnd.atStartOfDay(ZoneOffset.UTC).toInstant(),
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0,
                        metadata, userId, "PERFORMANCE_METRIC_CREATED")
        );

        return metric;
    }

    @Transactional
    public PerformanceMetric updatePerformanceRevenue(String metricId, BigDecimal totalRevenue, BigDecimal targetRevenue) {
        String tenantId = RequestContextHolder.getTenantId();

        PerformanceMetric metric = performanceMetricRepository.findByPerformanceMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("PerformanceMetric", metricId));

        metric.updateRevenueMetrics(totalRevenue, targetRevenue);
        metric.markCompleted();
        metric = performanceMetricRepository.save(metric);

        // Publish event
        eventPublisher.publishSalesPerformanceUpdated(
                SalesPerformanceUpdatedEvent.create(tenantId, metric.getEntityType(),
                        metric.getEntityId(), metric.getEntityName(),
                        metric.getPeriodStartDate().atStartOfDay(ZoneOffset.UTC).toInstant(),
                        metric.getPeriodEndDate().atStartOfDay(ZoneOffset.UTC).toInstant(),
                        totalRevenue, metric.getQuotaAchievement(),
                        metric.getWinRate(), metric.getDealsWon(),
                        metric.getKpis(), RequestContextHolder.getUserId(),
                        "PERFORMANCE_REVENUE_UPDATED")
        );

        return metric;
    }

    @Transactional
    public PerformanceMetric updatePerformanceDeals(String metricId, Integer dealsWon, Integer dealsLost) {
        String tenantId = RequestContextHolder.getTenantId();

        PerformanceMetric metric = performanceMetricRepository.findByPerformanceMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("PerformanceMetric", metricId));

        metric.updateDealMetrics(dealsWon, dealsLost);
        metric.updateAverageDealSize(metric.getTotalRevenue(), dealsWon);
        metric.markCompleted();
        metric = performanceMetricRepository.save(metric);

        return metric;
    }

    // ========== Sales Cycle Metric Operations ==========

    @Transactional
    public SalesCycleMetric createSalesCycleMetric(String entityType, String entityId, String entityName,
                                                   SalesCycleMetric.MetricPeriod period,
                                                   LocalDate periodStart, LocalDate periodEnd) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();

        SalesCycleMetric metric = SalesCycleMetric.create(tenantId, entityType, entityId,
                entityName, period, periodStart, periodEnd, userId);

        return salesCycleMetricRepository.save(metric);
    }

    @Transactional
    public SalesCycleMetric updateSalesCycleDuration(String metricId, BigDecimal average, BigDecimal median,
                                                     BigDecimal shortest, BigDecimal longest) {
        String tenantId = RequestContextHolder.getTenantId();

        SalesCycleMetric metric = salesCycleMetricRepository.findBySalesCycleMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("SalesCycleMetric", metricId));

        metric.updateCycleDuration(average, median, shortest, longest);
        return salesCycleMetricRepository.save(metric);
    }

    // ========== Pipeline Metric Operations ==========

    @Transactional
    public PipelineMetric createPipelineMetric(String entityType, String entityId, String entityName,
                                              PipelineMetric.MetricPeriod period,
                                              LocalDate periodStart, LocalDate periodEnd) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();

        PipelineMetric metric = PipelineMetric.create(tenantId, entityType, entityId,
                entityName, period, periodStart, periodEnd, userId);

        metric = pipelineMetricRepository.save(metric);

        // Publish event
        Map<String, Object> metadata = new HashMap<>();
        eventPublisher.publishPipelineUpdated(
                PipelineUpdatedEvent.create(tenantId, entityType, entityId, entityName,
                        periodStart.atStartOfDay(ZoneOffset.UTC).toInstant(),
                        periodEnd.atStartOfDay(ZoneOffset.UTC).toInstant(),
                        BigDecimal.ZERO, BigDecimal.ZERO, 0,
                        BigDecimal.ZERO, PipelineMetric.PipelineHealth.HEALTHY.name(),
                        100, metadata)
        );

        return metric;
    }

    @Transactional
    public PipelineMetric updatePipelineValue(String metricId, BigDecimal totalValue, BigDecimal openValue,
                                             BigDecimal wonValue, BigDecimal lostValue, BigDecimal stagnantValue) {
        String tenantId = RequestContextHolder.getTenantId();

        PipelineMetric metric = pipelineMetricRepository.findByPipelineMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("PipelineMetric", metricId));

        metric.updatePipelineValue(totalValue, openValue, wonValue, lostValue, stagnantValue);
        metric = pipelineMetricRepository.save(metric);

        // Publish event
        eventPublisher.publishPipelineUpdated(
                PipelineUpdatedEvent.create(tenantId, metric.getEntityType(),
                        metric.getEntityId(), metric.getEntityName(),
                        metric.getPeriodStartDate().atStartOfDay(ZoneOffset.UTC).toInstant(),
                        metric.getPeriodEndDate().atStartOfDay(ZoneOffset.UTC).toInstant(),
                        totalValue, metric.getPipelineVelocity(),
                        metric.getDealsWon(), metric.getPipelineCoverage(),
                        metric.getHealth().name(), metric.getHealthScore(),
                        new HashMap<>())
        );

        return metric;
    }

    @Transactional
    public PipelineMetric updatePipelineHealth(String metricId, Integer targetCoverage) {
        String tenantId = RequestContextHolder.getTenantId();

        PipelineMetric metric = pipelineMetricRepository.findByPipelineMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("PipelineMetric", metricId));

        metric.calculateHealthScore(BigDecimal.valueOf(targetCoverage));
        return pipelineMetricRepository.save(metric);
    }

    // ========== Win/Loss Metric Operations ==========

    @Transactional
    public WinLossMetric createWinLossMetric(String entityType, String entityId, String entityName,
                                            WinLossMetric.MetricPeriod period,
                                            LocalDate periodStart, LocalDate periodEnd) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();

        WinLossMetric metric = WinLossMetric.create(tenantId, entityType, entityId,
                entityName, period, periodStart, periodEnd, userId);

        metric = winLossMetricRepository.save(metric);

        // Publish event
        Map<String, Object> metadata = new HashMap<>();
        eventPublisher.publishWinLossAnalysis(
                WinLossAnalysisEvent.create(tenantId, entityType, entityId, entityName,
                        periodStart.atStartOfDay(ZoneOffset.UTC).toInstant(),
                        periodEnd.atStartOfDay(ZoneOffset.UTC).toInstant(),
                        BigDecimal.ZERO, BigDecimal.ZERO, 0, 0, null, null,
                        BigDecimal.ZERO, metadata)
        );

        return metric;
    }

    @Transactional
    public WinLossMetric updateWinLossCounts(String metricId, Integer dealsWon, Integer dealsLost, Integer inProgress) {
        String tenantId = RequestContextHolder.getTenantId();

        WinLossMetric metric = winLossMetricRepository.findByWinLossMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("WinLossMetric", metricId));

        metric.updateWinLossCounts(dealsWon, dealsLost, inProgress);
        metric = winLossMetricRepository.save(metric);

        // Publish event
        eventPublisher.publishWinLossAnalysis(
                WinLossAnalysisEvent.create(tenantId, metric.getEntityType(),
                        metric.getEntityId(), metric.getEntityName(),
                        metric.getPeriodStartDate().atStartOfDay(ZoneOffset.UTC).toInstant(),
                        metric.getPeriodEndDate().atStartOfDay(ZoneOffset.UTC).toInstant(),
                        metric.getWinRate(), metric.getLossRate(),
                        dealsWon, dealsLost, metric.getPrimaryLossReason(),
                        metric.getTopCompetitor(), metric.getWinRateTrend(),
                        new HashMap<>())
        );

        return metric;
    }

    @Transactional
    public WinLossMetric addLossReason(String metricId, String reason, Integer count, BigDecimal value) {
        String tenantId = RequestContextHolder.getTenantId();

        WinLossMetric metric = winLossMetricRepository.findByWinLossMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("WinLossMetric", metricId));

        metric.addLossReason(reason, count, value);
        return winLossMetricRepository.save(metric);
    }
}
