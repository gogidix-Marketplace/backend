package com.gogidix.hr.globalcompliance.application.service;

import com.gogidix.hr.globalcompliance.domain.model.AuditTrail;
import com.gogidix.hr.globalcompliance.domain.model.ComplianceReport;
import com.gogidix.hr.globalcompliance.domain.port.in.ReportCommand;
import com.gogidix.hr.globalcompliance.domain.port.out.EventPublisher;
import com.gogidix.hr.globalcompliance.domain.repository.AuditTrailRepository;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceReportRepository;
import com.gogidix.hr.globalcompliance.shared.exception.NotFoundException;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Report Command Service
 * Handles all write operations for compliance reports
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportCommandService {

    private final ComplianceReportRepository reportRepository;
    private final AuditTrailRepository auditTrailRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public ComplianceReport create(ReportCommand.CreateReportCommand command) {
        log.info("Creating report: {} for tenant: {}", command.getReportType(), command.getTenantId());

        ComplianceReport report = ComplianceReport.create(
                command.getTenantId(),
                command.getReportType(),
                command.getCountryCode(),
                command.getPeriodStart(),
                command.getPeriodEnd(),
                command.getPreparedBy(),
                command.getPreparedByName(),
                command.getDepartment()
        );

        if (command.getRegion() != null) {
            report.setRegion(command.getRegion());
        }

        ComplianceReport savedReport = reportRepository.save(report);

        createAuditTrail(savedReport.getTenantId(), savedReport.getReportId(),
                "ComplianceReport", savedReport.getReportId(),
                "CREATED", null, "Report created", command.getPreparedBy());

        log.info("Created report: {}", savedReport.getReportId());
        return savedReport;
    }

    @Transactional
    public void setMetrics(ReportCommand.SetMetricsCommand command) {
        log.info("Setting metrics for report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.setMetrics(command.getTotalRequirements(), command.getPassedChecks(),
                command.getFailedChecks(), command.getPendingChecks());
        reportRepository.save(report);

        createAuditTrail(report.getTenantId(), report.getReportId(),
                "ComplianceReport", report.getReportId(),
                "UPDATED", null, "Metrics updated, score=" + report.getComplianceScore(),
                RequestContextHolder.getUserId());

        log.info("Set metrics for report: {}", command.getReportId());
    }

    @Transactional
    public void addCriticalIssue(ReportCommand.AddCriticalIssueCommand command) {
        log.info("Adding critical issue to report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.addCriticalIssue(command.getCriticalIssue());
        reportRepository.save(report);

        log.info("Added critical issue to report: {}", command.getReportId());
    }

    @Transactional
    public void addRecommendation(ReportCommand.AddRecommendationCommand command) {
        log.info("Adding recommendation to report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.addRecommendation(command.getRecommendation());
        reportRepository.save(report);

        log.info("Added recommendation to report: {}", command.getReportId());
    }

    @Transactional
    public void setSummary(ReportCommand.SetSummaryCommand command) {
        log.info("Setting summary for report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.setSummary(command.getSummary());
        reportRepository.save(report);

        createAuditTrail(report.getTenantId(), report.getReportId(),
                "ComplianceReport", report.getReportId(),
                "UPDATED", null, "Summary set", RequestContextHolder.getUserId());

        log.info("Set summary for report: {}", command.getReportId());
    }

    @Transactional
    public void includeRequirement(ReportCommand.IncludeRequirementCommand command) {
        log.info("Including requirement in report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.includeRequirement(command.getRequirementId());
        reportRepository.save(report);

        log.info("Included requirement in report: {}", command.getReportId());
    }

    @Transactional
    public void includeCheck(ReportCommand.IncludeCheckCommand command) {
        log.info("Including check in report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.includeCheck(command.getCheckId());
        reportRepository.save(report);

        log.info("Included check in report: {}", command.getReportId());
    }

    @Transactional
    public void submit(ReportCommand.SubmitReportCommand command) {
        log.info("Submitting report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.submit();
        reportRepository.save(report);

        createAuditTrail(report.getTenantId(), report.getReportId(),
                "ComplianceReport", report.getReportId(),
                "SUBMITTED", "status=DRAFT", "status=SUBMITTED",
                RequestContextHolder.getUserId());

        log.info("Submitted report: {}", command.getReportId());
    }

    @Transactional
    public void approve(ReportCommand.ApproveReportCommand command) {
        log.info("Approving report: {} by {}", command.getReportId(), command.getApprovedBy());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.approve(command.getApprovedBy(), command.getApprovedByName());
        reportRepository.save(report);

        createAuditTrail(report.getTenantId(), report.getReportId(),
                "ComplianceReport", report.getReportId(),
                "APPROVED", "status=SUBMITTED", "status=APPROVED", command.getApprovedBy());

        publishReportEvents(report);
        log.info("Approved report: {}", command.getReportId());
    }

    @Transactional
    public void reject(ReportCommand.RejectReportCommand command) {
        log.info("Rejecting report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.reject(command.getRejectionReason());
        reportRepository.save(report);

        createAuditTrail(report.getTenantId(), report.getReportId(),
                "ComplianceReport", report.getReportId(),
                "REJECTED", "status=SUBMITTED", "status=REJECTED, reason=" + command.getRejectionReason(),
                RequestContextHolder.getUserId());

        log.info("Rejected report: {}", command.getReportId());
    }

    @Transactional
    public void publish(ReportCommand.PublishReportCommand command) {
        log.info("Publishing report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.publish();
        reportRepository.save(report);

        createAuditTrail(report.getTenantId(), report.getReportId(),
                "ComplianceReport", report.getReportId(),
                "PUBLISHED", "status=APPROVED", "status=PUBLISHED",
                RequestContextHolder.getUserId());

        log.info("Published report: {}", command.getReportId());
    }

    @Transactional
    public void returnToDraft(ReportCommand.ReturnToDraftCommand command) {
        log.info("Returning report to draft: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        String previousStatus = report.getStatus();
        report.returnToDraft(command.getReason());
        reportRepository.save(report);

        createAuditTrail(report.getTenantId(), report.getReportId(),
                "ComplianceReport", report.getReportId(),
                "UPDATED", "status=" + previousStatus, "status=DRAFT",
                RequestContextHolder.getUserId());

        log.info("Returned report to draft: {}", command.getReportId());
    }

    @Transactional
    public void updateNotes(ReportCommand.UpdateNotesCommand command) {
        log.info("Updating notes for report: {}", command.getReportId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        report.updateNotes(command.getNotes());
        reportRepository.save(report);

        log.info("Updated notes for report: {}", command.getReportId());
    }

    @Transactional
    public void delete(ReportCommand.DeleteReportCommand command) {
        log.info("Deleting report: {} for tenant: {}", command.getReportId(), command.getTenantId());

        ComplianceReport report = reportRepository.findByReportIdAndTenantId(
                command.getReportId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceReport", command.getReportId()));

        String reportId = report.getReportId();
        String tenantId = report.getTenantId();

        reportRepository.deleteByReportIdAndTenantId(command.getReportId(), command.getTenantId());
        auditTrailRepository.deleteByReportId(reportId);

        createAuditTrail(tenantId, reportId, "ComplianceReport", reportId,
                "DELETED", null, null, RequestContextHolder.getUserId());

        log.info("Deleted report: {}", command.getReportId());
    }

    private void publishReportEvents(ComplianceReport report) {
        if (!report.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            for (var event : report.getDomainEvents()) {
                eventPublisher.publishReportEvent(event);
            }
            report.clearDomainEvents();
        }
    }

    private void createAuditTrail(String tenantId, String entityId, String entityType,
                                   String reportId, String action, String previousValue,
                                   String newValue, String actionedBy) {
        AuditTrail audit = AuditTrail.forReport(tenantId, reportId, action,
                actionedBy, actionedBy, previousValue, newValue, null,
                RequestContextHolder.getOptional().map(ctx -> ctx.getIpAddress()).orElse(null),
                RequestContextHolder.getOptional().map(ctx -> ctx.getCountryCode()).orElse(null));

        auditTrailRepository.save(audit);
    }
}
