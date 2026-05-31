package com.gogidix.hr.payroll.domain.repository;

import com.gogidix.hr.payroll.domain.model.Payroll;
import com.gogidix.hr.payroll.domain.enums.PayrollStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Payroll aggregate
 */
public interface PayrollRepository {

    Payroll save(Payroll payroll);

    Optional<Payroll> findById(String id);

    List<Payroll> findByTenantId(String tenantId);

    List<Payroll> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<Payroll> findByTenantIdAndStatus(String tenantId, PayrollStatus status);

    Optional<Payroll> findByTenantIdAndPayrollPeriod(String tenantId, YearMonth period);

    List<Payroll> findByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Payroll> findByStatus(PayrollStatus status);

    List<Payroll> findByBatchId(String batchId);

    List<Payroll> findPendingApprovalPayrolls(String tenantId);

    List<Payroll> findProcessedPayrollsPendingPayment(String tenantId);

    List<Payroll> findActivePayrollsByTenant(String tenantId);

    List<Payroll> findUnlockedPayrolls(String tenantId);

    List<Payroll> findByRunType(String tenantId, String runType);

    void deleteById(String id);

    boolean existsByTenantIdAndPayrollPeriod(String tenantId, YearMonth period);

    List<Payroll> findByEmployeeId(String employeeId);

    List<Payroll> findRecentPayrolls(String tenantId, int limit);

    Optional<Payroll> findByPayrollIdAndTenantId(String payrollId, String tenantId);

    void deleteByPayrollIdAndTenantId(String payrollId, String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, String status);

    Page<Payroll> searchPayrolls(String tenantId, String searchTerm, Pageable pageable);

    boolean existsByPayrollPeriodAndTenantId(YearMonth period, String tenantId);

    List<Payroll> findByCountryCode(String countryCode);

    List<Payroll> findByBatchIdAndTenantId(String batchId, String tenantId);

    List<Payroll> findByTenantIdAndPaymentDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    Payroll findLatestPayroll(String tenantId);

    List<Payroll> findPayrollsRequiringAction(String tenantId);

    void deleteAllByTenantId(String tenantId);

    List<Payroll> findByTenantIdAndStartDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Payroll> findPendingApproval(String tenantId, org.springframework.data.domain.PageRequest pageRequest);

    List<Payroll> findApprovedPayrolls(String tenantId, org.springframework.data.domain.PageRequest pageRequest);

    List<Payroll> findProcessedPayrolls(String tenantId, org.springframework.data.domain.PageRequest pageRequest);

    List<Payroll> findPaidPayrolls(String tenantId, org.springframework.data.domain.PageRequest pageRequest);

    List<Payroll> findByEmployeeId(String employeeId, String tenantId);

    List<Payroll> findByDepartment(String department, String tenantId);

    List<Payroll> findByStatus(String status, String tenantId);

    List<Payroll> findActivePayrolls(String tenantId);
}
