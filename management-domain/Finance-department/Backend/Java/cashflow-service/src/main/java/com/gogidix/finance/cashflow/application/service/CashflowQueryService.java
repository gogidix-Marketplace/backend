package com.gogidix.finance.cashflow.application.service;

import com.gogidix.finance.cashflow.domain.model.CashflowForecast;
import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.domain.model.CashflowStatement;
import com.gogidix.finance.cashflow.domain.repository.CashflowForecastRepository;
import com.gogidix.finance.cashflow.domain.repository.CashflowItemRepository;
import com.gogidix.finance.cashflow.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cashflow Query Service
 * Handles all read operations for cashflow data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CashflowQueryService {

    private final CashflowItemRepository cashflowItemRepository;
    private final CashflowForecastRepository cashflowForecastRepository;

    // Cashflow Item Queries

    public CashflowItem getCashflowItemById(String cashflowItemId) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching cashflow item: {} for tenant: {}", cashflowItemId, tenantId);

        return cashflowItemRepository.findByCashflowItemIdAndTenantId(cashflowItemId, tenantId)
                .orElseThrow(() -> new NotFoundException("CashflowItem", cashflowItemId));
    }

    public Page<CashflowItem> getCashflowItemsByDateRange(LocalDate startDate, LocalDate endDate,
                                                           CashflowItem.CashflowType type,
                                                           List<CashflowItem.ItemStatus> statuses,
                                                           int page, int size) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching cashflow items for date range: {} to {} in tenant: {}",
                startDate, endDate, tenantId);

        List<CashflowItem> items = cashflowItemRepository.findByTenantIdAndTransactionDateBetween(
                tenantId, startDate, endDate);

        if (type != null) {
            items = items.stream()
                    .filter(item -> item.getType() == type)
                    .collect(Collectors.toList());
        }

        if (statuses != null && !statuses.isEmpty()) {
            items = items.stream()
                    .filter(item -> statuses.contains(item.getStatus()))
                    .collect(Collectors.toList());
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        return new PageImpl<>(items, pageRequest, items.size());
    }

    public Page<CashflowItem> getCashflowItemsByType(CashflowItem.CashflowType type,
                                                      LocalDate startDate, LocalDate endDate,
                                                      int page, int size) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching cashflow items for type: {} in tenant: {}", type, tenantId);

        List<CashflowItem> items = cashflowItemRepository.findByTenantIdAndType(tenantId, type);

        if (startDate != null && endDate != null) {
            items = items.stream()
                    .filter(item -> !item.getTransactionDate().isBefore(startDate) &&
                            !item.getTransactionDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        return new PageImpl<>(items, pageRequest, items.size());
    }

    public Page<CashflowItem> getCashflowItemsByCategory(CashflowItem.CashflowCategory category,
                                                          LocalDate startDate, LocalDate endDate,
                                                          int page, int size) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching cashflow items for category: {} in tenant: {}", category, tenantId);

        List<CashflowItem> items = cashflowItemRepository.findByTenantIdAndCategory(tenantId, category);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        return new PageImpl<>(items, pageRequest, items.size());
    }

    public Page<CashflowItem> getCashflowItemsByStatus(CashflowItem.ItemStatus status, int page, int size) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching cashflow items for status: {} in tenant: {}", status, tenantId);

        List<CashflowItem> items = cashflowItemRepository.findByTenantIdAndStatus(tenantId, status);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        return new PageImpl<>(items, pageRequest, items.size());
    }

    public Page<CashflowItem> getRecurringCashflowItems(Boolean recurring, int page, int size) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching recurring cashflow items for tenant: {}", tenantId);

        List<CashflowItem> items = cashflowItemRepository.findByTenantIdAndRecurringTrue(tenantId);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        return new PageImpl<>(items, pageRequest, items.size());
    }

    public List<CashflowItem> getAllCashflowItemsForTenant() {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching all cashflow items for tenant: {}", tenantId);

        return cashflowItemRepository.findByTenantId(tenantId);
    }

    public List<CashflowItem> getPendingCashflowItems(LocalDate dueDate) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching pending cashflow items for tenant: {}", tenantId);

        LocalDate targetDate = dueDate != null ? dueDate : LocalDate.now();

        return cashflowItemRepository.findByTenantIdAndStatusAndExpectedDateBefore(
                tenantId, CashflowItem.ItemStatus.EXPECTED, targetDate);
    }

    // Cashflow Forecast Queries

    public CashflowForecast getForecastById(String forecastId) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching forecast: {} for tenant: {}", forecastId, tenantId);

        return cashflowForecastRepository.findByForecastIdAndTenantId(forecastId, tenantId)
                .orElseThrow(() -> new NotFoundException("CashflowForecast", forecastId));
    }

    public Page<CashflowForecast> getForecastsByDateRange(LocalDate startDate, LocalDate endDate,
                                                           CashflowForecast.ForecastScenario scenario,
                                                           int page, int size) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for date range: {} to {} in tenant: {}",
                startDate, endDate, tenantId);

        List<CashflowForecast> forecasts = cashflowForecastRepository.findByTenantIdAndDateRange(
                tenantId, startDate, endDate);

        if (scenario != null) {
            forecasts = forecasts.stream()
                    .filter(f -> f.getScenario() == scenario)
                    .collect(Collectors.toList());
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    public Page<CashflowForecast> getForecastsByScenario(CashflowForecast.ForecastScenario scenario,
                                                          int page, int size) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for scenario: {} in tenant: {}", scenario, tenantId);

        List<CashflowForecast> forecasts = cashflowForecastRepository.findByTenantIdAndScenario(
                tenantId, scenario);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    public Page<CashflowForecast> getForecastsByStatus(CashflowForecast.ForecastStatus status, int page, int size) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching forecasts for status: {} in tenant: {}", status, tenantId);

        List<CashflowForecast> forecasts = cashflowForecastRepository.findByTenantIdAndStatus(
                tenantId, status);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return new PageImpl<>(forecasts, pageRequest, forecasts.size());
    }

    public List<CashflowForecast> getAllForecastsForTenant() {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching all forecasts for tenant: {}", tenantId);

        return cashflowForecastRepository.findByTenantId(tenantId);
    }

    // Summary Queries

    public CashflowSummary getCashflowSummary(LocalDate startDate, LocalDate endDate,
                                               String costCenter, String project) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching cashflow summary for tenant: {}", tenantId);

        List<CashflowItem> items = getCashflowItemsForSummary(tenantId, startDate, endDate, costCenter, project);

        BigDecimal totalInflow = items.stream()
                .filter(item -> item.getType() == CashflowItem.CashflowType.INFLOW)
                .map(CashflowItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOutflow = items.stream()
                .filter(item -> item.getType() == CashflowItem.CashflowType.OUTFLOW)
                .map(CashflowItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netCashflow = totalInflow.subtract(totalOutflow);

        long pendingCount = items.stream()
                .filter(item -> item.getStatus() == CashflowItem.ItemStatus.PENDING)
                .count();

        long settledCount = items.stream()
                .filter(item -> item.getStatus() == CashflowItem.ItemStatus.SETTLED)
                .count();

        return CashflowSummary.builder()
                .totalCount(items.size())
                .totalInflow(totalInflow)
                .totalOutflow(totalOutflow)
                .netCashflow(netCashflow)
                .pendingCount(pendingCount)
                .settledCount(settledCount)
                .build();
    }

    public CashflowPosition getCashflowPosition(LocalDate asOfDate) {
        String tenantId = com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching cashflow position for tenant: {}", tenantId);

        LocalDate targetDate = asOfDate != null ? asOfDate : LocalDate.now();

        List<CashflowItem> settledItems = cashflowItemRepository.findByTenantIdAndSettledDateBetween(
                tenantId, LocalDate.ofEpochDay(0), targetDate);

        BigDecimal settledInflow = settledItems.stream()
                .filter(item -> item.getType() == CashflowItem.CashflowType.INFLOW)
                .map(CashflowItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal settledOutflow = settledItems.stream()
                .filter(item -> item.getType() == CashflowItem.CashflowType.OUTFLOW)
                .map(CashflowItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CashflowItem> pendingItems = cashflowItemRepository.findByTenantIdAndStatusAndExpectedDateBefore(
                tenantId, CashflowItem.ItemStatus.EXPECTED, targetDate);

        BigDecimal pendingInflow = pendingItems.stream()
                .filter(item -> item.getType() == CashflowItem.CashflowType.INFLOW)
                .map(CashflowItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pendingOutflow = pendingItems.stream()
                .filter(item -> item.getType() == CashflowItem.CashflowType.OUTFLOW)
                .map(CashflowItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CashflowPosition.builder()
                .asOfDate(targetDate)
                .settledInflow(settledInflow)
                .settledOutflow(settledOutflow)
                .pendingInflow(pendingInflow)
                .pendingOutflow(pendingOutflow)
                .totalInflow(settledInflow.add(pendingInflow))
                .totalOutflow(settledOutflow.add(pendingOutflow))
                .netPosition(settledInflow.subtract(settledOutflow))
                .projectedNetPosition(settledInflow.add(pendingInflow).subtract(settledOutflow).subtract(pendingOutflow))
                .build();
    }

    private List<CashflowItem> getCashflowItemsForSummary(String tenantId, LocalDate startDate,
                                                           LocalDate endDate, String costCenter, String project) {
        List<CashflowItem> items;

        if (startDate != null && endDate != null) {
            items = cashflowItemRepository.findByTenantIdAndTransactionDateBetween(
                    tenantId, startDate, endDate);
        } else {
            items = cashflowItemRepository.findByTenantId(tenantId);
        }

        if (costCenter != null) {
            items = items.stream()
                    .filter(item -> costCenter.equals(item.getCostCenter()))
                    .collect(Collectors.toList());
        }

        if (project != null) {
            items = items.stream()
                    .filter(item -> project.equals(item.getProjectId()))
                    .collect(Collectors.toList());
        }

        return items;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CashflowSummary {
        private long totalCount;
        private BigDecimal totalInflow;
        private BigDecimal totalOutflow;
        private BigDecimal netCashflow;
        private long pendingCount;
        private long settledCount;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CashflowPosition {
        private LocalDate asOfDate;
        private BigDecimal settledInflow;
        private BigDecimal settledOutflow;
        private BigDecimal pendingInflow;
        private BigDecimal pendingOutflow;
        private BigDecimal totalInflow;
        private BigDecimal totalOutflow;
        private BigDecimal netPosition;
        private BigDecimal projectedNetPosition;
    }
}
