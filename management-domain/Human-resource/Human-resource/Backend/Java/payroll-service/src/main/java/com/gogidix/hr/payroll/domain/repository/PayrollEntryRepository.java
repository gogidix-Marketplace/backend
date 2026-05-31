package com.gogidix.hr.payroll.domain.repository;

import com.gogidix.hr.payroll.domain.model.PayrollEntry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for PayrollEntry
 */
public interface PayrollEntryRepository {

    PayrollEntry save(PayrollEntry entry);

    Optional<PayrollEntry> findByEntryIdAndTenantId(String entryId, String tenantId);

    List<PayrollEntry> saveAll(List<PayrollEntry> entries);

    Optional<PayrollEntry> findById(String id);

    List<PayrollEntry> findByPayrollId(String payrollId);

    List<PayrollEntry> findByEmployeeId(String employeeId);

    List<PayrollEntry> findByTenantId(String tenantId);

    List<PayrollEntry> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<PayrollEntry> findByPayrollIdAndEmployeeId(String payrollId, String employeeId);

    List<PayrollEntry> findUnpaidEntries(String payrollId);

    List<PayrollEntry> findPaidEntries(String payrollId);

    List<PayrollEntry> findEntriesOnHold(String payrollId);

    List<PayrollEntry> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    List<PayrollEntry> findByEmployeeIdAndDateRange(String employeeId, LocalDate startDate, LocalDate endDate);

    void deleteById(String id);

    void deleteByPayrollId(String payrollId);

    boolean existsByPayrollIdAndEmployeeId(String payrollId, String employeeId);

    List<PayrollEntry> findByBankAccountNumber(String accountNumber);

    List<PayrollEntry> findRecentEntries(String employeeId, int limit);

    List<PayrollEntry> findByPayrollIdAndTenantId(String payrollId, String tenantId);

    List<PayrollEntry> findByTenantIdAndEmployeeId(String tenantId, String employeeId);

    List<PayrollEntry> findByTenantIdAndDepartment(String tenantId, String department);

    List<PayrollEntry> findUnpaidEntries(String tenantId, String payrollId);

    List<PayrollEntry> findHeldEntries(String tenantId, String employeeId);

    List<PayrollEntry> findEntriesWithIssues(String tenantId);

    long countByPayrollId(String payrollId);

    long countByTenantIdAndEmployeeId(String tenantId, String employeeId);

    Page<PayrollEntry> searchEntries(String tenantId, String searchTerm, PageRequest pageRequest);

    void deleteByEntryIdAndTenantId(String entryId, String tenantId);
}
