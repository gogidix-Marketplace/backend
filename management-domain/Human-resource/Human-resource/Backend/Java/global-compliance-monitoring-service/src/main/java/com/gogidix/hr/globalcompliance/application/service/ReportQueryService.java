package com.gogidix.hr.globalcompliance.application.service;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceReport;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceReportRepository;
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
 * Report Query Service
 * Handles all read operations for compliance reports
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportQueryService {

    private final ComplianceReportRepository reportRepository;

    public ComplianceReport getById(String reportId) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.findByReportIdAndTenantId(reportId, tenantId)
                .orElseThrow(() -> new NotFoundException("ComplianceReport", reportId));
    }

    public ComplianceReport getByNumber(String reportNumber) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.findByReportNumberAndTenantId(reportNumber, tenantId)
                .orElseThrow(() -> new NotFoundException("ComplianceReport", reportNumber));
    }

    public Page<ComplianceReport> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findByTenantId(tenantId);
        return new PageImpl<>(reports, PageRequest.of(0, reports.size()), reports.size());
    }

    public Page<ComplianceReport> getByCountryCode(String countryCode, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getByReportType(String reportType, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findByTenantIdAndReportType(tenantId, reportType);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getByStatus(String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findByTenantIdAndStatus(tenantId, status);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getByPreparedBy(String preparedBy, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findByTenantIdAndPreparedBy(tenantId, preparedBy);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getByApprovedBy(String approvedBy, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findByTenantIdAndApprovedBy(tenantId, approvedBy);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getByPeriod(LocalDate periodStart, LocalDate periodEnd, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findByTenantIdAndPeriodBetween(tenantId, periodStart, periodEnd);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getByDepartment(String department, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findByTenantIdAndDepartment(tenantId, department);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getDraftReports(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findDraftReports(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getSubmittedReports(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findSubmittedReports(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getApprovedReports(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findApprovedReports(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getPublishedReports(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findPublishedReports(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getByComplianceScoreRange(Double minScore, Double maxScore, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findReportsByComplianceScoreRange(tenantId, minScore, maxScore);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getReportsWithCriticalIssues(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findReportsWithCriticalIssues(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(reports, pageRequest, reports.size());
    }

    public Page<ComplianceReport> getRecentReports(int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        List<ComplianceReport> reports = reportRepository.findRecentReports(tenantId, limit);
        return new PageImpl<>(reports, PageRequest.of(0, limit), reports.size());
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public long countByReportType(String reportType) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.countByTenantIdAndReportType(tenantId, reportType);
    }

    public Double getAverageComplianceScore() {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.getAverageComplianceScore(tenantId);
    }
}
