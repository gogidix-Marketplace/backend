package com.gogidix.dashboard.reporting.domain.port.in;

import com.gogidix.dashboard.reporting.domain.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Report Management Use Case
 * 
 * Primary port for report generation and management operations
 * Defines all business use cases for the reporting domain
 */
public interface ReportManagementUseCase {

    /**
     * Generate a new report
     */
    Report generateReport(GenerateReportCommand command);

    /**
     * Get report by ID
     */
    Optional<Report> getReport(ReportId reportId);

    /**
     * Get reports by type
     */
    List<Report> getReportsByType(ReportType type);

    /**
     * Get reports by domain
     */
    List<Report> getReportsByDomain(String domain);

    /**
     * Schedule automated report
     */
    ScheduledReport scheduleReport(ScheduleReportCommand command);

    /**
     * Get scheduled reports
     */
    List<ScheduledReport> getScheduledReports();

    /**
     * Export report to different formats
     */
    ReportExport exportReport(ReportId reportId, ExportFormat format);

    /**
     * Delete report
     */
    void deleteReport(ReportId reportId);

    // Command Classes
    class GenerateReportCommand {
        private final String reportName;
        private final ReportType type;
        private final String domain;
        private final LocalDateTime fromDate;
        private final LocalDateTime toDate;
        private final ReportConfiguration configuration;

        public GenerateReportCommand(String reportName, ReportType type, String domain, 
                                   LocalDateTime fromDate, LocalDateTime toDate, 
                                   ReportConfiguration configuration) {
            this.reportName = reportName;
            this.type = type;
            this.domain = domain;
            this.fromDate = fromDate;
            this.toDate = toDate;
            this.configuration = configuration;
        }

        public String getReportName() { return reportName; }
        public ReportType getType() { return type; }
        public String getDomain() { return domain; }
        public LocalDateTime getFromDate() { return fromDate; }
        public LocalDateTime getToDate() { return toDate; }
        public ReportConfiguration getConfiguration() { return configuration; }
    }

    class ScheduleReportCommand {
        private final String reportName;
        private final ReportType type;
        private final String domain;
        private final String cronExpression;
        private final ReportConfiguration configuration;

        public ScheduleReportCommand(String reportName, ReportType type, String domain,
                                   String cronExpression, ReportConfiguration configuration) {
            this.reportName = reportName;
            this.type = type;
            this.domain = domain;
            this.cronExpression = cronExpression;
            this.configuration = configuration;
        }

        public String getReportName() { return reportName; }
        public ReportType getType() { return type; }
        public String getDomain() { return domain; }
        public String getCronExpression() { return cronExpression; }
        public ReportConfiguration getConfiguration() { return configuration; }
    }
}