package com.gogidix.analytics.bi.application.service;

import com.gogidix.analytics.bi.domain.model.ReportDefinition;
import com.gogidix.analytics.bi.domain.model.ReportExecution;
import com.gogidix.analytics.bi.domain.port.in.CreateReportCommand;
import com.gogidix.analytics.bi.domain.port.in.ExecuteReportQuery;
import com.gogidix.analytics.bi.domain.port.out.ReportNotificationGateway;
import com.gogidix.analytics.bi.domain.repository.ReportDefinitionRepository;
import com.gogidix.analytics.bi.domain.repository.ReportExecutionRepository;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportCommandService {

    private final ReportDefinitionRepository reportRepository;
    private final ReportExecutionRepository executionRepository;
    private final ReportNotificationGateway notificationGateway;
    private final AuditService auditService;

    @Transactional
    public ReportDefinition createReport(CreateReportCommand command) {
        log.info("Creating report definition: name={}", command.getReportName());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        ReportDefinition report = ReportDefinition.builder()
            .reportName(command.getReportName())
            .description(command.getDescription())
            .reportType(command.getReportType())
            .scheduleType(command.getScheduleType())
            .scheduleConfig(command.getScheduleConfig())
            .dataSource(command.getDataSource())
            .queryDefinition(command.getQueryDefinition())
            .outputFormat(command.getOutputFormat())
            .templateConfig(command.getTemplateConfig())
            .recipients(command.getRecipients())
            .ownerId(command.getOwnerId())
            .tenantId(tenantId)
            .enabled(command.getEnabled())
            .build();

        if (command.getScheduleType() != null && command.getEnabled()) {
            report.setNextRunAt(calculateNextRunTime(command.getScheduleType()));
        }

        ReportDefinition savedReport = reportRepository.save(report);

        auditService.logEvent(
            "REPORT_DEFINITION_CREATED",
            "ReportDefinition",
            savedReport.getId(),
            "Created report definition: " + savedReport.getReportName()
        );

        log.info("Report definition created: id={}, name={}", savedReport.getId(), savedReport.getReportName());
        return savedReport;
    }

    @Transactional
    public ReportDefinition updateReport(String reportId, CreateReportCommand command) {
        log.info("Updating report definition: id={}", reportId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        ReportDefinition report = reportRepository.findById(reportId)
            .orElseThrow(() -> new NotFoundException("Report not found: " + reportId));

        if (!report.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Report belongs to different tenant");
        }

        report.setReportName(command.getReportName());
        report.setDescription(command.getDescription());
        report.setReportType(command.getReportType());
        report.setScheduleType(command.getScheduleType());
        report.setScheduleConfig(command.getScheduleConfig());
        report.setDataSource(command.getDataSource());
        report.setQueryDefinition(command.getQueryDefinition());
        report.setOutputFormat(command.getOutputFormat());
        report.setTemplateConfig(command.getTemplateConfig());
        report.setRecipients(command.getRecipients());
        report.setEnabled(command.getEnabled());

        if (command.getScheduleType() != null && command.getEnabled()) {
            report.setNextRunAt(calculateNextRunTime(command.getScheduleType()));
        }

        ReportDefinition savedReport = reportRepository.save(report);

        auditService.logEvent(
            "REPORT_DEFINITION_UPDATED",
            "ReportDefinition",
            savedReport.getId(),
            "Updated report definition: " + savedReport.getReportName()
        );

        log.info("Report definition updated: id={}", reportId);
        return savedReport;
    }

    @Transactional
    public void deleteReport(String reportId) {
        log.info("Deleting report definition: id={}", reportId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        ReportDefinition report = reportRepository.findById(reportId)
            .orElseThrow(() -> new NotFoundException("Report not found: " + reportId));

        if (!report.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Report belongs to different tenant");
        }

        reportRepository.delete(report);

        auditService.logEvent(
            "REPORT_DEFINITION_DELETED",
            "ReportDefinition",
            reportId,
            "Deleted report definition: " + report.getReportName()
        );

        log.info("Report definition deleted: id={}", reportId);
    }

    @Transactional
    public ReportExecution executeReport(ExecuteReportQuery query) {
        log.info("Executing report: reportId={}, async={}", query.getReportId(), query.getAsync());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        ReportDefinition report = reportRepository.findById(query.getReportId())
            .orElseThrow(() -> new NotFoundException("Report not found: " + query.getReportId()));

        if (!report.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Report belongs to different tenant");
        }

        ReportExecution execution = ReportExecution.builder()
            .reportDefinition(report)
            .status(ReportExecution.ExecutionStatus.PENDING)
            .requestedBy(query.getRequestedBy())
            .build();

        ReportExecution savedExecution = executionRepository.save(execution);

        if (query.getAsync()) {
            executeReportAsync(savedExecution.getId(), query);
        } else {
            executeReportSync(savedExecution, query);
        }

        log.info("Report execution created: executionId={}, reportId={}", savedExecution.getId(), query.getReportId());
        return savedExecution;
    }

    @Transactional
    public void cancelExecution(String executionId) {
        log.info("Cancelling report execution: executionId={}", executionId);

        ReportExecution execution = executionRepository.findById(executionId)
            .orElseThrow(() -> new NotFoundException("Execution not found: " + executionId));

        if (execution.getStatus() == ReportExecution.ExecutionStatus.PENDING ||
            execution.getStatus() == ReportExecution.ExecutionStatus.RUNNING) {

            execution.setStatus(ReportExecution.ExecutionStatus.CANCELLED);
            execution.setCompletedAt(LocalDateTime.now());
            executionRepository.save(execution);

            auditService.logEvent(
                "REPORT_EXECUTION_CANCELLED",
                "ReportExecution",
                executionId,
                "Cancelled report execution"
            );

            log.info("Report execution cancelled: executionId={}", executionId);
        }
    }

    @Transactional
    public ReportDefinition toggleReport(String reportId, boolean enabled) {
        log.info("{} report: reportId={}", enabled ? "Enabling" : "Disabling", reportId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        ReportDefinition report = reportRepository.findById(reportId)
            .orElseThrow(() -> new NotFoundException("Report not found: " + reportId));

        if (!report.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Report belongs to different tenant");
        }

        report.setEnabled(enabled);
        if (enabled && report.getScheduleType() != null) {
            report.setNextRunAt(calculateNextRunTime(report.getScheduleType()));
        } else {
            report.setNextRunAt(null);
        }

        ReportDefinition savedReport = reportRepository.save(report);

        auditService.logEvent(
            "REPORT_DEFINITION_TOGGLED",
            "ReportDefinition",
            reportId,
            (enabled ? "Enabled" : "Disabled") + " report: " + report.getReportName()
        );

        log.info("Report {}: reportId={}", enabled ? "enabled" : "disabled", reportId);
        return savedReport;
    }

    @Async
    protected void executeReportAsync(String executionId, ExecuteReportQuery query) {
        try {
            Thread.sleep(100);
            ReportExecution execution = executionRepository.findById(executionId).orElse(null);
            if (execution != null) {
                executeReportSync(execution, query);
            }
        } catch (Exception e) {
            log.error("Failed to execute report asynchronously: executionId={}", executionId, e);
        }
    }

    private void executeReportSync(ReportExecution execution, ExecuteReportQuery query) {
        execution.markAsRunning();
        executionRepository.save(execution);

        try {
            Thread.sleep(1000);

            String filePath = "/reports/" + execution.getId() + "." +
                (query.getOutputFormat() != null ? query.getOutputFormat().name().toLowerCase() : "pdf");

            execution.markAsCompleted(filePath, 1024L, 100);
            executionRepository.save(execution);

            ReportDefinition report = execution.getReportDefinition();
            report.setLastRunAt(LocalDateTime.now());
            if (report.getScheduleType() != null) {
                report.setNextRunAt(calculateNextRunTime(report.getScheduleType()));
            }
            reportRepository.save(report);

            notificationGateway.sendReportReadyNotification(execution);

            log.info("Report execution completed: executionId={}", execution.getId());

        } catch (Exception e) {
            execution.markAsFailed(e.getMessage());
            executionRepository.save(execution);

            notificationGateway.sendReportFailedNotification(execution, e.getMessage());

            log.error("Report execution failed: executionId={}", execution.getId(), e);
        }
    }

    private LocalDateTime calculateNextRunTime(ReportDefinition.ScheduleType scheduleType) {
        LocalDateTime now = LocalDateTime.now();
        return switch (scheduleType) {
            case HOURLY -> now.plusHours(1);
            case DAILY -> now.plusDays(1);
            case WEEKLY -> now.plusWeeks(1);
            case MONTHLY -> now.plusMonths(1);
            case QUARTERLY -> now.plusMonths(3);
            case YEARLY -> now.plusYears(1);
            default -> null;
        };
    }
}
