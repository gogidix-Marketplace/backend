package com.gogidix.finance.reporting.domain.model;

import com.gogidix.finance.reporting.domain.event.ReportGeneratedEvent;
import com.gogidix.finance.reporting.shared.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Report Domain Entity
 * Multi-tenant financial report generation and management
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "reports")
public class Report extends BaseEntity {

    private String reportId;
    private String tenantId;
    private String name;
    private ReportType reportType;
    private ReportFormat format;
    private ReportStatus status;
    private LocalDate reportDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String generatedBy;
    private String fileUrl;
    private Long fileSize;
    private Integer recordCount;
    private Map<String, Object> parameters;
    private List<ReportColumn> columns;
    private List<ReportFilter> filters;
    private String errorMessage;
    private Integer progressPercentage;
    private Instant startedAt;
    private Instant completedAt;
    private Long processingTimeMs;
    private String scheduleId;
    private Boolean isScheduled;
    private List<String> recipientEmails;
    private List<ReportGeneratedEvent> domainEvents = new ArrayList<>();

    public enum ReportType {
        BALANCE_SHEET,
        INCOME_STATEMENT,
        CASH_FLOW,
        TRIAL_BALANCE,
        GENERAL_LEDGER,
        AGING_REPORT,
        BUDGET_VS_ACTUAL,
        EXPENSE_REPORT,
        REVENUE_REPORT,
        CUSTOM_REPORT
    }

    public enum ReportFormat {
        PDF,
        EXCEL,
        CSV,
        JSON
    }

    public enum ReportStatus {
        PENDING,
        GENERATING,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public record ReportColumn(String field, String label, String dataType, Integer width) {}

    public record ReportFilter(String field, String operator, Object value) {}

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Report report = new Report();

        public Builder reportId(String reportId) {
            report.reportId = reportId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            report.tenantId = tenantId;
            return this;
        }

        public Builder name(String name) {
            report.name = name;
            return this;
        }

        public Builder reportType(ReportType reportType) {
            report.reportType = reportType;
            return this;
        }

        public Builder format(ReportFormat format) {
            report.format = format;
            return this;
        }

        public Builder status(ReportStatus status) {
            report.status = status;
            return this;
        }

        public Builder reportDate(LocalDate reportDate) {
            report.reportDate = reportDate;
            return this;
        }

        public Builder periodStart(LocalDate periodStart) {
            report.periodStart = periodStart;
            return this;
        }

        public Builder periodEnd(LocalDate periodEnd) {
            report.periodEnd = periodEnd;
            return this;
        }

        public Builder generatedBy(String generatedBy) {
            report.generatedBy = generatedBy;
            return this;
        }

        public Builder fileUrl(String fileUrl) {
            report.fileUrl = fileUrl;
            return this;
        }

        public Builder fileSize(Long fileSize) {
            report.fileSize = fileSize;
            return this;
        }

        public Builder recordCount(Integer recordCount) {
            report.recordCount = recordCount;
            return this;
        }

        public Builder parameters(Map<String, Object> parameters) {
            report.parameters = parameters;
            return this;
        }

        public Builder columns(List<ReportColumn> columns) {
            report.columns = columns;
            return this;
        }

        public Builder filters(List<ReportFilter> filters) {
            report.filters = filters;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            report.errorMessage = errorMessage;
            return this;
        }

        public Builder progressPercentage(Integer progressPercentage) {
            report.progressPercentage = progressPercentage;
            return this;
        }

        public Builder startedAt(Instant startedAt) {
            report.startedAt = startedAt;
            return this;
        }

        public Builder completedAt(Instant completedAt) {
            report.completedAt = completedAt;
            return this;
        }

        public Builder processingTimeMs(Long processingTimeMs) {
            report.processingTimeMs = processingTimeMs;
            return this;
        }

        public Builder scheduleId(String scheduleId) {
            report.scheduleId = scheduleId;
            return this;
        }

        public Builder isScheduled(Boolean isScheduled) {
            report.isScheduled = isScheduled;
            return this;
        }

        public Builder recipientEmails(List<String> recipientEmails) {
            report.recipientEmails = recipientEmails;
            return this;
        }

        public Builder domainEvents(List<ReportGeneratedEvent> domainEvents) {
            report.domainEvents = domainEvents;
            return this;
        }

        public Report build() {
            return report;
        }
    }

