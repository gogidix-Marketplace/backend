package com.gogidix.hr.globalcompliance.application.service;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceCheckRepository;
import com.gogidix.hr.globalcompliance.shared.exception.NotFoundException;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Check Query Service
 * Handles all read operations for compliance checks
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CheckQueryService {

    private final ComplianceCheckRepository checkRepository;

    public ComplianceCheck getById(String checkId) {
        String tenantId = RequestContextHolder.getTenantId();
        return checkRepository.findByCheckIdAndTenantId(checkId, tenantId)
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", checkId));
    }

    public ComplianceCheck getByNumber(String checkNumber) {
        String tenantId = RequestContextHolder.getTenantId();
        return checkRepository.findByCheckNumberAndTenantId(checkNumber, tenantId)
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", checkNumber));
    }

    public Page<ComplianceCheck> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findByTenantId(tenantId);
        return new PageImpl<>(checks, PageRequest.of(0, checks.size()), checks.size());
    }

    public Page<ComplianceCheck> getByRequirementId(String requirementId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findByTenantIdAndRequirementId(tenantId, requirementId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getByCountryCode(String countryCode, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getByStatus(String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findByTenantIdAndStatus(tenantId, status);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getByResult(String result, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findByTenantIdAndResult(tenantId, result);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getOverdueChecks(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findOverdueChecks(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getUpcomingChecks(LocalDate fromDate, LocalDate toDate, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        LocalDate from = fromDate != null ? fromDate : LocalDate.now();
        LocalDate to = toDate != null ? toDate : from.plusWeeks(1);
        List<ComplianceCheck> checks = checkRepository.findUpcomingChecks(tenantId, from, to);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getPendingChecks(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findPendingChecks(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getCompletedChecks(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findCompletedChecks(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getFailedChecks(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findFailedChecks(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getByCheckedBy(String checkedBy, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findByTenantIdAndCheckedBy(tenantId, checkedBy);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getByScheduledDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findByTenantIdAndScheduledDateBetween(tenantId, startDate, endDate);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public Page<ComplianceCheck> getByCompletedDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceCheck> checks = checkRepository.findByTenantIdAndCompletedDateBetween(tenantId, startDate, endDate);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return checkRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        return checkRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public long countByResult(String result) {
        String tenantId = RequestContextHolder.getTenantId();
        return checkRepository.countByTenantIdAndResult(tenantId, result);
    }

    public long countOverdue() {
        String tenantId = RequestContextHolder.getTenantId();
        return checkRepository.countOverdueChecks(tenantId);
    }
}
