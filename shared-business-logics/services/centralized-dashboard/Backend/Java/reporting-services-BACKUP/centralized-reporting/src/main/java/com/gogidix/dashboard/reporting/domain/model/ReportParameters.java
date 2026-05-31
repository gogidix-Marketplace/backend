package com.gogidix.dashboard.reporting.domain.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Report Parameters Value Object
 */
public class ReportParameters {
    private final Map<String, Object> parameters;
    private final String domain;
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    private ReportParameters(Builder builder) {
        this.parameters = Map.copyOf(builder.parameters);
        this.domain = builder.domain;
        this.fromDate = builder.fromDate;
        this.toDate = builder.toDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getDomain() {
        return domain;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public String getId() {
        return domain + "_" + fromDate + "_" + toDate;
    }

    public boolean hasSorting() {
        return parameters.containsKey("sort");
    }

    public boolean hasFiltering() {
        return parameters.containsKey("filter");
    }

    public java.util.List<java.util.Map<String, Object>> applySorting(java.util.List<java.util.Map<String, Object>> data) {
        return data;
    }

    public java.util.List<java.util.Map<String, Object>> applyFiltering(java.util.List<java.util.Map<String, Object>> data) {
        return data;
    }

    /**
     * Get data for specific section
     */
    public Map<String, Object> getDataForSection(String sectionId) {
        return new HashMap<>(parameters);
    }

    /**
     * Get table data for specific section
     */
    public java.util.List<Map<String, Object>> getTableDataForSection(String sectionId) {
        Object tableData = parameters.get("tableData_" + sectionId);
        if (tableData instanceof java.util.List) {
            return (java.util.List<Map<String, Object>>) tableData;
        }
        return java.util.List.of();
    }

    /**
     * Get chart data for specific section
     */
    public Map<String, Object> getChartDataForSection(String sectionId) {
        Object chartData = parameters.get("chartData_" + sectionId);
        if (chartData instanceof Map) {
            return (Map<String, Object>) chartData;
        }
        return Map.of();
    }

    /**
     * Get metrics for specific section
     */
    public Map<String, Double> getMetricsForSection(String sectionId) {
        Object metrics = parameters.get("metrics_" + sectionId);
        if (metrics instanceof Map) {
            return (Map<String, Double>) metrics;
        }
        return Map.of();
    }

    public static class Builder {
        private Map<String, Object> parameters = new HashMap<>();
        private String domain;
        private LocalDateTime fromDate;
        private LocalDateTime toDate;

        public Builder withParameters(Map<String, Object> parameters) {
            this.parameters = parameters != null ? parameters : new HashMap<>();
            return this;
        }

        public Builder withDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder withDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
            this.fromDate = fromDate;
            this.toDate = toDate;
            return this;
        }

        public Builder withConfiguration(ReportConfiguration configuration) {
            // Configuration is stored as part of parameters map
            if (configuration != null) {
                this.parameters.put("configuration", configuration);
            }
            return this;
        }

        public ReportParameters build() {
            return new ReportParameters(this);
        }
    }
}
