package com.gogidix.finance.budgettracking.domain.repository;

import com.gogidix.finance.budgettracking.domain.model.BudgetMonitor;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Budget Monitor Repository Interface (Port)
 * Defines the contract for budget monitor persistence operations
 */
public interface BudgetMonitorRepository {

    BudgetMonitor save(BudgetMonitor monitor);

    List<BudgetMonitor> saveAll(List<BudgetMonitor> monitors);

    Optional<BudgetMonitor> findById(String id);

    Optional<BudgetMonitor> findByMonitorIdAndTenantId(String monitorId, String tenantId);

    List<BudgetMonitor> findByTenantId(String tenantId);

    List<BudgetMonitor> findByTenantIdAndBudgetId(String tenantId, String budgetId);

    List<BudgetMonitor> findByTenantIdAndBudgetCode(String tenantId, String budgetCode);

    List<BudgetMonitor> findByTenantIdAndPeriod(String tenantId, YearMonth period);

    List<BudgetMonitor> findByTenantIdAndStatus(String tenantId, BudgetMonitor.MonitorStatus status);

    List<BudgetMonitor> findByTenantIdAndDepartment(String tenantId, String department);

    List<BudgetMonitor> findByTenantIdAndCategory(String tenantId, String category);

    List<BudgetMonitor> findByTenantIdAndPeriodBetween(String tenantId, YearMonth startPeriod, YearMonth endPeriod);

    List<BudgetMonitor> findByTenantIdAndThresholdBreached(String tenantId, boolean breached);

    List<BudgetMonitor> findByTenantIdAndStatusIn(String tenantId, List<BudgetMonitor.MonitorStatus> statuses);

    Optional<BudgetMonitor> findByTenantIdAndBudgetIdAndPeriod(String tenantId, String budgetId, YearMonth period);

    boolean existsByMonitorIdAndTenantId(String monitorId, String tenantId);

    boolean existsByTenantIdAndBudgetIdAndPeriod(String tenantId, String budgetId, YearMonth period);

    void deleteById(String id);

    void deleteByMonitorIdAndTenantId(String monitorId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, BudgetMonitor.MonitorStatus status);

    List<BudgetMonitor> findCriticalBudgets(String tenantId);

    List<BudgetMonitor> findOverBudgetMonitors(String tenantId);
}
