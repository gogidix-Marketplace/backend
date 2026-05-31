package com.gogidix.hr.globalworkforceanalytics.domain.model;

import com.gogidix.hr.globalworkforceanalytics.domain.enums.AggregationLevel;
import com.gogidix.hr.globalworkforceanalytics.domain.enums.TimePeriod;
import com.gogidix.hr.globalworkforceanalytics.domain.event.ReportGeneratedEvent;
import com.gogidix.hr.globalworkforceanalytics.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AnalyticsReport Domain Entity
 * Represents generated analytics reports with visualizations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "analytics_reports")
public class AnalyticsReport extends BaseEntity {

    @Indexed(unique = true)
    private String reportCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String countryCode;

    private String reportName;
    private String description;
    private String category;

    @Indexed
    private TimePeriod timePeriod;

    private YearMonth period;
    private LocalDate startDate;
    private LocalDate endDate;

    @Indexed
    private AggregationLevel aggregationLevel;

    @Indexed
    private String reportType;

    @Indexed
    private ReportStatus status;

    @Indexed
    private List<String> metricIds;

    @Builder.Default
    private Map<String, Object> metricData = new HashMap<>();

    @Builder.Default
    private List<ReportSection> sections = new ArrayList<>();

    @Builder.Default
    private List<ChartDefinition> charts = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> filters = new HashMap<>();

    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();

    private String dataSource;
    private Integer dataRowCount;

    @Indexed
    private String generatedBy;

    private LocalDate generatedDate;
    private LocalDate lastRefreshedDate;

    @Indexed
    private Boolean isScheduled;

    private String scheduleExpression;
    private LocalDate nextRunDate;

    @Indexed
    private Boolean isPublished;

    @Indexed
    private Boolean isArchived;

    private String publishedBy;
    private LocalDate publishedDate;

    @Builder.Default
    private List<String> distributionList = new ArrayList<>();

    private String reportFormat;
    private String outputLocation;
    private String fileUrl;

    @Indexed
    private String parentReportId;

    @Builder.Default
    private List<String> childReportIds = new ArrayList<>();

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @Builder.Default
    private List<String> viewerIds = new ArrayList<>();

    private Integer viewCount;

    @Builder.Default
    private Map<String, Object> executionStats = new HashMap<>();

    public enum ReportStatus {
        DRAFT,
        GENERATING,
        GENERATED,
        FAILED,
        PUBLISHED,
        ARCHIVED
    }

    /**
     * Report section definition
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSection {
        private String sectionId;
        private String title;
        private String description;
        private Integer order;
        private String sectionType;
        private Map<String, Object> content;
        private List<String> chartIds;
        private Boolean isVisible;
    }

    /**
     * Chart definition
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartDefinition {
        private String chartId;
        private String title;
        private String chartType;
        private String xAxis;
        private String yAxis;
        private List<String> series;
        private Map<String, Object> config;
        private Map<String, Object> data;
        private Integer order;
        private String sectionId;
    }

    /**
     * Creates a new analytics report
     */
    public static AnalyticsReport create(String tenantId, String countryCode,
                                         String reportName, String reportType,
                                         TimePeriod timePeriod, YearMonth period,
                                         AggregationLevel aggregationLevel,
                                         String generatedBy) {
        String reportCode = generateReportCode(tenantId, reportType);

        AnalyticsReport report = new AnalyticsReport();
        report.setTenantId(tenantId);
        report.setCountryCode(countryCode);
        report.setReportName(reportName);
        report.setReportCode(reportCode);
        report.setReportType(reportType);
        report.setTimePeriod(timePeriod);
        report.setPeriod(period);
        report.setAggregationLevel(aggregationLevel);
        report.setStatus(ReportStatus.DRAFT);
        report.setGeneratedBy(generatedBy);
        report.setGeneratedDate(LocalDate.now());
        report.setIsScheduled(false);
        report.setIsPublished(false);
        report.setIsArchived(false);
        report.setViewCount(0);
        report.setMetricIds(new ArrayList<>());
        report.setMetricData(new HashMap<>());
        report.setSections(new ArrayList<>());
        report.setCharts(new ArrayList<>());
        report.setFilters(new HashMap<>());
        report.setParameters(new HashMap<>());
        report.setDistributionList(new ArrayList<>());
        report.setChildReportIds(new ArrayList<>());
        report.setTags(new ArrayList<>());
        report.setMetadata(new HashMap<>());
        report.setViewerIds(new ArrayList<>());
        report.setExecutionStats(new HashMap<>());

        return report;
    }

    /**
     * Starts report generation
     */
    public void startGeneration() {
        if (this.status != ReportStatus.DRAFT && this.status != ReportStatus.FAILED) {
            throw new IllegalStateException("Report generation can only be started from DRAFT or FAILED status");
        }
        this.status = ReportStatus.GENERATING;
    }

