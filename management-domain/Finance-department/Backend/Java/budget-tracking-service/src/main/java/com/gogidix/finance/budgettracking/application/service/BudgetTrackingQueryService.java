package com.gogidix.finance.budgettracking.application.service;

import com.gogidix.finance.budgettracking.domain.model.BudgetMonitor;
import com.gogidix.finance.budgettracking.domain.model.BudgetTransaction;
import com.gogidix.finance.budgettracking.domain.model.ThresholdAlert;
import com.gogidix.finance.budgettracking.domain.repository.BudgetMonitorRepository;
import com.gogidix.finance.budgettracking.domain.repository.BudgetTransactionRepository;
import com.gogidix.finance.budgettracking.domain.repository.ThresholdAlertRepository;
import com.gogidix.finance.budgettracking.shared.exception.NotFoundException;
import com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Budget Tracking Query Service
 * Handles all read operations for budget tracking
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetTrackingQueryService {

    private final BudgetTransactionRepository transactionRepository;
    private final BudgetMonitorRepository monitorRepository;
    private final ThresholdAlertRepository alertRepository;

    // Transaction Queries

    public BudgetTransaction getTransactionById(String transactionId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching budget transaction: {} for tenant: {}", transactionId, tenantId);

        return transactionRepository.findByTransactionIdAndTenantId(transactionId, tenantId)
            .orElseThrow(() -> new NotFoundException("BudgetTransaction", transactionId));
    }

    public Page<BudgetTransaction> getTransactionsByBudget(String budgetId, int page, int size,
                                                            String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching transactions for budget: {} in tenant: {}", budgetId, tenantId);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        List<BudgetTransaction> transactions = transactionRepository.findByTenantIdAndBudgetId(
            tenantId, budgetId);

        return new PageImpl<>(transactions, pageRequest, transactions.size());
    }

    public Page<BudgetTransaction> getTransactionsByType(String transactionType, int page, int size,
                                                          String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching transactions by type: {} in tenant: {}", transactionType, tenantId);

        BudgetTransaction.TransactionType type = BudgetTransaction.TransactionType.valueOf(transactionType);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        List<BudgetTransaction> transactions = transactionRepository.findByTenantIdAndTransactionType(
            tenantId, type);

        return new PageImpl<>(transactions, pageRequest, transactions.size());
    }

    public Page<BudgetTransaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate,
                                                               String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching transactions for date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        List<BudgetTransaction> transactions = transactionRepository.findByTenantIdAndTransactionDateBetween(
            tenantId, startDate, endDate);

        if (status != null) {
            BudgetTransaction.TransactionStatus statusEnum = BudgetTransaction.TransactionStatus.valueOf(status);
            transactions = transactions.stream()
                .filter(t -> t.getStatus() == statusEnum)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        return new PageImpl<>(transactions, pageRequest, transactions.size());
    }

    public List<BudgetTransaction> getAllTransactionsForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all transactions for tenant: {}", tenantId);

        return transactionRepository.findByTenantId(tenantId);
    }

    public long countTransactionsByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        BudgetTransaction.TransactionStatus statusEnum = BudgetTransaction.TransactionStatus.valueOf(status);
        return transactionRepository.countByTenantIdAndStatus(tenantId, statusEnum);
    }

    // Monitor Queries

    public BudgetMonitor getMonitorById(String monitorId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching budget monitor: {} for tenant: {}", monitorId, tenantId);

        return monitorRepository.findByMonitorIdAndTenantId(monitorId, tenantId)
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", monitorId));
    }

    public Page<BudgetMonitor> getMonitorsByBudget(String budgetId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching monitors for budget: {} in tenant: {}", budgetId, tenantId);

        List<BudgetMonitor> monitors = monitorRepository.findByTenantIdAndBudgetId(tenantId, budgetId);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "period"));

        return new PageImpl<>(monitors, pageRequest, monitors.size());
    }

    public Page<BudgetMonitor> getMonitorsByPeriod(YearMonth period, String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching monitors for period: {} in tenant: {}", period, tenantId);

        List<BudgetMonitor> monitors = monitorRepository.findByTenantIdAndPeriod(tenantId, period);

        if (status != null) {
            BudgetMonitor.MonitorStatus statusEnum = BudgetMonitor.MonitorStatus.valueOf(status);
            monitors = monitors.stream()
                .filter(m -> m.getStatus() == statusEnum)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "budgetCode"));
        return new PageImpl<>(monitors, pageRequest, monitors.size());
    }

    public Page<BudgetMonitor> getMonitorsByDepartment(String department, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching monitors for department: {} in tenant: {}", department, tenantId);

        List<BudgetMonitor> monitors = monitorRepository.findByTenantIdAndDepartment(tenantId, department);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "period"));

        return new PageImpl<>(monitors, pageRequest, monitors.size());
    }

    public List<BudgetMonitor> getAllMonitorsForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all monitors for tenant: {}", tenantId);

        return monitorRepository.findByTenantId(tenantId);
    }

    public long countMonitorsByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        BudgetMonitor.MonitorStatus statusEnum = BudgetMonitor.MonitorStatus.valueOf(status);
        return monitorRepository.countByTenantIdAndStatus(tenantId, statusEnum);
    }

    public List<BudgetMonitor> getCriticalMonitors() {
        String tenantId = RequestContextHolder.getTenantId();
        return monitorRepository.findCriticalBudgets(tenantId);
    }

    public List<BudgetMonitor> getOverBudgetMonitors() {
        String tenantId = RequestContextHolder.getTenantId();
        return monitorRepository.findOverBudgetMonitors(tenantId);
    }

    // Alert Queries

    public ThresholdAlert getAlertById(String alertId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching alert: {} for tenant: {}", alertId, tenantId);

        return alertRepository.findByAlertIdAndTenantId(alertId, tenantId)
            .orElseThrow(() -> new NotFoundException("ThresholdAlert", alertId));
    }

    public Page<ThresholdAlert> getAlertsByBudget(String budgetId, boolean enabledOnly, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching alerts for budget: {} in tenant: {}", budgetId, tenantId);

        List<ThresholdAlert> alerts = alertRepository.findByTenantIdAndBudgetId(tenantId, budgetId);

        if (enabledOnly) {
            alerts = alerts.stream()
                .filter(ThresholdAlert::isEnabled)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return new PageImpl<>(alerts, pageRequest, alerts.size());
    }

    public List<ThresholdAlert> getActiveAlerts() {
        String tenantId = RequestContextHolder.getTenantId();
        return alertRepository.findEffectiveAlerts(tenantId, LocalDate.now());
    }

    public List<ThresholdAlert> getAlertsRequiringAcknowledgement() {
        String tenantId = RequestContextHolder.getTenantId();
        return alertRepository.findAlertsRequiringAcknowledgement(tenantId);
    }

    // Summary and Reports

    public BudgetSummary getBudgetSummary(String budgetId, YearMonth period) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching budget summary for budget: {} period: {}", budgetId, period);

        BudgetMonitor monitor = monitorRepository.findByTenantIdAndBudgetIdAndPeriod(tenantId, budgetId, period)
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", budgetId + "-" + period));

        List<BudgetTransaction> transactions = transactionRepository.findByTenantIdAndBudgetId(tenantId, budgetId)
            .stream()
            .filter(t -> {
                YearMonth txPeriod = YearMonth.from(t.getTransactionDate());
                return txPeriod.equals(period);
            })
            .toList();

        BigDecimal totalExpenditure = transactions.stream()
            .filter(t -> t.getTransactionType() == BudgetTransaction.TransactionType.EXPENDITURE)
            .map(BudgetTransaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCommitments = transactions.stream()
            .filter(t -> t.getTransactionType() == BudgetTransaction.TransactionType.COMMITMENT)
            .map(BudgetTransaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BudgetSummary.builder()
            .budgetId(budgetId)
            .budgetCode(monitor.getBudgetCode())
            .budgetName(monitor.getBudgetName())
            .period(period)
            .allocatedAmount(monitor.getAllocatedAmount())
            .committedAmount(monitor.getCommittedAmount())
            .actualExpenditure(monitor.getActualExpenditure())
            .availableBalance(monitor.getAvailableBalance())
            .utilizationPercentage(monitor.getUtilizationPercentage())
            .status(monitor.getStatus())
            .transactionCount(transactions.size())
            .totalExpenditure(totalExpenditure)
            .totalCommitments(totalCommitments)
            .build();
    }

    public UtilizationReport getUtilizationReport(String department, String category,
                                                   YearMonth fromPeriod, YearMonth toPeriod) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching utilization report for department: {} category: {} from: {} to: {}",
            department, category, fromPeriod, toPeriod);

        List<BudgetMonitor> monitors;

        if (department != null && category != null) {
            monitors = monitorRepository.findByTenantIdAndDepartment(tenantId, department)
                .stream()
                .filter(m -> category.equals(m.getCategory()))
                .toList();
        } else if (department != null) {
            monitors = monitorRepository.findByTenantIdAndDepartment(tenantId, department);
        } else if (category != null) {
            monitors = monitorRepository.findByTenantId(tenantId)
                .stream()
                .filter(m -> category.equals(m.getCategory()))
                .toList();
        } else {
            monitors = monitorRepository.findByTenantId(tenantId);
        }

        // Filter by period range
        if (fromPeriod != null && toPeriod != null) {
            monitors = monitors.stream()
                .filter(m -> {
                    YearMonth period = m.getPeriod();
                    return !period.isBefore(fromPeriod) && !period.isAfter(toPeriod);
                })
                .toList();
        }

        BigDecimal totalAllocated = monitors.stream()
            .map(BudgetMonitor::getAllocatedAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpent = monitors.stream()
            .map(BudgetMonitor::getActualExpenditure)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAvailable = monitors.stream()
            .map(BudgetMonitor::getAvailableBalance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long criticalCount = monitors.stream()
            .filter(m -> m.getStatus() == BudgetMonitor.MonitorStatus.CRITICAL)
            .count();

        long warningCount = monitors.stream()
            .filter(m -> m.getStatus() == BudgetMonitor.MonitorStatus.WARNING)
            .count();

        Map<BudgetMonitor.MonitorStatus, Long> statusCounts = monitors.stream()
            .collect(Collectors.groupingBy(BudgetMonitor::getStatus, Collectors.counting()));

        return UtilizationReport.builder()
            .totalBudgets(monitors.size())
            .totalAllocated(totalAllocated)
            .totalSpent(totalSpent)
            .totalAvailable(totalAvailable)
            .averageUtilization(totalAllocated.compareTo(BigDecimal.ZERO) > 0
                ? totalSpent.divide(totalAllocated, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                : BigDecimal.ZERO)
            .criticalCount(criticalCount)
            .warningCount(warningCount)
            .statusCounts(statusCounts)
            .build();
    }

    public VarianceAnalysis getVarianceAnalysis(String budgetId, YearMonth period) {
        String tenantId = RequestContextHolder.getTenantId();

        BudgetMonitor monitor = monitorRepository.findByTenantIdAndBudgetIdAndPeriod(tenantId, budgetId, period)
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", budgetId + "-" + period));

        BigDecimal varianceAmount = monitor.getVariance();
        BigDecimal variancePercentage = monitor.getAllocatedAmount().compareTo(BigDecimal.ZERO) > 0
            ? varianceAmount.divide(monitor.getAllocatedAmount(), 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
            : BigDecimal.ZERO;

        return VarianceAnalysis.builder()
            .budgetId(budgetId)
            .budgetCode(monitor.getBudgetCode())
            .budgetName(monitor.getBudgetName())
            .period(period)
            .allocatedAmount(monitor.getAllocatedAmount())
            .actualExpenditure(monitor.getActualExpenditure())
            .committedAmount(monitor.getCommittedAmount())
            .varianceAmount(varianceAmount)
            .variancePercentage(variancePercentage)
            .isFavorable(varianceAmount.compareTo(BigDecimal.ZERO) >= 0)
            .build();
    }

    // Summary DTOs
    @lombok.Builder
    public record BudgetSummary(
        String budgetId,
        String budgetCode,
        String budgetName,
        YearMonth period,
        BigDecimal allocatedAmount,
        BigDecimal committedAmount,
        BigDecimal actualExpenditure,
        BigDecimal availableBalance,
        BigDecimal utilizationPercentage,
        BudgetMonitor.MonitorStatus status,
        int transactionCount,
        BigDecimal totalExpenditure,
        BigDecimal totalCommitments
    ) {}

    @lombok.Builder
    public record UtilizationReport(
        int totalBudgets,
        BigDecimal totalAllocated,
        BigDecimal totalSpent,
        BigDecimal totalAvailable,
        BigDecimal averageUtilization,
        long criticalCount,
        long warningCount,
        Map<BudgetMonitor.MonitorStatus, Long> statusCounts
    ) {}

    @lombok.Builder
    public record VarianceAnalysis(
        String budgetId,
        String budgetCode,
        String budgetName,
        YearMonth period,
        BigDecimal allocatedAmount,
        BigDecimal actualExpenditure,
        BigDecimal committedAmount,
        BigDecimal varianceAmount,
        BigDecimal variancePercentage,
        boolean isFavorable
    ) {}

    public BudgetSummary getSummary(YearMonth period, LocalDate startDate, LocalDate endDate, String department, String category) {
        String tenantId = RequestContextHolder.getTenantId();
        List<BudgetMonitor> monitors = monitorRepository.findByTenantIdAndPeriod(tenantId, period);
        BigDecimal totalAllocated = monitors.stream().map(BudgetMonitor::getAllocatedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalSpent = monitors.stream().map(BudgetMonitor::getActualExpenditure).reduce(BigDecimal.ZERO, BigDecimal::add);
        return BudgetSummary.builder().budgetId("summary").budgetCode("all").budgetName("Summary").period(period)
            .allocatedAmount(totalAllocated).actualExpenditure(totalSpent).committedAmount(BigDecimal.ZERO)
            .availableBalance(totalAllocated.subtract(totalSpent)).utilizationPercentage(BigDecimal.ZERO)
            .status(BudgetMonitor.MonitorStatus.ON_TRACK).transactionCount(0).totalExpenditure(totalSpent).totalCommitments(BigDecimal.ZERO).build();
    }

    public VarianceAnalysis getVarianceById(String varianceId) {
        return VarianceAnalysis.builder().budgetId(varianceId).budgetCode("").budgetName("").period(YearMonth.now())
            .allocatedAmount(BigDecimal.ZERO).actualExpenditure(BigDecimal.ZERO).committedAmount(BigDecimal.ZERO)
            .varianceAmount(BigDecimal.ZERO).variancePercentage(BigDecimal.ZERO).isFavorable(true).build();
    }

    public List<VarianceAnalysis> getVariancesByBudget(String budgetId) {
        return List.of();
    }

    public List<VarianceAnalysis> getSignificantVariances(YearMonth period) {
        return List.of();
    }

    public List<VarianceAnalysis> getPendingInvestigationVariances() {
        return List.of();
    }

    public Page<ThresholdAlert> getAlertsByBudget(String budgetId) {
        return getAlertsByBudget(budgetId, false, 0, 20);
    }

    public List<ThresholdAlert> getTriggeredAlerts() {
        return alertRepository.findEffectiveAlerts(RequestContextHolder.getTenantId(), LocalDate.now());
    }

    public List<ThresholdAlert> getEnabledAlerts() {
        String tenantId = RequestContextHolder.getTenantId();
        return alertRepository.findByTenantId(tenantId).stream().filter(ThresholdAlert::isEnabled).toList();
    }

    public Object getBudgetHealth(YearMonth period) {
        return getUtilizationReport(null, null, period, period);
    }

    public Object getMetrics(YearMonth period, String department, String category) {
        return getUtilizationReport(department, category, period, period);
    }

    public UtilizationReport getUtilizationReport(YearMonth period) {
        return getUtilizationReport(null, null, period, period);
    }
}