    /**
     * Creates a new report request
     */
    public static Report create(String tenantId, String name, ReportType reportType,
                                ReportFormat format, LocalDate reportDate,
                                String generatedBy, Map<String, Object> parameters) {
        Report report = Report.builder()
            .tenantId(tenantId)
            .name(name)
            .reportType(reportType)
            .format(format)
            .status(ReportStatus.PENDING)
            .reportDate(reportDate)
            .generatedBy(generatedBy)
            .parameters(parameters)
            .progressPercentage(0)
            .isScheduled(false)
            .recipientEmails(new ArrayList<>())
            .columns(new ArrayList<>())
            .filters(new ArrayList<>())
            .build();

        // Generate reportId if not set
        if (report.getReportId() == null) {
            report.setReportId(UUID.randomUUID().toString());
        }

        report.addDomainEvent(ReportGeneratedEvent.create(
            report.getReportId(),
            tenantId,
            reportType.name(),
            ReportStatus.PENDING.name(),
            "REPORT_CREATED"
        ));

        return report;
    }

    /**
     * Starts report generation
     */
    public void startGeneration() {
        if (this.status != ReportStatus.PENDING) {
            throw new IllegalStateException("Can only start pending reports");
        }

        this.status = ReportStatus.GENERATING;
        this.startedAt = Instant.now();
        this.progressPercentage = 0;

        addDomainEvent(ReportGeneratedEvent.create(
            this.reportId,
            this.tenantId,
            this.reportType.name(),
            ReportStatus.GENERATING.name(),
            "REPORT_GENERATION_STARTED"
        ));
    }

    /**
     * Updates progress
     */
    public void updateProgress(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }

        this.progressPercentage = percentage;
    }

    /**
     * Marks report as completed
     */
    public void complete(String fileUrl, Long fileSize, Integer recordCount) {
        if (this.status != ReportStatus.GENERATING) {
            throw new IllegalStateException("Can only complete generating reports");
        }

        this.status = ReportStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.recordCount = recordCount;
        this.progressPercentage = 100;

        if (this.startedAt != null) {
            this.processingTimeMs = this.completedAt.toEpochMilli() - this.startedAt.toEpochMilli();
        }

        addDomainEvent(ReportGeneratedEvent.create(
            this.reportId,
            this.tenantId,
            this.reportType.name(),
            ReportStatus.COMPLETED.name(),
            "REPORT_COMPLETED"
        ));
    }

    /**
     * Marks report as failed
     */
    public void fail(String errorMessage) {
        this.status = ReportStatus.FAILED;
        this.completedAt = Instant.now();
        this.errorMessage = errorMessage;

        if (this.startedAt != null) {
            this.processingTimeMs = this.completedAt.toEpochMilli() - this.startedAt.toEpochMilli();
        }

        addDomainEvent(ReportGeneratedEvent.create(
            this.reportId,
            this.tenantId,
            this.reportType.name(),
            ReportStatus.FAILED.name(),
            "REPORT_FAILED"
        ));
    }

    /**
     * Cancels report generation
     */
    public void cancel() {
        if (this.status == ReportStatus.COMPLETED || this.status == ReportStatus.FAILED) {
            throw new IllegalStateException("Cannot cancel completed or failed reports");
        }

        this.status = ReportStatus.CANCELLED;
        this.completedAt = Instant.now();

        addDomainEvent(ReportGeneratedEvent.create(
            this.reportId,
            this.tenantId,
            this.reportType.name(),
            ReportStatus.CANCELLED.name(),
            "REPORT_CANCELLED"
        ));
    }

    /**
     * Adds a column to the report
     */
    public void addColumn(String field, String label, String dataType, Integer width) {
        if (this.columns == null) {
            this.columns = new ArrayList<>();
        }
        this.columns.add(new ReportColumn(field, label, dataType, width));
    }

    /**
     * Adds a filter to the report
     */
    public void addFilter(String field, String operator, Object value) {
        if (this.filters == null) {
            this.filters = new ArrayList<>();
        }
        this.filters.add(new ReportFilter(field, operator, value));
    }

    /**
     * Adds a recipient email
     */
    public void addRecipientEmail(String email) {
        if (this.recipientEmails == null) {
            this.recipientEmails = new ArrayList<>();
        }
        if (!this.recipientEmails.contains(email)) {
            this.recipientEmails.add(email);
        }
    }

    /**
     * Checks if report is in terminal state
     */
    public boolean isTerminal() {
        return this.status == ReportStatus.COMPLETED ||
               this.status == ReportStatus.FAILED ||
               this.status == ReportStatus.CANCELLED;
    }

    /**
     * Checks if report is ready for download
     */
    public boolean isReadyForDownload() {
        return this.status == ReportStatus.COMPLETED && this.fileUrl != null;
    }

    public void addDomainEvent(ReportGeneratedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
