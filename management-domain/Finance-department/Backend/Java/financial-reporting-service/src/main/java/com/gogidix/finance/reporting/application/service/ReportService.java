package com.gogidix.finance.reporting.application.service;

import com.gogidix.finance.reporting.domain.event.ReportGeneratedEvent;
import com.gogidix.finance.reporting.domain.model.Report;
import com.gogidix.finance.reporting.domain.port.out.EventPublisher;
import com.gogidix.finance.reporting.domain.repository.ReportRepository;
import com.gogidix.finance.reporting.shared.exception.NotFoundException;
import com.gogidix.finance.reporting.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Report Service
 * Handles report generation and management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Report createReport(String name, Report.ReportType reportType,
                               Report.ReportFormat format, LocalDate reportDate,
                               Map<String, Object> parameters) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();

        log.info("Creating report: {} of type: {} for tenant: {}", name, reportType, tenantId);

        Report report = Report.create(
            tenantId, name, reportType, format, reportDate, userId, parameters);

        Report saved = reportRepository.save(report);
        publishEvents(saved);

        // Start async generation
        startGenerationAsync(saved.getReportId());

        return saved;
    }

    @Async
    public void generateReport(String reportId) {
        Report report = reportRepository.findByReportIdAndTenantId(
            reportId, RequestContextHolder.getTenantId())
            .orElseThrow(() -> new NotFoundException("Report", reportId));

        try {
            report.startGeneration();
            reportRepository.save(report);

            // Simulate report generation progress
            for (int i = 10; i <= 90; i += 10) {
                Thread.sleep(100);
                report.updateProgress(i);
                reportRepository.save(report);
            }

            // Complete report
            String fileUrl = generateFileUrl(report);
            report.complete(fileUrl, 1024L * 100, 500);
            reportRepository.save(report);
            publishEvents(report);

            log.info("Report generation completed: {}", reportId);

        } catch (InterruptedException e) {
            report.fail("Generation interrupted");
            reportRepository.save(report);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            report.fail(e.getMessage());
            reportRepository.save(report);
            log.error("Report generation failed: {}", reportId, e);
        }
    }

    public Report getReport(String reportId) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.findByReportIdAndTenantId(reportId, tenantId)
            .orElseThrow(() -> new NotFoundException("Report", reportId));
    }

    public List<Report> getAllReports() {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.findByTenantId(tenantId);
    }

    public List<Report> getReportsByStatus(Report.ReportStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<Report> getReportsByType(Report.ReportType type) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.findByTenantIdAndReportType(tenantId, type);
    }

    @Transactional
    public void cancelReport(String reportId) {
        Report report = getReport(reportId);
        report.cancel();
        reportRepository.save(report);
        publishEvents(report);
        log.info("Report cancelled: {}", reportId);
    }

    @Transactional
    public void deleteReport(String reportId) {
        String tenantId = RequestContextHolder.getTenantId();
        reportRepository.deleteByReportIdAndTenantId(reportId, tenantId);
        log.info("Report deleted: {}", reportId);
    }

    private void startGenerationAsync(String reportId) {
        // This would trigger the async generation
        log.debug("Starting async generation for report: {}", reportId);
    }

    private String generateFileUrl(Report report) {
        return String.format("reports/%s/%s.%s",
            report.getTenantId(),
            report.getReportId(),
            report.getFormat().name().toLowerCase());
    }

    private void publishEvents(Report report) {
        if (!report.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll((List<Object>) (List<?>) report.getDomainEvents());
            report.clearDomainEvents();
        }
    }
}
