package com.gogidix.sales.analytics.application.service;

import com.gogidix.sales.analytics.domain.model.AnalyticsReport;
import com.gogidix.sales.analytics.domain.repository.AnalyticsReportRepository;
import com.gogidix.sales.analytics.infrastructure.messaging.KafkaEventPublisher;
import com.gogidix.sales.analytics.shared.exception.NotFoundException;
import com.gogidix.sales.analytics.shared.exception.ValidationException;
import com.gogidix.sales.analytics.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service for Analytics Report Operations
 * Handles report generation, scheduling, and retrieval
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final AnalyticsReportRepository reportRepository;
    private final KafkaEventPublisher eventPublisher;

    @Transactional
    public AnalyticsReport createReport(String name, AnalyticsReport.ReportType reportType,
                                       String description, Instant startDate, Instant endDate,
                                       Map<String, Object> filters, AnalyticsReport.ReportFormat format) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();

        AnalyticsReport report = AnalyticsReport.create(
                tenantId, name, reportType, description, startDate, endDate,
                userId, filters, format
        );

        report.validateDateRange();
        report = reportRepository.save(report);

        // Publish report created event
        eventPublisher.publishReportGenerated(report.getDomainEvents().isEmpty() ?
                com.gogidix.sales.analytics.domain.event.ReportGeneratedEvent.create(
                        report.getReportId(), tenantId, reportType.name(), userId,
                        startDate, endDate, new HashMap<>(), "REPORT_CREATED"
                ) : report.getDomainEvents().get(0));

        report.clearDomainEvents();

        // Start async report generation
        generateReportAsync(report.getReportId());

        return report;
    }

    @Async
    public void generateReportAsync(String reportId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.info("Starting async report generation for: {}", reportId);

        try {
            AnalyticsReport report = reportRepository.findByReportIdAndTenantId(reportId, tenantId)
                    .orElseThrow(() -> new NotFoundException("AnalyticsReport", reportId));

            report.startGeneration();
            reportRepository.save(report);

            // Simulate report generation with actual metrics
            Map<String, Object> metrics = generateReportMetrics(report);

            // Complete the report
            String fileUrl = generateFileUrl(report);
            long fileSizeBytes = calculateFileSize(metrics);

            report.completeGeneration(metrics, fileUrl, fileSizeBytes);
            report = reportRepository.save(report);

            // Publish completion event
            for (var event : report.getDomainEvents()) {
                eventPublisher.publishReportGenerated(event);
            }
            report.clearDomainEvents();

            log.info("Completed report generation for: {}", reportId);

        } catch (Exception e) {
            log.error("Failed to generate report: {}", reportId, e);

            AnalyticsReport report = reportRepository.findByReportIdAndTenantId(reportId, tenantId).orElse(null);
            if (report != null) {
                report.markAsFailed(e.getMessage());
                reportRepository.save(report);
            }
        }
    }

    @Transactional(readOnly = true)
    public AnalyticsReport getReport(String reportId) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.findByReportIdAndTenantId(reportId, tenantId)
                .orElseThrow(() -> new NotFoundException("AnalyticsReport", reportId));
    }

    @Transactional(readOnly = true)
    public List<AnalyticsReport> getReportsByStatus(AnalyticsReport.ReportStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsReport> getReportsByType(AnalyticsReport.ReportType reportType) {
        String tenantId = RequestContextHolder.getTenantId();
        return reportRepository.findByTenantIdAndReportType(tenantId, reportType);
    }

    @Transactional(readOnly = true)
    public List<AnalyticsReport> getReportsByUser() {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();
        return reportRepository.findByTenantIdAndGeneratedBy(tenantId, userId);
    }

    @Transactional
    public AnalyticsReport shareReport(String reportId, String userId) {
        String tenantId = RequestContextHolder.getTenantId();

        AnalyticsReport report = reportRepository.findByReportIdAndTenantId(reportId, tenantId)
                .orElseThrow(() -> new NotFoundException("AnalyticsReport", reportId));

        report.shareWith(userId);
        return reportRepository.save(report);
    }

    @Transactional
    public AnalyticsReport unshareReport(String reportId, String userId) {
        String tenantId = RequestContextHolder.getTenantId();

        AnalyticsReport report = reportRepository.findByReportIdAndTenantId(reportId, tenantId)
                .orElseThrow(() -> new NotFoundException("AnalyticsReport", reportId));

        report.unshareFrom(userId);
        return reportRepository.save(report);
    }

    @Transactional
    public AnalyticsReport addReportTag(String reportId, String tag) {
        String tenantId = RequestContextHolder.getTenantId();

        AnalyticsReport report = reportRepository.findByReportIdAndTenantId(reportId, tenantId)
                .orElseThrow(() -> new NotFoundException("AnalyticsReport", reportId));

        report.addTag(tag);
        return reportRepository.save(report);
    }

    @Transactional
    public void deleteReport(String reportId) {
        String tenantId = RequestContextHolder.getTenantId();

        if (!reportRepository.existsByReportIdAndTenantId(reportId, tenantId)) {
            throw new NotFoundException("AnalyticsReport", reportId);
        }

        reportRepository.deleteByReportIdAndTenantId(reportId, tenantId);
        log.info("Deleted report: {} for tenant: {}", reportId, tenantId);
    }

    @Transactional
    public void cleanupExpiredReports() {
        String tenantId = RequestContextHolder.getTenantId();
        reportRepository.deleteExpiredReports(tenantId);
        log.info("Cleaned up expired reports for tenant: {}", tenantId);
    }

    /**
     * Generates report metrics based on report type
     */
    private Map<String, Object> generateReportMetrics(AnalyticsReport report) {
        Map<String, Object> metrics = new HashMap<>();

        switch (report.getReportType()) {
            case SALES_PERFORMANCE:
                metrics.put("totalRevenue", Math.random() * 1000000);
                metrics.put("quotaAchievement", Math.random() * 100);
                metrics.put("dealsWon", (int) (Math.random() * 100));
                metrics.put("averageDealSize", Math.random() * 50000);
                break;

            case CONVERSION_RATE:
                metrics.put("leadToOpportunityRate", Math.random() * 50);
                metrics.put("opportunityToWinRate", Math.random() * 50);
                metrics.put("overallConversionRate", Math.random() * 25);
                break;

            case PIPELINE_ANALYSIS:
                metrics.put("totalPipelineValue", Math.random() * 5000000);
                metrics.put("pipelineVelocity", Math.random() * 100);
                metrics.put("stageDistribution", Map.of(
                        "prospecting", Math.random() * 100,
                        "qualification", Math.random() * 80,
                        "proposal", Math.random() * 60,
                        "negotiation", Math.random() * 40
                ));
                break;

            case WIN_LOSS_ANALYSIS:
                metrics.put("winRate", Math.random() * 50);
                metrics.put("lossRate", 100 - (Math.random() * 50));
                metrics.put("topLossReasons", List.of("Price", "Competition", "Timing"));
                metrics.put("topCompetitors", List.of("Competitor A", "Competitor B"));
                break;

            default:
                metrics.put("generatedAt", Instant.now().toString());
                metrics.put("reportType", report.getReportType().name());
        }

        return metrics;
    }

    /**
     * Generates a file URL for the report
     */
    private String generateFileUrl(AnalyticsReport report) {
        String extension = getFileExtension(report.getFormat());
        return String.format("https://storage.example.com/reports/%s/%s.%s",
                report.getTenantId(), report.getReportId(), extension);
    }

    /**
     * Gets file extension for format
     */
    private String getFileExtension(AnalyticsReport.ReportFormat format) {
        return switch (format) {
            case PDF -> "pdf";
            case EXCEL -> "xlsx";
            case CSV -> "csv";
            case JSON -> "json";
        };
    }

    /**
     * Calculates estimated file size
     */
    private long calculateFileSize(Map<String, Object> metrics) {
        // Rough estimate based on metrics size
        return (metrics.size() * 100L) + 1024; // Base size + metrics
    }
}
