package com.gogidix.sales.analytics.application.service;

import com.gogidix.sales.analytics.domain.model.*;
import com.gogidix.sales.analytics.domain.repository.*;
import com.gogidix.sales.analytics.shared.exception.NotFoundException;
import com.gogidix.sales.analytics.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Query Service for Sales Analytics Metrics
 * Handles all read operations for metrics
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsQueryService {

    private final MetricRepository metricRepository;
    private final PerformanceMetricRepository performanceMetricRepository;
    private final SalesCycleMetricRepository salesCycleMetricRepository;
    private final PipelineMetricRepository pipelineMetricRepository;
    private final WinLossMetricRepository winLossMetricRepository;
    private final AnalyticsReportRepository analyticsReportRepository;
    private final DashboardWidgetRepository dashboardWidgetRepository;

    // ========== Metric Queries ==========

    @Transactional(readOnly = true)
    public Metric getMetric(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return metricRepository.findByMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("Metric", metricId));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "metrics", key = "#tenantId + '_' + #entityType + '_' + #entityId")
    public List<Metric> getMetricsByEntity(String entityType, String entityId) {
        String tenantId = RequestContextHolder.getTenantId();
        return metricRepository.findByTenantIdAndEntityTypeAndEntityId(tenantId, entityType, entityId);
    }

    @Transactional(readOnly = true)
    public List<Metric> getMetricsByType(Metric.MetricType metricType) {
        String tenantId = RequestContextHolder.getTenantId();
        return metricRepository.findByTenantIdAndMetricType(tenantId, metricType);
    }

    @Transactional(readOnly = true)
    public List<Metric> getMetricsByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        return metricRepository.findByTenantIdAndPeriod(tenantId, period);
    }

    @Transactional(readOnly = true)
    public DashboardSummary getDashboardSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        List<Metric> allMetrics = metricRepository.findByTenantId(tenantId);
        List<PerformanceMetric> performanceMetrics = performanceMetricRepository.findByTenantId(tenantId);
        List<PipelineMetric> pipelineMetrics = pipelineMetricRepository.findByTenantId(tenantId);
        List<WinLossMetric> winLossMetrics = winLossMetricRepository.findByTenantId(tenantId);

        // Calculate summary metrics
        BigDecimal totalRevenue = allMetrics.stream()
                .filter(m -> m.getMetricType() == Metric.MetricType.TOTAL_REVENUE)
                .map(Metric::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageWinRate = winLossMetrics.stream()
                .map(WinLossMetric::getWinRate)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(Math.max(1, winLossMetrics.size())), 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal totalPipelineValue = pipelineMetrics.stream()
                .map(PipelineMetric::getTotalPipelineValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int activeDeals = pipelineMetrics.stream()
                .mapToInt(PipelineMetric::getDealsWon)
                .sum();

        int atRiskPipelines = (int) pipelineMetrics.stream()
                .filter(p -> p.getHealth() != PipelineMetric.PipelineHealth.HEALTHY)
                .count();

        return new DashboardSummary(
                totalRevenue,
                averageWinRate,
                totalPipelineValue,
                activeDeals,
                atRiskPipelines,
                allMetrics.size(),
                performanceMetrics.size()
        );
    }

    // ========== Performance Metric Queries ==========

    @Transactional(readOnly = true)
    public PerformanceMetric getPerformanceMetric(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return performanceMetricRepository.findByPerformanceMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("PerformanceMetric", metricId));
    }

    @Transactional(readOnly = true)
    public List<PerformanceMetric> getPerformanceMetricsByEntity(String entityType, String entityId) {
        String tenantId = RequestContextHolder.getTenantId();
        return performanceMetricRepository.findByTenantIdAndEntityTypeAndEntityId(tenantId, entityType, entityId);
    }

    @Transactional(readOnly = true)
    public List<PerformanceMetric> getTopPerformers(String entityType, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        return performanceMetricRepository.findTopPerformersByTenantIdAndEntityType(tenantId, entityType, limit);
    }

    @Transactional(readOnly = true)
    public PerformanceRankings getRankings(String entityType) {
        String tenantId = RequestContextHolder.getTenantId();
        List<PerformanceMetric> ranked = performanceMetricRepository
                .findRankedMetricsByTenantIdAndEntityType(tenantId, entityType);

        List<PerformanceMetric> topPerformers = ranked.stream()
                .filter(m -> m.getQuotaAchievement() != null)
                .sorted(Comparator.comparing(PerformanceMetric::getQuotaAchievement).reversed())
                .limit(10)
                .collect(Collectors.toList());

        List<PerformanceMetric> needsImprovement = ranked.stream()
                .filter(m -> m.getQuotaAchievement() != null && m.getQuotaAchievement().compareTo(BigDecimal.valueOf(80)) < 0)
                .sorted(Comparator.comparing(PerformanceMetric::getQuotaAchievement))
                .limit(10)
                .collect(Collectors.toList());

        return new PerformanceRankings(topPerformers, needsImprovement);
    }

    // ========== Sales Cycle Metric Queries ==========

    @Transactional(readOnly = true)
    public SalesCycleMetric getSalesCycleMetric(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return salesCycleMetricRepository.findBySalesCycleMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("SalesCycleMetric", metricId));
    }

    @Transactional(readOnly = true)
    public List<SalesCycleMetric> getSalesCycleMetricsByEntity(String entityType, String entityId) {
        String tenantId = RequestContextHolder.getTenantId();
        return salesCycleMetricRepository.findByTenantIdAndEntityTypeAndEntityId(tenantId, entityType, entityId);
    }

    @Transactional(readOnly = true)
    public SalesCycleSummary getSalesCycleSummary() {
        String tenantId = RequestContextHolder.getTenantId();
        List<SalesCycleMetric> metrics = salesCycleMetricRepository.findByTenantId(tenantId);

        if (metrics.isEmpty()) {
            return new SalesCycleSummary(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, 0, 0);
        }

        BigDecimal avgCycle = metrics.stream()
                .map(SalesCycleMetric::getAverageCycleDurationDays)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(metrics.size()), 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal medianCycle = metrics.stream()
                .map(SalesCycleMetric::getMedianCycleDurationDays)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(metrics.size()), 2, BigDecimal.ROUND_HALF_UP);

        long slowCycles = metrics.stream()
                .filter(m -> m.getHealthScore() == SalesCycleMetric.CycleHealthScore.CRITICAL ||
                           m.getHealthScore() == SalesCycleMetric.CycleHealthScore.POOR)
                .count();

        long fastCycles = metrics.stream()
                .filter(m -> m.getHealthScore() == SalesCycleMetric.CycleHealthScore.EXCELLENT ||
                           m.getHealthScore() == SalesCycleMetric.CycleHealthScore.GOOD)
                .count();

        return new SalesCycleSummary(avgCycle, medianCycle,
                metrics.get(0).getShortestCycleDays(),
                metrics.get(0).getLongestCycleDays(),
                slowCycles, fastCycles);
    }

    // ========== Pipeline Metric Queries ==========

    @Transactional(readOnly = true)
    public PipelineMetric getPipelineMetric(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return pipelineMetricRepository.findByPipelineMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("PipelineMetric", metricId));
    }

    @Transactional(readOnly = true)
    public List<PipelineMetric> getPipelineMetricsByEntity(String entityType, String entityId) {
        String tenantId = RequestContextHolder.getTenantId();
        return pipelineMetricRepository.findByTenantIdAndEntityTypeAndEntityId(tenantId, entityType, entityId);
    }

    @Transactional(readOnly = true)
    public List<PipelineMetric> getAtRiskPipelines() {
        String tenantId = RequestContextHolder.getTenantId();
        return pipelineMetricRepository.findAtRiskPipelinesByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public PipelineHealthSummary getPipelineHealthSummary() {
        String tenantId = RequestContextHolder.getTenantId();
        List<PipelineMetric> metrics = pipelineMetricRepository.findByTenantId(tenantId);

        if (metrics.isEmpty()) {
            return new PipelineHealthSummary(0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        long healthy = metrics.stream().filter(m -> m.getHealth() == PipelineMetric.PipelineHealth.HEALTHY).count();
        long attentionNeeded = metrics.stream().filter(m -> m.getHealth() == PipelineMetric.PipelineHealth.ATTENTION_NEEDED).count();
        long atRisk = metrics.stream().filter(m -> m.getHealth() == PipelineMetric.PipelineHealth.AT_RISK).count();
        long critical = metrics.stream().filter(m -> m.getHealth() == PipelineMetric.PipelineHealth.CRITICAL).count();

        BigDecimal totalValue = metrics.stream()
                .map(PipelineMetric::getTotalPipelineValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal weightedPipeline = metrics.stream()
                .map(PipelineMetric::getWeightedPipelineValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PipelineHealthSummary(healthy, attentionNeeded, atRisk, critical, totalValue, weightedPipeline);
    }

    // ========== Win/Loss Metric Queries ==========

    @Transactional(readOnly = true)
    public WinLossMetric getWinLossMetric(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return winLossMetricRepository.findByWinLossMetricIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("WinLossMetric", metricId));
    }

    @Transactional(readOnly = true)
    public List<WinLossMetric> getWinLossMetricsByEntity(String entityType, String entityId) {
        String tenantId = RequestContextHolder.getTenantId();
        return winLossMetricRepository.findByTenantIdAndEntityTypeAndEntityId(tenantId, entityType, entityId);
    }

    @Transactional(readOnly = true)
    public List<WinLossMetric> getLowestPerformers(String entityType, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        return winLossMetricRepository.findLowestPerformersByTenantIdAndEntityType(tenantId, entityType, limit);
    }

    @Transactional(readOnly = true)
    public List<WinLossMetric> getHighestPerformers(String entityType, int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        return winLossMetricRepository.findHighestPerformersByTenantIdAndEntityType(tenantId, entityType, limit);
    }

    @Transactional(readOnly = true)
    public WinLossAnalysisSummary getWinLossAnalysisSummary() {
        String tenantId = RequestContextHolder.getTenantId();
        List<WinLossMetric> metrics = winLossMetricRepository.findByTenantId(tenantId);

        if (metrics.isEmpty()) {
            return new WinLossAnalysisSummary(BigDecimal.ZERO, BigDecimal.ZERO,
                    0, 0, BigDecimal.ZERO, null, null);
        }

        BigDecimal avgWinRate = metrics.stream()
                .map(WinLossMetric::getWinRate)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(metrics.size()), 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal avgLossRate = metrics.stream()
                .map(WinLossMetric::getLossRate)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(metrics.size()), 2, BigDecimal.ROUND_HALF_UP);

        int totalWon = metrics.stream().mapToInt(WinLossMetric::getDealsWon).sum();
        int totalLost = metrics.stream().mapToInt(WinLossMetric::getDealsLost).sum();

        String primaryLossReason = metrics.stream()
                .map(WinLossMetric::getPrimaryLossReason)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        String topCompetitor = metrics.stream()
                .map(WinLossMetric::getTopCompetitor)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        return new WinLossAnalysisSummary(avgWinRate, avgLossRate, totalWon, totalLost,
                avgWinRate, primaryLossReason, topCompetitor);
    }

    // ========== Record Classes for DTOs ==========

    public record DashboardSummary(
            BigDecimal totalRevenue,
            BigDecimal averageWinRate,
            BigDecimal totalPipelineValue,
            int activeDeals,
            int atRiskPipelines,
            int totalMetrics,
            int performanceMetrics
    ) {}

    public record PerformanceRankings(
            List<PerformanceMetric> topPerformers,
            List<PerformanceMetric> needsImprovement
    ) {}

    public record SalesCycleSummary(
            BigDecimal averageCycleDays,
            BigDecimal medianCycleDays,
            BigDecimal shortestCycleDays,
            BigDecimal longestCycleDays,
            long slowCycles,
            long fastCycles
    ) {}

    public record PipelineHealthSummary(
            long healthy,
            long attentionNeeded,
            long atRisk,
            long critical,
            BigDecimal totalValue,
            BigDecimal weightedValue
    ) {}

    public record WinLossAnalysisSummary(
            BigDecimal averageWinRate,
            BigDecimal averageLossRate,
            int totalDealsWon,
            int totalDealsLost,
            BigDecimal overallWinRate,
            String primaryLossReason,
            String topCompetitor
    ) {}
}
