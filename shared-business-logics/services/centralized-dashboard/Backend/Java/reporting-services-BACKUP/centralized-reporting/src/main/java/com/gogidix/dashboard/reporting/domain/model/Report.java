package com.gogidix.dashboard.reporting.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Report Domain Entity
 * 
 * Core business entity for dashboard reporting with comprehensive business logic
 */
public class Report {
    
    private final ReportId id;
    private final String reportName;
    private final ReportType reportType;
    private final ReportTemplate template;
    private final ReportParameters parameters;
    private final LocalDateTime createdAt;
    private final String createdBy;
    private ReportStatus status;
    private LocalDateTime generatedAt;
    private ReportOutput output;
    private final List<ReportSection> sections;
    private final Map<String, Object> metadata;
    private final ReportConfiguration configuration;
    
    private Report(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "Report ID is required");
        this.reportName = Objects.requireNonNull(builder.reportName, "Report name is required");
        this.reportType = Objects.requireNonNull(builder.reportType, "Report type is required");
        this.template = Objects.requireNonNull(builder.template, "Template is required");
        this.parameters = Objects.requireNonNull(builder.parameters, "Parameters are required");
        this.createdAt = LocalDateTime.now();
        this.createdBy = Objects.requireNonNull(builder.createdBy, "Creator is required");
        this.status = ReportStatus.PENDING;
        this.sections = List.copyOf(builder.sections);
        this.metadata = Map.copyOf(builder.metadata);
        this.configuration = Objects.requireNonNull(builder.configuration, "Configuration is required");
    }
    
    /**
     * Generate the report
     */
    public void generate() {
        validateCanGenerate();
        
        status = ReportStatus.GENERATING;
        
        try {
            // Execute report generation logic
            ReportOutput.Builder outputBuilder = ReportOutput.builder()
                .withReportId(id)
                .withFormat(configuration.getOutputFormat())
                .withGeneratedAt(LocalDateTime.now());
            
            // Process each section
            for (ReportSection section : sections) {
                SectionOutput sectionOutput = generateSection(section);
                outputBuilder.addSectionOutput(sectionOutput);
            }
            
            // Apply formatting and styling
            output = outputBuilder
                .withTotalPages(calculateTotalPages())
                .withFileSize(calculateFileSize())
                .build();
            
            generatedAt = LocalDateTime.now();
            status = ReportStatus.COMPLETED;
            
        } catch (Exception e) {
            status = ReportStatus.FAILED;
            throw new ReportGenerationException("Failed to generate report: " + e.getMessage(), e);
        }
    }
    
    /**
     * Generate individual section
     */
    private SectionOutput generateSection(ReportSection section) {
        // Apply section-specific business logic
        switch (section.getType()) {
            case EXECUTIVE_SUMMARY:
                return generateExecutiveSummary(section);
            case DATA_TABLE:
                return generateDataTable(section);
            case CHART:
                return generateChart(section);
            case METRICS_GRID:
                return generateMetricsGrid(section);
            default:
                throw new IllegalArgumentException("Unsupported section type: " + section.getType());
        }
    }
    
    /**
     * Generate executive summary section
     */
    private SectionOutput generateExecutiveSummary(ReportSection section) {
        Map<String, Object> data = parameters.getDataForSection(section.getId());
        
        // Business logic for executive summary
        String summary = template.generateExecutiveSummary(data);
        List<String> keyInsights = extractKeyInsights(data);
        Map<String, Double> kpis = calculateSectionKPIs(data);
        
        return SectionOutput.builder()
            .withSectionId(section.getId())
            .withContent(summary)
            .withKeyInsights(keyInsights)
            .withKpis(kpis)
            .build();
    }
    
    /**
     * Generate data table section
     */
    private SectionOutput generateDataTable(ReportSection section) {
        List<Map<String, Object>> tableData = parameters.getTableDataForSection(section.getId());
        
        // Apply sorting and filtering
        if (section.hasSorting()) {
            tableData = section.applySorting(tableData);
        }
        
        if (section.hasFiltering()) {
            tableData = section.applyFiltering(tableData);
        }
        
        return SectionOutput.builder()
            .withSectionId(section.getId())
            .withTableData(tableData)
            .withRowCount(tableData.size())
            .build();
    }
    
    /**
     * Generate chart section
     */
    private SectionOutput generateChart(ReportSection section) {
        Map<String, Object> chartData = parameters.getChartDataForSection(section.getId());
        
        // Business logic for chart generation
        ChartConfiguration chartConfig = section.getChartConfiguration();
        String chartImagePath = template.generateChart(chartData, chartConfig);
        
        return SectionOutput.builder()
            .withSectionId(section.getId())
            .withChartImagePath(chartImagePath)
            .withChartData(chartData)
            .build();
    }
    
    /**
     * Generate metrics grid section
     */
    private SectionOutput generateMetricsGrid(ReportSection section) {
        Map<String, Double> metrics = parameters.getMetricsForSection(section.getId());
        
        // Apply business rules for metric display
        Map<String, MetricDisplay> displayMetrics = metrics.entrySet().stream()
            .collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                entry -> createMetricDisplay(entry.getKey(), entry.getValue())
            ));
        
        return SectionOutput.builder()
            .withSectionId(section.getId())
            .withMetrics(displayMetrics)
            .build();
    }
    
    /**
     * Create metric display with formatting and status
     */
    private MetricDisplay createMetricDisplay(String metricName, Double value) {
        // Business logic for metric display
        String formattedValue = formatMetricValue(value);
        MetricStatus status = determineMetricStatus(metricName, value);
        String trend = calculateMetricTrend(metricName, value);
        
        return MetricDisplay.builder()
            .withName(metricName)
            .withValue(value)
            .withFormattedValue(formattedValue)
            .withStatus(status)
            .withTrend(trend)
            .build();
    }
    
    /**
     * Extract key insights from data
     */
    private List<String> extractKeyInsights(Map<String, Object> data) {
        // Business logic for insight extraction
        return template.extractInsights(data);
    }
    
    /**
     * Calculate section KPIs
     */
    private Map<String, Double> calculateSectionKPIs(Map<String, Object> data) {
        return template.calculateKPIs(data);
    }
    
    /**
     * Format metric value based on type
     */
    private String formatMetricValue(Double value) {
        // Business formatting logic
        if (value >= 1000000) {
            return String.format("%.1fM", value / 1000000);
        } else if (value >= 1000) {
            return String.format("%.1fK", value / 1000);
        } else {
            return String.format("%.2f", value);
        }
    }
    
    /**
     * Determine metric status based on thresholds
     */
    private MetricStatus determineMetricStatus(String metricName, Double value) {
        // Business rules for metric status
        return configuration.getMetricStatus(metricName, value);
    }
    
    /**
     * Calculate metric trend
     */
    private String calculateMetricTrend(String metricName, Double value) {
        // Historical comparison logic
        return configuration.calculateTrend(metricName, value);
    }
    
    /**
     * Calculate total pages
     */
    private int calculateTotalPages() {
        return Math.max(1, (int) Math.ceil(sections.size() / (double) configuration.getSectionsPerPage()));
    }
    
    /**
     * Calculate file size estimate
     */
    private long calculateFileSize() {
        // Estimate based on content and format
        long baseSize = 50000; // 50KB base
        long sectionSize = sections.size() * 10000; // 10KB per section
        
        if (configuration.getOutputFormat() == OutputFormat.PDF) {
            return (long) ((baseSize + sectionSize) * 1.5); // PDF overhead
        } else {
            return baseSize + sectionSize;
        }
    }
    
    /**
     * Validate report can be generated
     */
    private void validateCanGenerate() {
        if (status == ReportStatus.GENERATING) {
            throw new IllegalStateException("Report is already being generated");
        }
        
        if (status == ReportStatus.COMPLETED) {
            throw new IllegalStateException("Report has already been generated");
        }
        
        if (sections.isEmpty()) {
            throw new IllegalStateException("Report must have at least one section");
        }
    }
    
    /**
     * Check if report is ready for export
     */
    public boolean isReadyForExport() {
        return status == ReportStatus.COMPLETED && output != null;
    }
    
    /**
     * Get report file path
     */
    public String getFilePath() {
        if (!isReadyForExport()) {
            throw new IllegalStateException("Report is not ready for export");
        }
        return output.getFilePath();
    }
    
    // Getters
    public ReportId getId() { return id; }
    public String getReportName() { return reportName; }
    public ReportType getReportType() { return reportType; }
    public ReportTemplate getTemplate() { return template; }
    public ReportParameters getParameters() { return parameters; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedBy() { return createdBy; }
    public ReportStatus getStatus() { return status; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public ReportOutput getOutput() { return output; }
    public List<ReportSection> getSections() { return List.copyOf(sections); }
    public Map<String, Object> getMetadata() { return Map.copyOf(metadata); }
    public ReportConfiguration getConfiguration() { return configuration; }

    /**
     * Static builder factory method
     */
    public static Builder builder() {
        return new Builder();
    }

    // Builder
    public static class Builder {
        private ReportId id;
        private String reportName;
        private ReportType reportType;
        private ReportTemplate template;
        private ReportParameters parameters;
        private String createdBy;
        private List<ReportSection> sections = List.of();
        private Map<String, Object> metadata = Map.of();
        private ReportConfiguration configuration;
        
        public Builder withId(ReportId id) {
            this.id = id;
            return this;
        }
        
        public Builder withReportName(String reportName) {
            this.reportName = reportName;
            return this;
        }
        
        public Builder withReportType(ReportType reportType) {
            this.reportType = reportType;
            return this;
        }
        
        public Builder withTemplate(ReportTemplate template) {
            this.template = template;
            return this;
        }
        
        public Builder withParameters(ReportParameters parameters) {
            this.parameters = parameters;
            return this;
        }
        
        public Builder withCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }
        
        public Builder withSections(List<ReportSection> sections) {
            this.sections = sections;
            return this;
        }
        
        public Builder withMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }
        
        public Builder withConfiguration(ReportConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public Report build() {
            return new Report(this);
        }
    }
}