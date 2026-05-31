package com.gogidix.hr.payroll.application.service;

import com.gogidix.hr.payroll.domain.model.PayrollEntry;
import com.gogidix.hr.payroll.domain.repository.PayrollEntryRepository;
import com.gogidix.hr.payroll.shared.exception.PayrollNotFoundException;
import com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Payroll Entry Query Service
 * Handles all read operations for payroll entries
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollEntryQueryService {

    private final PayrollEntryRepository payrollEntryRepository;

    public PayrollEntry getById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollEntryRepository.findById(id)
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElseThrow(() -> new PayrollNotFoundException("PayrollEntry", id));
    }

    public PayrollEntry getByEntryId(String entryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollEntryRepository.findByEntryIdAndTenantId(entryId, tenantId)
                .orElseThrow(() -> new PayrollNotFoundException("PayrollEntry", entryId));
    }

    public List<PayrollEntry> getByPayrollId(String payrollId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollEntryRepository.findByPayrollIdAndTenantId(payrollId, tenantId);
    }

    public Page<PayrollEntry> getByPayrollId(String payrollId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollIdAndTenantId(payrollId, tenantId);
        return paginateList(entries, page, size);
    }

    public List<PayrollEntry> getByEmployeeId(String employeeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollEntryRepository.findByTenantIdAndEmployeeId(tenantId, employeeId);
    }

    public Page<PayrollEntry> getByEmployeeId(String employeeId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<PayrollEntry> entries = payrollEntryRepository.findByTenantIdAndEmployeeId(tenantId, employeeId);
        return paginateList(entries, page, size);
    }

    public List<PayrollEntry> getByPayrollIdAndEmployeeId(String payrollId, String employeeId) {
        return payrollEntryRepository.findByPayrollIdAndEmployeeId(payrollId, employeeId);
    }

    public List<PayrollEntry> getByDepartment(String department) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollEntryRepository.findByTenantIdAndDepartment(tenantId, department);
    }

    public List<PayrollEntry> getUnpaidEntries(String payrollId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollEntryRepository.findUnpaidEntries(tenantId, payrollId);
    }

    public List<PayrollEntry> getHeldEntries(String payrollId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollEntryRepository.findHeldEntries(tenantId, payrollId);
    }

    public List<PayrollEntry> getEntriesWithIssues(String payrollId) {
        return payrollEntryRepository.findEntriesWithIssues(payrollId);
    }

    public BigDecimal getTotalGrossPay(String payrollId) {
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollId(payrollId);
        return entries.stream()
                .map(PayrollEntry::getGrossPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalNetPay(String payrollId) {
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollId(payrollId);
        return entries.stream()
                .map(PayrollEntry::getNetPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalTax(String payrollId) {
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollId(payrollId);
        return entries.stream()
                .map(PayrollEntry::getTotalTax)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long countByPayrollId(String payrollId) {
        return payrollEntryRepository.countByPayrollId(payrollId);
    }

    public long countByEmployeeId(String employeeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollEntryRepository.countByTenantIdAndEmployeeId(tenantId, employeeId);
    }

    public Page<PayrollEntry> search(String searchTerm, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollEntryRepository.searchEntries(tenantId, searchTerm, PageRequest.of(page, size));
    }

    private <T> Page<T> paginateList(List<T> list, int page, int size) {
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min(start + size, list.size());
        List<T> paginatedList = start < list.size() ? list.subList(start, end) : List.of();
        return new PageImpl<>(paginatedList, PageRequest.of(page, size), list.size());
    }
}
