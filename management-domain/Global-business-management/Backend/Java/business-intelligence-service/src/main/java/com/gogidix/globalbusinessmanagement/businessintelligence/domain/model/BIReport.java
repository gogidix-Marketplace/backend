package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing a Business Intelligence report.
 * Stores generated BI reports with various metrics and insights.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bi_reports")
public class BIReport {

    @Id
    private String id;

    @NotBlank(message = "Report name is required")
    @Indexed
    private String reportName;

    private String reportDescription;

    @NotBlank(message = "Report type is required")
    @Indexed
    private ReportType reportType;

    @NotNull(message = "Report period is required")
    @Indexed
    private ReportPeriod reportPeriod;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Report status is required")
    @Builder.Default
    private ReportStatus status = ReportStatus.DRAFT;

    @NotNull(message = "Created by is required")
    @Indexed
    private String createdBy;

    private List<String> viewers;

    @NotEmpty(message = "At least one section is required")
    @Valid
    private List<ReportSection> sections;

    @Valid
    private ReportSummary summary;

    @Valid
    private ReportMetadata metadata;

    @Indexed
    private Instant generatedAt;

    @Indexed
    private Instant scheduledFor;

    @Indexed
    private String scheduleId;

    private String templateId;

    @Indexed
    private String regionCode;

    @Indexed
    private String businessUnit;

    private ReportSettings settings;

    private Map<String, Object> parameters;

    @Indexed
    private Instant createdAt;

    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSection {
        @NotBlank(message = "Section ID is required")
        private String sectionId;

        @NotBlank(message = "Section title is required")
        private String title;

        private SectionType type;

        private Integer displayOrder;

        @NotNull(message = "Section content is required")
        private SectionContent content;

        private SectionSettings settings;

        private Boolean isVisible;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionContent {
        private String text;

        @Valid
        private List<MetricData> metrics;

        @Valid
        private List<ChartData> charts;

        @Valid
        private List<TableData> tables;

        @Valid
        private List<InsightReference> insights;

        private Map<String, Object> rawData;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricData {
        private String name;

        private String displayName;

        private String value;

        private String format;

        private String trend;

        private BigDecimal trendValue;

        private String comparison;

        private String thresholdStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartData {
        private String chartId;

        private String chartType;

        private String title;

        private List<String> labels;

        private List<DataSet> datasets;

        private ChartOptions options;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSet {
        private String label;

        private String backgroundColor;

        private String borderColor;

        private List<BigDecimal> data;

        private List<BigDecimal> backgroundColors;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartOptions {
        private Boolean responsive;

        private Boolean maintainAspectRatio;

        private String legendPosition;

        private AxisOptions xAxes;

        private AxisOptions yAxes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AxisOptions {
        private Boolean stacked;

        private Boolean display;

        private String position;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TableData {
        private String tableId;

        private String title;

        private List<String> headers;

        private List<List<String>> rows;

        private TableOptions options;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TableOptions {
        private Boolean sortable;

        private Boolean filterable;

        private Boolean paginated;

        private Integer pageSize;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsightReference {
        private String insightId;

        private String title;

        private String summary;

        private String relevance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSummary {
        private String executiveSummary;

        private List<String> keyFindings;

        private List<String> recommendations;

        private List<String> risks;

        private List<String> opportunities;

        private String overallSentiment;

        private Integer confidenceScore;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportMetadata {
        private Integer totalMetrics;

        private Integer totalCharts;

        private Integer totalTables;

        private String dataSource;

        private String dataFreshness;

        private String generationTime;

        private String currency;

        private String locale;

        private String timeZone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSettings {
        private Boolean includeCharts;

        private Boolean includeTables;

        private Boolean includeRawData;

        private Boolean includeInsights;

        private String chartType;

        private String dateFormat;

        private String numberFormat;

        private String currencyFormat;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionSettings {
        private Boolean collapsible;

        private Boolean defaultExpanded;

        private String backgroundColor;

        private String borderColor;
    }

    public enum ReportType {
        EXECUTIVE_SUMMARY,
        FINANCIAL_PERFORMANCE,
        SALES_ANALYTICS,
        CUSTOMER_ANALYTICS,
        OPERATIONAL_REPORT,
        REGIONAL_REPORT,
        FORECAST_REPORT,
        CUSTOM
    }

    public enum ReportStatus {
        DRAFT,
        GENERATING,
        COMPLETED,
        FAILED,
        SCHEDULED,
        ARCHIVED
    }

    public enum ReportPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY,
        CUSTOM
    }

    public enum SectionType {
        SUMMARY,
        METRICS,
        CHARTS,
        TABLES,
        INSIGHTS,
        FORECASTS,
        CUSTOM
    }

    public boolean isCompleted() {
        return ReportStatus.COMPLETED.equals(status);
    }

    public boolean isScheduled() {
        return ReportStatus.SCHEDULED.equals(status);
    }

    public boolean hasViewer(String userId) {
        return viewers != null && viewers.contains(userId);
    }

    public ReportSection getSectionById(String sectionId) {
        return sections.stream()
            .filter(s -> s.getSectionId().equals(sectionId))
            .findFirst()
            .orElse(null);
    }
}
