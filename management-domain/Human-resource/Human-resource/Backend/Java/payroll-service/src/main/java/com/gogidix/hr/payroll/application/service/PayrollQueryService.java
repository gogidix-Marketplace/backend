package com.gogidix.hr.payroll.application.service;

import com.gogidix.hr.payroll.domain.model.Payroll;
import com.gogidix.hr.payroll.domain.enums.PayrollStatus;
import com.gogidix.hr.payroll.domain.repository.PayrollRepository;
import com.gogidix.hr.payroll.shared.exception.PayrollNotFoundException;
import com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Payroll Query Service
 * Handles all read operations for payroll
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollQueryService {

    private final PayrollRepository payrollRepository;

    public Payroll getById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findById(id)
                .filter(p -> p.getTenantId().equals(tenantId))
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", id));
    }

    public Payroll getByPayrollId(String payrollId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findByPayrollIdAndTenantId(payrollId, tenantId)
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", payrollId));
    }

    public List<Payroll> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findByTenantId(tenantId);
    }

    public Page<Payroll> getAllForTenant(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<Payroll> payrolls = payrollRepository.findByTenantId(tenantId);
        return paginateList(payrolls, page, size);
    }

    public List<Payroll> getByCountryCode(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
    }

    public Page<Payroll> getByCountryCode(String countryCode, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<Payroll> payrolls = payrollRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
        return paginateList(payrolls, page, size);
    }

    public List<Payroll> getByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        PayrollStatus payrollStatus = PayrollStatus.valueOf(status.toUpperCase());
        return payrollRepository.findByTenantIdAndStatus(tenantId, payrollStatus);
    }

    public Page<Payroll> getByStatus(String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        PayrollStatus payrollStatus = PayrollStatus.valueOf(status.toUpperCase());
        List<Payroll> payrolls = payrollRepository.findByTenantIdAndStatus(tenantId, payrollStatus);
        return paginateList(payrolls, page, size);
    }

    public List<Payroll> getByPeriod(YearMonth period) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findByTenantIdAndPayrollPeriod(tenantId, period)
                .map(List::of)
                .orElse(List.of());
    }

    public List<Payroll> getByDateRange(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findByTenantIdAndStartDateBetween(tenantId, startDate, endDate);
    }

    public List<Payroll> getByPaymentDateRange(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findByTenantIdAndPaymentDateBetween(tenantId, startDate, endDate);
    }

    public Page<Payroll> getPendingApproval(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<Payroll> payrolls = payrollRepository.findPendingApproval(tenantId, PageRequest.of(page, size));
        return paginateList(payrolls, page, size);
    }

    public Page<Payroll> getApprovedPayrolls(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<Payroll> payrolls = payrollRepository.findApprovedPayrolls(tenantId, PageRequest.of(page, size));
        return paginateList(payrolls, page, size);
    }

    public Page<Payroll> getProcessedPayrolls(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<Payroll> payrolls = payrollRepository.findProcessedPayrolls(tenantId, PageRequest.of(page, size));
        return paginateList(payrolls, page, size);
    }

    public Page<Payroll> getPaidPayrolls(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<Payroll> payrolls = payrollRepository.findPaidPayrolls(tenantId, PageRequest.of(page, size));
        return paginateList(payrolls, page, size);
    }

    public List<Payroll> getByEmployeeId(String employeeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findByEmployeeId(tenantId, employeeId);
    }

    public List<Payroll> getByDepartment(String department) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findByDepartment(tenantId, department);
    }

    public List<Payroll> getActivePayrolls() {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findActivePayrolls(tenantId);
    }

    public List<Payroll> getUnlockedPayrolls() {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findUnlockedPayrolls(tenantId);
    }

    public List<Payroll> getByRunType(String runType) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findByRunType(tenantId, runType);
    }

    public Page<Payroll> search(String searchTerm, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.searchPayrolls(tenantId, searchTerm, PageRequest.of(page, size));
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public Payroll getLatestPayroll() {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findLatestPayroll(tenantId);
    }

    public List<Payroll> getPayrollsRequiringAction() {
        String tenantId = RequestContextHolder.getTenantId();
        return payrollRepository.findPayrollsRequiringAction(tenantId);
    }

    private <T> Page<T> paginateList(List<T> list, int page, int size) {
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min(start + size, list.size());
        List<T> paginatedList = start < list.size() ? list.subList(start, end) : List.of();
        return new PageImpl<>(paginatedList, PageRequest.of(page, size), list.size());
    }
}
