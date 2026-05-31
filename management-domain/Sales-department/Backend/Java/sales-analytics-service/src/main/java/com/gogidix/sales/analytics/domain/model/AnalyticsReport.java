package com.gogidix.sales.analytics.domain.model;

import com.gogidix.sales.analytics.domain.event.ReportGeneratedEvent;
import com.gogidix.sales.analytics.shared.base.BaseEntity;
import com.gogidix.sales.analytics.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analytics Report Domain Entity
 * Represents a generated analytics report with various metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "analytics_reports")
public class AnalyticsReport extends BaseEntity {

    private String reportId;

    private String tenantId;

    private String name;

    private ReportType reportType;

    private String description;

    private Instant startDate;

    private Instant endDate;

    private ReportStatus status;

    private String generatedBy;

    private Map<String, Object> metrics;

    private Map<String, Object> filters;

    private ReportFormat format;

    private String fileUrl;

    private Long fileSizeBytes;

    private Instant generatedAt;

    private Instant expiresAt;

    private List<String> sharedWith;

    private String scheduleId;

    private Boolean isScheduled;

    private List<String> tags;

    @Builder.Default
    private List<ReportGeneratedEvent> domainEvents = new ArrayList<>();

    public enum ReportType {
        SALES_PERFORMANCE,
        CONVERSION_RATE,
        SALES_VELOCITY,
        PIPELINE_ANALYSIS,
        WIN_LOSS_ANALYSIS,
        PRODUCT_PERFORMANCE,
        SALES_REP_PERFORMANCE,
        REGIONAL_PERFORMANCE,
        CUSTOMER_SEGMENTATION,
        FORECAST_ACCURACY,
        QUOTA_ACHIEVEMENT,
        CUSTOM
    }

    public enum ReportStatus {
        PENDING,
        GENERATING,
        COMPLETED,
        FAILED,
        EXPIRED,
        CANCELLED
    }

    public enum ReportFormat {
        PDF,
        EXCEL,
        CSV,
        JSON
    }

    /**
     * Creates a new analytics report
     */
    public static AnalyticsReport create(String tenantId, String name, ReportType reportType,
                                         String description, Instant startDate, Instant endDate,
                                         String generatedBy, Map<String, Object> filters,
                                         ReportFormat format) {
        String reportId = generateReportId();

        AnalyticsReport report = AnalyticsReport.builder()
                .reportId(reportId)
                .tenantId(tenantId)
                .name(name)
                .reportType(reportType)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .status(ReportStatus.PENDING)
                .generatedBy(generatedBy)
                .metrics(new HashMap<>())
                .filters(filters != null ? filters : new HashMap<>())
                .format(format)
                .sharedWith(new ArrayList<>())
                .tags(new ArrayList<>())
                .isScheduled(false)
                .build();

        report.addDomainEvent(ReportGeneratedEvent.create(
                reportId, tenantId, reportType.name(), generatedBy,
                startDate, endDate, new HashMap<>(), "REPORT_CREATED"
        ));

        return report;
    }

    /**
     * Starts report generation
     */
    public void startGeneration() {
        if (this.status != ReportStatus.PENDING) {
            throw new IllegalStateException("Can only start generation for pending reports");
        }
        this.status = ReportStatus.GENERATING;
    }

    /**
     * Completes report generation
     */
    public void completeGeneration(Map<String, Object> metrics, String fileUrl, Long fileSizeBytes) {
        if (this.status != ReportStatus.GENERATING) {
            throw new IllegalStateException("Can only complete generating reports");
        }

        this.status = ReportStatus.COMPLETED;
        this.metrics = metrics;
        this.fileUrl = fileUrl;
        this.fileSizeBytes = fileSizeBytes;
        this.generatedAt = Instant.now();

        // Set expiry to 30 days from now
        this.expiresAt = Instant.now().plusSeconds(30 * 24 * 60 * 60);

        addDomainEvent(ReportGeneratedEvent.create(
                this.reportId, this.tenantId, this.reportType.name(), this.generatedBy,
                this.startDate, this.endDate, metrics, "REPORT_COMPLETED"
        ));
    }

    /**
     * Marks report as failed
     */
    public void markAsFailed(String reason) {
        this.status = ReportStatus.FAILED;
        this.metrics = Map.of("error", reason, "failedAt", Instant.now().toString());

        addDomainEvent(ReportGeneratedEvent.create(
                this.reportId, this.tenantId, this.reportType.name(), this.generatedBy,
                this.startDate, this.endDate, this.metrics, "REPORT_FAILED"
        ));
    }

    /**
     * Adds a metric to the report
     */
    public void addMetric(String key, Object value) {
        if (this.metrics == null) {
            this.metrics = new HashMap<>();
        }
        this.metrics.put(key, value);
    }

    /**
     * Shares the report with a user
     */
    public void shareWith(String userId) {
        if (this.sharedWith == null) {
            this.sharedWith = new ArrayList<>();
        }
        if (!this.sharedWith.contains(userId)) {
            this.sharedWith.add(userId);
        }
    }

    /**
     * Removes sharing from a user
     */
    public void unshareFrom(String userId) {
        if (this.sharedWith != null) {
            this.sharedWith.remove(userId);
        }
    }

    /**
     * Adds a tag to the report
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Sets the schedule for the report
     */
    public void setSchedule(String scheduleId) {
        this.scheduleId = scheduleId;
        this.isScheduled = true;
    }

    /**
     * Validates the report date range
     */
    public void validateDateRange() {
        if (this.startDate != null && this.endDate != null) {
            if (this.endDate.isBefore(this.startDate)) {
                throw new ValidationException("endDate", "End date must be after start date");
            }
        }
    }

    /**
     * Checks if the report has expired
     */
    public boolean isExpired() {
        return this.expiresAt != null && Instant.now().isAfter(this.expiresAt);
    }

    /**
     * Refreshes the expiry date
     */
    public void refreshExpiry() {
        this.expiresAt = Instant.now().plusSeconds(30 * 24 * 60 * 60);
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

    private static String generateReportId() {
        return "RPT-" + System.currentTimeMillis() + "-" +
               Integer.toHexString((int) (Math.random() * 0xFFFF)).toUpperCase();
    }
}
