package com.gogidix.finance.globalfinancedashboard.application.service;

import com.gogidix.finance.globalfinancedashboard.domain.model.FinancialMetric;
import com.gogidix.finance.globalfinancedashboard.domain.repository.FinancialMetricRepository;
import com.gogidix.finance.globalfinancedashboard.shared.exception.NotFoundException;
import com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException;
import com.gogidix.finance.globalfinancedashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Application Service for Financial Dashboard Operations
 * Handles business logic for financial metrics and dashboard data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialDashboardService {

    private final FinancialMetricRepository metricRepository;

    /**
     * Create a new financial metric
     */
    @Transactional
    public FinancialMetric createMetric(FinancialMetric.MetricType metricType,
                                        BigDecimal amount,
                                        String currency,
                                        String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Creating financial metric for tenant: {}, type: {}", tenantId, metricType);

        FinancialMetric metric = new FinancialMetric(tenantId, metricType, amount, currency, period);
        return metricRepository.save(metric);
    }

    /**
     * Get metric by ID
     */
    @Transactional(readOnly = true)
    public FinancialMetric getMetric(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        return metricRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new NotFoundException("FinancialMetric", id));
    }

    /**
     * Get all metrics for current tenant
     */
    @Transactional(readOnly = true)
    public List<FinancialMetric> getAllMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        return metricRepository.findByTenantId(tenantId);
    }

    /**
     * Get metrics by type
     */
    @Transactional(readOnly = true)
    public List<FinancialMetric> getMetricsByType(FinancialMetric.MetricType metricType) {
        String tenantId = RequestContextHolder.getTenantId();
        return metricRepository.findByTenantIdAndMetricType(tenantId, metricType);
    }

    /**
     * Get metrics for a specific period
     */
    @Transactional(readOnly = true)
    public List<FinancialMetric> getMetricsByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        return metricRepository.findByTenantIdAndRegionAndPeriod(tenantId, null, period);
    }

    /**
     * Update metric amount
     */
    @Transactional
    public FinancialMetric updateMetricAmount(String id, BigDecimal newAmount) {
        String tenantId = RequestContextHolder.getTenantId();
        FinancialMetric metric = metricRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new NotFoundException("FinancialMetric", id));

        metric.updateAmount(newAmount);
        return metricRepository.save(metric);
    }

    /**
     * Delete metric
     */
    @Transactional
    public void deleteMetric(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        if (!metricRepository.existsByIdAndTenantId(id, tenantId)) {
            throw new NotFoundException("FinancialMetric", id);
        }
        metricRepository.deleteByIdAndTenantId(id, tenantId);
        log.info("Deleted financial metric: {} for tenant: {}", id, tenantId);
    }

    /**
     * Get summary statistics for dashboard
     */
    @Transactional(readOnly = true)
    public DashboardSummary getDashboardSummary() {
        String tenantId = RequestContextHolder.getTenantId();
        List<FinancialMetric> allMetrics = metricRepository.findByTenantId(tenantId);

        BigDecimal totalRevenue = allMetrics.stream()
                .filter(m -> m.getMetricType() == FinancialMetric.MetricType.REVENUE)
                .map(FinancialMetric::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = allMetrics.stream()
                .filter(m -> m.getMetricType() == FinancialMetric.MetricType.EXPENSE)
                .map(FinancialMetric::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profit = totalRevenue.subtract(totalExpenses);

        return new DashboardSummary(totalRevenue, totalExpenses, profit, allMetrics.size());
    }

    public record DashboardSummary(
        BigDecimal totalRevenue,
        BigDecimal totalExpenses,
        BigDecimal profit,
        int metricCount
    ) {}
}
