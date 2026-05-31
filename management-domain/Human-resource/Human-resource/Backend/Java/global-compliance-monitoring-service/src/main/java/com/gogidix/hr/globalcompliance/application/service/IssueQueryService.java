package com.gogidix.hr.globalcompliance.application.service;

import com.gogidix.hr.globalcompliance.domain.model.NonComplianceIssue;
import com.gogidix.hr.globalcompliance.domain.repository.NonComplianceIssueRepository;
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
 * Issue Query Service
 * Handles all read operations for non-compliance issues
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IssueQueryService {

    private final NonComplianceIssueRepository issueRepository;

    public NonComplianceIssue getById(String issueId) {
        String tenantId = RequestContextHolder.getTenantId();
        return issueRepository.findByIssueIdAndTenantId(issueId, tenantId)
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", issueId));
    }

    public NonComplianceIssue getByNumber(String issueNumber) {
        String tenantId = RequestContextHolder.getTenantId();
        return issueRepository.findByIssueNumberAndTenantId(issueNumber, tenantId)
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", issueNumber));
    }

    public Page<NonComplianceIssue> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantId(tenantId);
        return new PageImpl<>(issues, PageRequest.of(0, issues.size()), issues.size());
    }

    public Page<NonComplianceIssue> getByRequirementId(String requirementId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndRequirementId(tenantId, requirementId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getByCheckId(String checkId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndCheckId(tenantId, checkId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getByCountryCode(String countryCode, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getBySeverity(String severity, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndSeverity(tenantId, severity);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getByStatus(String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndStatus(tenantId, status);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getOpenIssues(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findOpenIssues(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getOverdueIssues(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findOverdueIssues(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getCriticalIssues(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findCriticalIssues(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getByAssignedTo(String assignedTo, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndAssignedTo(tenantId, assignedTo);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getByDepartment(String department, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndDepartment(tenantId, department);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getByIdentifiedBy(String identifiedBy, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndIdentifiedBy(tenantId, identifiedBy);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getByIdentifiedDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndIdentifiedDateBetween(tenantId, startDate, endDate);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getByDueDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findByTenantIdAndDueDateBetween(tenantId, startDate, endDate);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getByAffectedEmployee(String employeeId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findIssuesByAffectedEmployee(tenantId, employeeId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getResolvedBetweenDates(LocalDate startDate, LocalDate endDate, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findResolvedIssuesBetweenDates(tenantId, startDate, endDate);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> search(String searchTerm, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.searchByTitleOrDescription(tenantId, searchTerm);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public Page<NonComplianceIssue> getIssuesRequiringAttention(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<NonComplianceIssue> issues = issueRepository.findIssuesRequiringAttention(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(issues, pageRequest, issues.size());
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return issueRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        return issueRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public long countBySeverity(String severity) {
        String tenantId = RequestContextHolder.getTenantId();
        return issueRepository.countByTenantIdAndSeverity(tenantId, severity);
    }

    public long countOpenIssues() {
        String tenantId = RequestContextHolder.getTenantId();
        return issueRepository.countOpenIssues(tenantId);
    }

    public long countOverdueIssues() {
        String tenantId = RequestContextHolder.getTenantId();
        return issueRepository.countOverdueIssues(tenantId);
    }
}