    /**
     * Completes report generation
     */
    public void completeGeneration(Integer dataRowCount, Map<String, Object> metricData) {
        if (this.status != ReportStatus.GENERATING) {
            throw new IllegalStateException("Report is not in GENERATING status");
        }
        this.status = ReportStatus.GENERATED;
        this.dataRowCount = dataRowCount;
        this.metricData = metricData != null ? metricData : new HashMap<>();
        this.lastRefreshedDate = LocalDate.now();

        this.executionStats.put("generatedAt", LocalDate.now().toString());
        this.executionStats.put("dataRowCount", dataRowCount);

        this.addDomainEvent(ReportGeneratedEvent.builder()
                .reportId(this.getId())
                .tenantId(this.tenantId)
                .reportCode(this.reportCode)
                .reportName(this.reportName)
                .eventType("REPORT_GENERATED")
                .build());
    }

    /**
     * Fails report generation
     */
    public void failGeneration(String errorMessage) {
        if (this.status != ReportStatus.GENERATING) {
            throw new IllegalStateException("Report is not in GENERATING status");
        }
        this.status = ReportStatus.FAILED;
        this.executionStats.put("failedAt", LocalDate.now().toString());
        this.executionStats.put("errorMessage", errorMessage);
    }

    /**
     * Publishes the report
     */
    public void publish(String publishedBy) {
        if (this.status != ReportStatus.GENERATED) {
            throw new IllegalStateException("Can only publish generated reports");
        }
        this.status = ReportStatus.PUBLISHED;
        this.isPublished = true;
        this.publishedBy = publishedBy;
        this.publishedDate = LocalDate.now();
    }

    /**
     * Archives the report
     */
    public void archive() {
        this.status = ReportStatus.ARCHIVED;
        this.isArchived = true;
    }

    /**
     * Adds a metric to the report
     */
    public void addMetric(String metricId) {
        if (this.metricIds == null) {
            this.metricIds = new ArrayList<>();
        }
        if (!this.metricIds.contains(metricId)) {
            this.metricIds.add(metricId);
        }
    }

    /**
     * Removes a metric from the report
     */
    public void removeMetric(String metricId) {
        if (this.metricIds != null) {
            this.metricIds.remove(metricId);
        }
    }

    /**
     * Adds a section
     */
    public void addSection(ReportSection section) {
        if (this.sections == null) {
            this.sections = new ArrayList<>();
        }
        this.sections.add(section);
    }

    /**
     * Adds a chart
     */
    public void addChart(ChartDefinition chart) {
        if (this.charts == null) {
            this.charts = new ArrayList<>();
        }
        this.charts.add(chart);
    }

    /**
     * Sets a filter
     */
    public void setFilter(String key, Object value) {
        if (this.filters == null) {
            this.filters = new HashMap<>();
        }
        this.filters.put(key, value);
    }

    /**
     * Sets a parameter
     */
    public void setParameter(String key, Object value) {
        if (this.parameters == null) {
            this.parameters = new HashMap<>();
        }
        this.parameters.put(key, value);
    }

    /**
     * Adds a recipient to distribution list
     */
    public void addDistributionRecipient(String email) {
        if (this.distributionList == null) {
            this.distributionList = new ArrayList<>();
        }
        if (!this.distributionList.contains(email)) {
            this.distributionList.add(email);
        }
    }

    /**
     * Sets up schedule
     */
    public void setSchedule(String scheduleExpression, LocalDate nextRunDate) {
        this.isScheduled = true;
        this.scheduleExpression = scheduleExpression;
        this.nextRunDate = nextRunDate;
    }

    /**
     * Removes schedule
     */
    public void removeSchedule() {
        this.isScheduled = false;
        this.scheduleExpression = null;
        this.nextRunDate = null;
    }

    /**
     * Increments view count
     */
    public void incrementViewCount(String viewerId) {
        this.viewCount = (this.viewCount != null ? this.viewCount : 0) + 1;
        if (viewerId != null) {
            if (this.viewerIds == null) {
                this.viewerIds = new ArrayList<>();
            }
            if (!this.viewerIds.contains(viewerId)) {
                this.viewerIds.add(viewerId);
            }
        }
    }

    /**
     * Adds a tag
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
     * Checks if report is ready to view
     */
    public boolean isReadyToView() {
        return this.status == ReportStatus.GENERATED ||
               this.status == ReportStatus.PUBLISHED;
    }

    /**
     * Checks if report can be modified
     */
    public boolean canModify() {
        return !this.isPublished && !this.isArchived &&
               (this.status == ReportStatus.DRAFT || this.status == ReportStatus.FAILED);
    }

    /**
     * Refreshes report data
     */
    public void refreshData() {
        if (this.status != ReportStatus.GENERATED && this.status != ReportStatus.PUBLISHED) {
            throw new IllegalStateException("Can only refresh generated or published reports");
        }
        ReportStatus previousStatus = this.status;
        this.status = ReportStatus.GENERATING;
        this.executionStats.put("lastRefreshAt", LocalDate.now().toString());
        this.executionStats.put("previousStatus", previousStatus.name());
    }

    /**
     * Generates report code
     */
    private static String generateReportCode(String tenantId, String reportType) {
        String prefix = reportType.substring(0, Math.min(3, reportType.length())).toUpperCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "RPT-" + prefix + "-" + uniqueId;
    }

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public void addDomainEvent(Object event) {
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
