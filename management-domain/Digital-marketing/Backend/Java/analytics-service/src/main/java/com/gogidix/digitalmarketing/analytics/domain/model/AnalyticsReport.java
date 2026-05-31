package com.gogidix.digitalmarketing.analytics.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

/**
 * AnalyticsReport - Generated analytics reports with configurations
 *
 * <p>This entity stores generated reports with their configurations and results.
 * Reports can be scheduled, on-demand, or real-time dashboards.</p>
 *
 * <p>Report Types:</p>
 * <ul>
 *   <li>CAMPAIGN_PERFORMANCE - Campaign performance summary</li>
 *   <li>CHANNEL_COMPARISON - Channel performance comparison</li>
 *   <li>ROI_ANALYSIS - Return on investment analysis</li>
 *   <li>TREND_ANALYSIS - Trend and pattern analysis</li>
 *   <li>ATTRIBUTION - Multi-touch attribution report</li>
 *   <li>CUSTOM - Custom configured report</li>
 * </ul>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "analytics_reports")
@TypeAlias("analytics_report")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "report_tenant_type_idx", def = "{'tenantId': 1, 'reportType': 1, 'createdAt': -1}")
@CompoundIndex(name = "report_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'createdAt': -1}")
@CompoundIndex(name = "report_tenant_schedule_idx", def = "{'tenantId': 1, 'scheduleEnabled': 1, 'nextRunAt': 1}")
public class AnalyticsReport extends BaseEntity {

    /**
     * Report name
     */
    @Indexed
    private String name;

    /**
     * Report description
     */
    private String description;

    /**
     * Report type
     */
    @Indexed
    private String reportType;

    /**
     * Report format (PDF, XLSX, CSV, JSON, HTML)
     */
    private String format;

    /**
     * Report status (DRAFT, SCHEDULED, RUNNING, COMPLETED, FAILED)
     */
    @Indexed
    private String status;

    /**
     * Report owner/creator
     */
    @Indexed
    private String owner;

    /**
     * Whether this is a template report
     */
    @Builder.Default
    private Boolean isTemplate = false;

    /**
     * Template ID if created from a template
     */
    @Indexed
    private String templateId;

    // === Date Range ===

    /**
     * Report start date
     */
    private Instant startDate;

    /**
     * Report end date
     */
    private Instant endDate;

    /**
     * Date range type (TODAY, YESTERDAY, LAST_7_DAYS, LAST_30_DAYS, THIS_MONTH, LAST_MONTH, CUSTOM)
     */
    private String dateRangeType;

    /**
     * Comparison period start date (for year-over-year, etc.)
     */
    private Instant comparisonStartDate;

    /**
     * Comparison period end date
     */
    private Instant comparisonEndDate;

    // === Filters ===

    /**
     * Campaign IDs to include (empty = all campaigns)
     */
    private List<String> campaignIds;

    /**
     * Channel types to include (empty = all channels)
     */
    private List<String> channelTypes;

    /**
     * Geographic regions to include
     */
    private List<String> regions;

    /**
     * Product categories to include
     */
    private List<String> productCategories;

    /**
     * Custom filters as key-value pairs
     */
    private Map<String, Object> filters;

    // === Metrics Configuration ===

    /**
     * Metrics to include in the report
     */
    private List<String> metrics;

    /**
     * Dimensions to group by
     */
    private List<String> dimensions;

    /**
     * Sort order for results
     */
    private List<SortConfiguration> sortConfigurations;

    /**
     * Aggregation configuration
     */
    private AggregationConfiguration aggregationConfiguration;

    // === Scheduling ===

    /**
     * Whether the report is scheduled
     */
    @Indexed
    private Boolean scheduleEnabled;

    /**
     * Schedule type (ONCE, DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY)
     */
    private String scheduleType;

    /**
     * Cron expression for advanced scheduling
     */
    private String cronExpression;

    /**
     * Next scheduled run time
     */
    @Indexed
    private Instant nextRunAt;

    /**
     * Last run time
     */
    private Instant lastRunAt;

    /**
     * Last run status
     */
    private String lastRunStatus;

    // === Delivery ===

    /**
     * Delivery method (EMAIL, WEBHOOK, S3, NONE)
     */
    private String deliveryMethod;

    /**
     * Email recipients for delivery
     */
    private List<String> emailRecipients;

    /**
     * Webhook URL for delivery
     */
    private String webhookUrl;

    /**
     * S3 bucket for storage
     */
    private String s3Bucket;

    /**
     * S3 key prefix
     */
    private String s3KeyPrefix;

    // === Results ===

    /**
     * Generated report data
     */
    private Map<String, Object> reportData;

    /**
     * Report sections/panels
     */
    private List<ReportSection> sections;

    /**
     * Chart configurations
     */
    private List<ChartConfiguration> chartConfigurations;

    /**
     * Key findings/insights
     */
    private List<String> insights;

    /**
     * Recommendations based on data
     */
    private List<String> recommendations;

    /**
     * Report file location (if exported)
     */
    private String fileLocation;

    /**
     * Report file URL
     */
    private String fileUrl;

    /**
     * File size in bytes
     */
    private Long fileSize;

    /**
     * Report generation duration in milliseconds
     */
    private Long generationDuration;

    /**
     * Number of records processed
     */
    private Long recordsProcessed;

    /**
     * Error message if failed
     */
    private String errorMessage;

    // === Visualization ===

    /**
     * Report theme/light/dark mode
     */
    private String theme;

    /**
     * Color palette
     */
    private List<String> colorPalette;

    /**
     * Logo URL
     */
    private String logoUrl;

    /**
     * Custom CSS
     */
    private String customCss;

    // === Access Control ===

    /**
     * Users with access to this report
     */
    private List<String> accessibleBy;

    /**
     * Whether this is a public report
     */
    @Builder.Default
    private Boolean isPublic = false;

    /**
     * Public link token
     */
    private String publicLinkToken;

    /**
     * Public link expiration
     */
    private Instant publicLinkExpiration;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Last updated timestamp
     */
    private Instant lastUpdatedAt;

    /**
     * Create a new report.
     *
     * @param tenantId   the tenant ID
     * @param name       the report name
     * @param reportType the report type
     * @param owner      the report owner
     */
    public AnalyticsReport(String tenantId, String name, String reportType, String owner) {
        super(tenantId);
        this.name = Objects.requireNonNull(name, "Report name is required");
        this.reportType = Objects.requireNonNull(reportType, "Report type is required");
        this.owner = owner;
        this.status = "DRAFT";
        this.scheduleEnabled = false;
        this.isTemplate = false;
        this.isPublic = false;
        this.format = "PDF";
    }

    /**
     * Add a campaign filter.
     *
     * @param campaignId the campaign ID
     */
    public void addCampaignFilter(String campaignId) {
        if (this.campaignIds == null) {
            this.campaignIds = new ArrayList<>();
        }
        this.campaignIds.add(campaignId);
    }

    /**
     * Add a channel filter.
     *
     * @param channelType the channel type
     */
    public void addChannelFilter(String channelType) {
        if (this.channelTypes == null) {
            this.channelTypes = new ArrayList<>();
        }
        this.channelTypes.add(channelType);
    }

    /**
     * Add a metric to include.
     *
     * @param metric the metric name
     */
    public void addMetric(String metric) {
        if (this.metrics == null) {
            this.metrics = new ArrayList<>();
        }
        this.metrics.add(metric);
    }

    /**
     * Add a dimension to group by.
     *
     * @param dimension the dimension name
     */
    public void addDimension(String dimension) {
        if (this.dimensions == null) {
            this.dimensions = new ArrayList<>();
        }
        this.dimensions.add(dimension);
    }

    /**
     * Add a sort configuration.
     *
     * @param field     the field to sort by
     * @param direction the sort direction (ASC, DESC)
     */
    public void addSortConfiguration(String field, String direction) {
        if (this.sortConfigurations == null) {
            this.sortConfigurations = new ArrayList<>();
        }
        this.sortConfigurations.add(new SortConfiguration(field, direction));
    }

    /**
     * Add an email recipient.
     *
     * @param email the email address
     */
    public void addEmailRecipient(String email) {
        if (this.emailRecipients == null) {
            this.emailRecipients = new ArrayList<>();
        }
        this.emailRecipients.add(email);
    }

    /**
     * Add a report section.
     *
     * @param section the section to add
     */
    public void addSection(ReportSection section) {
        if (this.sections == null) {
            this.sections = new ArrayList<>();
        }
        this.sections.add(section);
    }

    /**
     * Add a chart configuration.
     *
     * @param chart the chart configuration
     */
    public void addChartConfiguration(ChartConfiguration chart) {
        if (this.chartConfigurations == null) {
            this.chartConfigurations = new ArrayList<>();
        }
        this.chartConfigurations.add(chart);
    }

    /**
     * Add an insight.
     *
     * @param insight the insight to add
     */
    public void addInsight(String insight) {
        if (this.insights == null) {
            this.insights = new ArrayList<>();
        }
        this.insights.add(insight);
    }

    /**
     * Add a recommendation.
     *
     * @param recommendation the recommendation to add
     */
    public void addRecommendation(String recommendation) {
        if (this.recommendations == null) {
            this.recommendations = new ArrayList<>();
        }
        this.recommendations.add(recommendation);
    }

    /**
     * Add an accessible user.
     *
     * @param userId the user ID
     */
    public void addAccessibleUser(String userId) {
        if (this.accessibleBy == null) {
            this.accessibleBy = new ArrayList<>();
        }
        this.accessibleBy.add(userId);
    }

    /**
     * Check if a user has access to this report.
     *
     * @param userId the user ID
     * @return true if user has access
     */
    public boolean isAccessibleBy(String userId) {
        return isPublic
            || (owner != null && owner.equals(userId))
            || (accessibleBy != null && accessibleBy.contains(userId));
    }

    /**
     * Check if the report is ready.
     *
     * @return true if status is COMPLETED
     */
    public boolean isReady() {
        return "COMPLETED".equals(this.status);
    }

    /**
     * Check if the report is running.
     *
     * @return true if status is RUNNING
     */
    public boolean isRunning() {
        return "RUNNING".equals(this.status);
    }

    /**
     * Check if the report failed.
     *
     * @return true if status is FAILED
     */
    public boolean hasFailed() {
        return "FAILED".equals(this.status);
    }

    /**
     * Check if the report is scheduled.
     *
     * @return true if schedule is enabled
     */
    public boolean isScheduled() {
        return scheduleEnabled != null && scheduleEnabled;
    }

    /**
     * Check if the public link is valid.
     *
     * @return true if public link is not expired
     */
    public boolean isPublicLinkValid() {
        return isPublic && publicLinkToken != null
            && (publicLinkExpiration == null || publicLinkExpiration.isAfter(Instant.now()));
    }

    /**
     * Mark report as running.
     */
    public void markAsRunning() {
        this.status = "RUNNING";
        this.lastRunAt = Instant.now();
        this.lastRunStatus = "RUNNING";
        markAsUpdated();
    }

    /**
     * Mark report as completed.
     *
     * @param fileUrl the generated file URL
     */
    public void markAsCompleted(String fileUrl) {
        this.status = "COMPLETED";
        this.lastRunStatus = "COMPLETED";
        this.fileUrl = fileUrl;
        markAsUpdated();

        // Schedule next run if enabled
        if (scheduleEnabled && nextRunAt != null && nextRunAt.isBefore(Instant.now())) {
            calculateNextRunTime();
        }
    }

    /**
     * Mark report as failed.
     *
     * @param errorMessage the error message
     */
    public void markAsFailed(String errorMessage) {
        this.status = "FAILED";
        this.lastRunStatus = "FAILED";
        this.errorMessage = errorMessage;
        markAsUpdated();
    }

    /**
     * Calculate next run time based on schedule type.
     */
    public void calculateNextRunTime() {
        if (!scheduleEnabled) {
            this.nextRunAt = null;
            return;
        }

        Instant now = Instant.now();
        switch (scheduleType) {
            case "DAILY":
                this.nextRunAt = now.plus(java.time.Duration.ofDays(1));
                break;
            case "WEEKLY":
                this.nextRunAt = now.plus(java.time.Duration.ofDays(7));
                break;
            case "MONTHLY":
                this.nextRunAt = now.plus(java.time.Duration.ofDays(30));
                break;
            case "QUARTERLY":
                this.nextRunAt = now.plus(java.time.Duration.ofDays(90));
                break;
            case "YEARLY":
                this.nextRunAt = now.plus(java.time.Duration.ofDays(365));
                break;
            case "ONCE":
            default:
                this.nextRunAt = null;
                break;
        }
    }

    /**
     * Mark report as updated.
     */
    public void markAsUpdated() {
        this.lastUpdatedAt = Instant.now();
        this.touch();
    }

    /**
     * Get the date range type for display.
     *
     * @return formatted date range
     */
    public String getDateRangeDisplay() {
        if (dateRangeType != null && !"CUSTOM".equals(dateRangeType)) {
            return dateRangeType.replace("_", " ");
        }
        if (startDate != null && endDate != null) {
            return startDate + " to " + endDate;
        }
        return "Not specified";
    }

    /**
     * Sort configuration for report results.
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SortConfiguration {
        private String field;
        private String direction; // ASC, DESC
    }

    /**
     * Aggregation configuration.
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AggregationConfiguration {
        private String type; // SUM, AVG, COUNT, MIN, MAX
        private String field;
        private String groupBy;
    }

    /**
     * Report section configuration.
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSection {
        private String id;
        private String title;
        private String type; // TABLE, CHART, METRIC, TEXT
        private Integer order;
        private Map<String, Object> configuration;
        private Map<String, Object> data;
    }

    /**
     * Chart configuration.
     */
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartConfiguration {
        private String id;
        private String title;
        private String chartType; // LINE, BAR, PIE, AREA, SCATTER, HEATMAP
        private String xAxis;
        private String yAxis;
        private List<String> series;
        private Map<String, Object> options;
        private Integer order;
    }
}
